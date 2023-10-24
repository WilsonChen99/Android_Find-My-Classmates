package com.example.csci310_group15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView recycle;
    private ArrayList<User> users;
    private ChatListAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("MY CHATS");

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        users = new ArrayList<User>();
        recycle = findViewById(R.id.recycle);
        adapter = new ChatListAdapter(this, users);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(adapter);

        // Once chat is finished update this to actually get recent conversations
        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot child: snapshot.getChildren()) {
                    User curr = child.getValue(User.class);
                    if (curr.getUid().equals(mAuth.getUid())) {
                        continue;
                    }
                    users.add(curr);
                }
                adapter.notifyDataSetChanged();
                System.out.println("HEYYYYY");
                System.out.println(users.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error in ChatListActivity");
            }
        });
    }
}