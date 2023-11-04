package com.example.csci310_group15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MakeRate extends AppCompatActivity {

    private EditText work;
    private EditText score;
    private EditText attendance;
    private EditText lateHW;
    private EditText otherComment;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private String department = null;
    private String depID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_rate);

        work = findViewById(R.id.work);
        score = findViewById(R.id.score);
        attendance = findViewById(R.id.attendance);
        lateHW = findViewById(R.id.lateHW);
        otherComment = findViewById(R.id.otherComment);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        department = intent.getStringExtra("department");
        depID = intent.getStringExtra("id");
    }

    public void goToRating(View view) {
        String workload = work.getText().toString();
        String scor = score.getText().toString();
        String attend = attendance.getText().toString();
        String hw = lateHW.getText().toString();
        String comm = otherComment.getText().toString();

        if (workload.isEmpty() || scor.isEmpty() || attend.isEmpty() || hw.isEmpty() || comm.isEmpty()) {
            Toast.makeText(MakeRate.this,"Please fill out all fields", Toast.LENGTH_LONG).show();
            return;
        }

        Rating rating = new Rating(workload, scor, attend, hw, comm);

        myRef.child("departments").child(department).child(depID).child("ratings").push().setValue(rating);
    }
}