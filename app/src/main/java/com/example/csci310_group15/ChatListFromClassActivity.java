package com.example.csci310_group15;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListFromClassActivity extends AppCompatActivity {

    private RecyclerView recycle;
    private ArrayList<User> users;
    private ChatListAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private boolean onPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Fix navigation bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        // Get class info from DB
        Intent intent = getIntent();
        String department = intent.getStringExtra("department");
        String classId = intent.getStringExtra("id");
        String classNum =intent.getStringExtra("num");

        // Update Banner
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(classNum);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        users = new ArrayList<User>();
        recycle = findViewById(R.id.recycle);
        adapter = new ChatListAdapter(this, users);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(adapter);

        myRef.child("departments").child(department).child(classId).child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String uid = child.getValue(String.class);
                    // Add enrolled students to the class chat list excluding current user
                    if(!uid.equals(mAuth.getUid()))
                    {
                        myRef.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                System.out.println("Error retrieving user");
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error in ChatListActivity");
            }
        });

        Button btnChat = findViewById(R.id.btnChat);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnProf = findViewById(R.id.btnProf);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnChat.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, ChatListActivity.class);
            startActivity(intent1);
        });
        btnHome.setOnClickListener(view -> {
            Intent intent1 = new Intent(this, MainActivity.class);
            startActivity(intent1);
        });
        btnLogout.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent1 = new Intent(this, LoginActivity.class);
            startActivity(intent1);
        });

        btnProf.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, ProfileActivity.class);
            startActivity(intent1);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPage = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        onPage = false;
    }
}
