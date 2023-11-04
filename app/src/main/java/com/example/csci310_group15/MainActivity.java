package com.example.csci310_group15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // [ Member Variables ]
    // DB
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    // Others
    private HashMap<String, Boolean> mDepartmentIsClicked = new HashMap<>();

    // [ Methods ]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("CLASSES");

        // Fix navigation bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Connect DB
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("departments");

        // [ Load from DB ]
        load(); // Load departments & classes info
    }

    // ======================================================================== [ HELPER FUNCTIONS ]
    // [ LOAD CLASS/DEPARTMENTS INFO ] =============================================================
    private void load()
    {
        /*
        * [ Setup DB ]
        * Uncomment to update DB
        */
        //DB_ClassSetup dbClassSetup = new DB_ClassSetup();
        //dbClassSetup.LoadClassToDB();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    loadDepartmentTabs(snapshot);
                }
                else
                {
                    System.out.println("No classes data loaded");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error);
            }
        });

        // [ Load Menu Bar ]
        loadMenuBar();
    }

    /**
     *
     * @param snapshot Firebase data snapshot
     */
    private void loadDepartmentTabs(DataSnapshot snapshot)
    {
        // === [ DEPARTMENT SECTION ] ===
        // Adding department tabs to the main grid
        LayoutInflater li = LayoutInflater.from(this);
        LinearLayout mainGrid = findViewById(R.id.mainPageGrid);
        for(DataSnapshot department : snapshot.getChildren())
        {
            String currDepartment = department.getKey();
            // Add each department to isClicked map to track click behaviors
            mDepartmentIsClicked.put(currDepartment, false);

            // Render the overall department_tab (a linear layout) in mainGrid
            LinearLayout departmentTab = (LinearLayout) li.inflate(R.layout.activity_main_department_tab, mainGrid, false);
            // Set the text for department name
            TextView tv = departmentTab.findViewById(R.id.department);
            tv.setId(View.generateViewId());
            tv.setText(currDepartment);

            // === [ CLASS TAB SECTION ] ===
            // Adding affiliated classes to current department tab
            LinearLayout classGrid = departmentTab.findViewById(R.id.classGrid);
            loadClassTabs(li, classGrid, department.getChildren()); // Helper function to inflate class tabs
            // Reset id for class grid
            classGrid.setId(View.generateViewId());

            // Add onClick event to the current department tab for expand/hide affiliated classes
            departmentTab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view)
                {
                    ImageView triangle = departmentTab.findViewById(R.id.expandBttn);
                    // If true, the classGrid is currently visible
                    if(mDepartmentIsClicked.get(currDepartment))
                    {
                        mDepartmentIsClicked.put(currDepartment, false);
                        // Hide the classes
                        classGrid.setVisibility(View.GONE);
                        // Rotate the triangle
                        triangle.setRotation(-90);

                    }
                    // If false, the classGrid is currently gone
                    else {
                        mDepartmentIsClicked.put(currDepartment, true);
                        classGrid.setVisibility(View.VISIBLE);
                        // Rotate the triangle
                        triangle.setRotation(-180);
                    }
                }
            });

            mainGrid.addView(departmentTab);
        }
    }

    /**
     *
     * @param li Layout inflater to render class tab
     * @param classGrid Destination of rendered content
     * @param affiliatedClasses Firebase data snapshots
     */
    private void loadClassTabs(LayoutInflater li, LinearLayout classGrid,Iterable<DataSnapshot> affiliatedClasses)
    {
        // Adding affiliated classes to current department tab
        for(DataSnapshot affiliatedClass : affiliatedClasses)
        {
            // ====== [ Set Class Tab Attributes ] ======
            // Create a new class tab and render it (but not adding it yet by setting to false)
            LinearLayout classTab = (LinearLayout) li.inflate(R.layout.activity_main_class_tab, classGrid, false);
            // Fetch the text views
            TextView classNum = classTab.findViewById(R.id.classNum);
            TextView instructor = classTab.findViewById(R.id.instructor);
            TextView classTime = classTab.findViewById(R.id.classTime);
            // Update the information
            classNum.setText(affiliatedClass.child("num").getValue().toString());
            instructor.setText(affiliatedClass.child("instructor").getValue().toString());
            classTime.setText(affiliatedClass.child("time").getValue().toString());
            // Reset the id for each text views
            classNum.setId(View.generateViewId());
            instructor.setId(View.generateViewId());
            classTime.setId(View.generateViewId());

            // ====== [ Buttons Setup ] ======
            Button enrollBttn = classTab.findViewById(R.id.classTabBttnRight);
            Button rateBttn = classTab.findViewById(R.id.classTabBttnMid);
            Button classmatesBttn = classTab.findViewById(R.id.classTabBttnLeft);
            setButtons(enrollBttn, rateBttn, classmatesBttn, affiliatedClass);
            // Reset button ids to avoid duplication
            enrollBttn.setId(View.generateViewId());
            classmatesBttn.setId(View.generateViewId());

            // Adding the class tab to the class grid
            classGrid.addView(classTab);
        }
    }

    // [ SETUP BUTTONS ] ===========================================================================
    /**
     * Setup the given enrollment button based on the data associated with the
     * provided DataSnapshot
     * @param enrollBttn the enrollment button to be setup
     * @param classmatesBttn the classmates button to be setup
     * @param affiliatedClass the data from firebase necessary to setup button
     */
    private void setButtons(Button enrollBttn, Button rateBttn, Button classmatesBttn, DataSnapshot affiliatedClass)
    {
        DatabaseReference students = affiliatedClass.child("students").getRef();
        // Look up for curr use in current classes registration pool
        Query searchCurrUser = students.orderByValue().equalTo(mAuth.getUid());

        searchCurrUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // The user IS ENROLLED
                if(snapshot.exists())
                {
                    enableClassmatesBttn(classmatesBttn, affiliatedClass);
                    enableRateBttn(rateBttn, affiliatedClass);
                    disableEnrollBttn(enrollBttn, affiliatedClass);
                }
                // The user is NOT YET enrolled
                else {
                    // Set onclick event for enrolling user in the class
                    setActiveEnrollBttn(enrollBttn, rateBttn, classmatesBttn, students, affiliatedClass);
                    setInactiveRateBttn(rateBttn, affiliatedClass);
                    setInactiveClassmatesBttn(classmatesBttn, affiliatedClass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error" + error);
            }
        });

    }

    /**
     * Disable the give ENROLL BUTTON by changing its color to gray
     * and change the associated onclick event to a notification to the user
     * @param bttn the enrollment button to be disabled
     * @param affiliatedClass the data from firebase necessary for operation
     */
    private void disableEnrollBttn(Button bttn, DataSnapshot affiliatedClass)
    {
        // Change enroll button to color gray
        bttn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
        // Set onclick event for notification
        bttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                // Notify user that he/she is already enrolled
                String alertTitle = affiliatedClass.child("num").getValue().toString();
                String alertMssg = "You are already enrolled!";
                showAlertDialog(alertTitle, alertMssg);
            }
        });
    }

    /**
     * This function sets an active enroll button, which encompasses an onclick event that:
     *      1. allows adding current user to the DB under the current class
     *      2. show alert on success enrollment
     *      3. allow classmates button to be enabled
     * @param enrollBttn the enrollment button to be initilized
     * @param classmatesBttn the classmates button to be initialized (to be disabled when not enrolled)
     * @param students the reference from DB to allow set up onclick event for enrolling student
     * @param affiliatedClass data from DB for setup
     */
    private void setActiveEnrollBttn(Button enrollBttn, Button rateBttn, Button classmatesBttn, DatabaseReference students, DataSnapshot affiliatedClass)
    {
        enrollBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                // Enroll current user in the DB
                DatabaseReference enrollStudent = students.push();
                enrollStudent.setValue(mAuth.getUid());
                // Show notification of successful enrollment
                String title = affiliatedClass.child("num").getValue().toString();
                String mssg = "Enrollment Successful";
                showAlertDialog(title, mssg);
                // Disable the enrollment bttn
                disableEnrollBttn(enrollBttn, affiliatedClass);
                // Enable classmates chat list button
                enableClassmatesBttn(classmatesBttn, affiliatedClass);
                // Enaable rate button
                enableRateBttn(rateBttn, affiliatedClass);
            }
        });
    }

    private void enableRateBttn(Button bttn, DataSnapshot affiliatedClass)
    {
        // Change to active color
        bttn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
        // Set onClick event redirect to rate page
        bttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, RatePageList.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void setInactiveRateBttn(Button bttn, DataSnapshot affiliatedClass)
    {
        bttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String title = affiliatedClass.child("num").getValue().toString();
                String mssg = "Rate function are limited to enrolled students.";
                showAlertDialog(title, mssg);
            }
        });
    }

    /**
     * Set onClick event on given btton for redirection to chat lists
     * @param bttn the button to be associated with the onClick event
     */
    private void enableClassmatesBttn(Button bttn, DataSnapshot affiliatedClass)
    {
        // Change classmates button to color cardinal
        bttn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
        // Set onClick event for redirection to chat list
        bttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                // [ Redirection to ChatListFromClass activity ]
                // Prepare keys for navigating DB
                String department = affiliatedClass.child("department").getValue().toString();
                String id = affiliatedClass.child("id").getValue().toString();
                String classNum = affiliatedClass.child("num").getValue().toString();
                // Send intent
                Intent intent = new Intent(MainActivity.this, ChatListFromClassActivity.class);
                intent.putExtra("department", department);
                intent.putExtra("id", id);
                intent.putExtra("num", classNum);
                // Invoke the redirection
                MainActivity.this.startActivity(intent);
            }
        });
    }

    /**
     * Set onClick event to print alert
     * @param bttn the button to be associated with the onClick event
     * @param affiliatedClass data from DB for info
     */
    private void setInactiveClassmatesBttn(Button bttn, DataSnapshot affiliatedClass)
    {
        bttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String title = affiliatedClass.child("num").getValue().toString();
                String mssg = "View classmates function are limited to enrolled students.";
                showAlertDialog(title, mssg);
            }
        });
    }

    // [ ALERT MESSAGE DISPLAYS ] ==================================================================
    /**
     * Print alert messages based on the given message and title
     * @param alertTitle the title to be printed
     * @param alertMessage the content message to be printed
     */
    private void showAlertDialog(String alertTitle, String alertMessage)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Set alert title & message
        builder.setTitle(alertTitle);
        builder.setMessage(alertMessage);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
            }
        });

        // Create & show alert
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // [ MENU BAR ] ==================================================================
    private void loadMenuBar()
    {
        Button btnChat = findViewById(R.id.btnChat);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnProf = findViewById(R.id.btnProf);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnChat.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
            startActivity(intent);
        });
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnProf.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

}