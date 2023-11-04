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
import java.util.HashMap;
import java.util.Map;

public class RatePageList extends AppCompatActivity {

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private String mDepartment;
    private String mId;
    private String mClassNum;
    private HashMap<String, Rating> mRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_page_list);

        // Fix navigation bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        // DB setup
        mRef = FirebaseDatabase.getInstance().getReference("departments");
        mAuth = FirebaseAuth.getInstance();
        mRatings = new HashMap<String, Rating>();

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
        mRef.child(mDepartment).child(mId).child("ratings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot child:snapshot.getChildren())
                {
                    String hashKey = child.getKey();
                    Rating rating = child.getValue(Rating.class);
                    mRatings.put(hashKey, rating);
                }

                for(Map.Entry<String, Rating> entry : mRatings.entrySet())
                {
                    Rating rating = entry.getValue();
                    String hash = entry.getKey();
                    LinearLayout rateTab = (LinearLayout) li.inflate(R.layout.activity_rate_tab, ratePageListGrid, false);
                    // Get Views
                    TextView scoreView = (TextView)rateTab.findViewById(R.id.rate_score);
                    TextView attendanceView = (TextView)rateTab.findViewById(R.id.rate_tab_attendance);
                    TextView workloadView = (TextView)rateTab.findViewById(R.id.rate_tab_workload);
                    TextView lateWorkView = (TextView)rateTab.findViewById(R.id.rate_tab_latework);
                    TextView voteView = (TextView)rateTab.findViewById(R.id.rate_tab_vote);
                    // Set text
                    scoreView.setText(rating.getScore());
                    attendanceView.setText(rating.getAttendance());
                    workloadView.setText(rating.getWorkload());
                    lateWorkView.setText(rating.getLateHW());
                    voteView.setText(Integer.toString(rating.getVote()));

                    // Change score color
                    int score = Integer.valueOf(rating.getScore());
                    if(score > 3)
                    {
                        scoreView.setTextColor(getResources().getColor(R.color.green));
                    }
                    else if(score == 3)
                    {
                        scoreView.setTextColor(getResources().getColor(R.color.gold));
                    }
                    else
                    {
                        scoreView.setTextColor(getResources().getColor(R.color.cardinal));
                    }

                    ratePageListGrid.addView(rateTab);

                    // Vote Button Setup
                    Button upVoteBttn = (Button)findViewById(R.id.rate_tab_upvote);
                    upVoteBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int vote = rating.getVote() + 1;
                            rating.setVote(vote);
                            TextView voteView = (TextView)rateTab.findViewById(R.id.rate_tab_vote);
                            voteView.setText(String.valueOf(vote));
                            mRef.child(mDepartment).child(mId).child("ratings").child(hash).child("vote").getRef().setValue(vote);
                        }
                    });
                    upVoteBttn.setId(View.generateViewId());

                    Button downVoteBttn = (Button)findViewById(R.id.rate_tab_downvote);
                    downVoteBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int vote = rating.getVote() - 1;
                            rating.setVote(vote);
                            TextView voteView = (TextView)rateTab.findViewById(R.id.rate_tab_vote);
                            voteView.setText(String.valueOf(vote));
                            mRef.child(mDepartment).child(mId).child("ratings").child(hash).child("vote").getRef().setValue(vote);
                        }
                    });
                    downVoteBttn.setId(View.generateViewId());

                    // Comment buttom setup
                    Button commentBttn = (Button)findViewById(R.id.rate_comment_btn);
                    commentBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showAlertDialog(mClassNum, rating.getComment());
                        }
                    });
                    commentBttn.setId(View.generateViewId());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error in RatePageList");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(RatePageList.this);

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
}