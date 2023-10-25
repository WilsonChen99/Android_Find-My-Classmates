package com.example.csci310_group15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recycle;
    private EditText text;
    private ChatAdapter adapter;
    private ArrayList<Message> messages;
    private String senderHash = null;
    private String receiverHash = null;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        text = findViewById(R.id.message);
        messages = new ArrayList<Message>();
        adapter = new ChatAdapter(messages);
        recycle = findViewById(R.id.chatRecycle);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(adapter);
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String uid = intent.getStringExtra("uid");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(name);

        senderHash = mAuth.getUid() + uid;
        receiverHash = uid + mAuth.getUid();

        myRef.child("chats").child(senderHash).child("texts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot child: snapshot.getChildren()) {
                    Message curr = child.getValue(Message.class);
                    messages.add(curr);
                }
                adapter.notifyDataSetChanged();
                int pos = messages.size() - 1;
                recycle.scrollToPosition(pos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error in getting previous chats");
            }
        });
    }

    public void sendMessage(View view) {
        // Check to see if user has blocked you
        // Add user to contacts list
        // Notify other user through android
        Message message = new Message(text.getText().toString(), mAuth.getUid());
        myRef.child("chats").child(senderHash).child("texts").push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                myRef.child("chats").child(receiverHash).child("texts").push().setValue(message);
            }
        });
        text.setText("");
    }
}