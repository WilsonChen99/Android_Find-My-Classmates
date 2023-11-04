package com.example.csci310_group15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RatePageList extends AppCompatActivity {

    private RecyclerView recycle;
    private ArrayList<User> users;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private String mDepartment;
    private String mId;
    private String mClassNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_page_list);

        // Fix navigation bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        // DB setup
        mRef = FirebaseDatabase.getInstance().getReference("departments");
        mAuth = FirebaseAuth.getInstance();

        // Get class info from intent
        Intent intent = getIntent();
        mDepartment = intent.getStringExtra("department");
        mId = intent.getStringExtra("id");
        mClassNum = intent.getStringExtra("num");

        // Change banner text
        ActionBar actionBar = getSupportActionBar();
        String title = "RATINGS   " + mClassNum;
        actionBar.setTitle(title);

        loadRatings();
    }

    public void onClickMakeRate(View view)
    {
        Intent intent = new Intent(RatePageList.this, MakeRate.class);
        intent.putExtra("department", mDepartment);
        intent.putExtra("id", mId);
        intent.putExtra("num", mClassNum);
        RatePageList.this.startActivity(intent);
    }

    private void loadRatings()
    {
        LayoutInflater li = LayoutInflater.from(RatePageList.this);
        LinearLayout ratePageListGrid = findViewById(R.id.ratePageListGrid);
        mRef.child(mDepartment).child(mId).child("ratings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot child : snapshot.getChildren())
                {
                    Rating rating = child.getValue(Rating.class);
                    LinearLayout rateTab = (LinearLayout) li.inflate(R.layout.activity_rate_tab, ratePageListGrid, false);
                    TextView scoreView = (TextView)rateTab.findViewById(R.id.rate_score);
                    TextView attendanceView = (TextView)rateTab.findViewById(R.id.rate_tab_attendance);
                    TextView workloadView = (TextView)rateTab.findViewById(R.id.rate_tab_workload);
                    TextView lateWorkView = (TextView)rateTab.findViewById(R.id.rate_tab_latework);
                    scoreView.setText(rating.getScore());
                    attendanceView.setText(rating.getAttendance());
                    workloadView.setText(rating.getWorkload());
                    lateWorkView.setText(rating.getLateHW());

                    ratePageListGrid.addView(rateTab);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error in RatePageList");
            }
        });
    }
}