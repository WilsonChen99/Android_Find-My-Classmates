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
import android.widget.Toast;

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
    private String receiverUid = null;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private boolean onPage = false;

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
        receiverUid = intent.getStringExtra("uid");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(name);

        senderHash = mAuth.getUid() + receiverUid;
        receiverHash = receiverUid + mAuth.getUid();

        myRef.child("chats").child(senderHash).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot child: snapshot.getChildren()) {
                    Message curr = child.getValue(Message.class);
                    messages.add(curr);
                }
                adapter.notifyDataSetChanged();
                int pos = messages.size() - 1;
                if (pos >= 0) {
                    recycle.scrollToPosition(pos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error in getting previous chats");
            }
        });

        myRef.child("usersNotify").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(mAuth.getUid()) && onPage) {
                    myRef.child("usersNotify").child(mAuth.getUid()).removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error in getting notifications");
            }
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

    public void sendMessage(View view) {
        myRef.child("blockedusers").child(receiverUid).orderByValue().equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    pushMessage();
                } else {
                    Toast.makeText(ChatActivity.this,"You can no longer send messages to this user", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error in getting blocked users");
            }
        });
    }

    public void pushMessage() {
        Message message = new Message(text.getText().toString(), mAuth.getUid());
        myRef.child("chats").child(senderHash).push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                myRef.child("chats").child(receiverHash).push().setValue(message);
            }
        });
        text.setText("");
        checkContacts();
        myRef.child("usersNotify").child(receiverUid).push().setValue("New Message");
    }

    public void checkContacts() {
        myRef.child("contacts").child(receiverUid).orderByValue().equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    addToContacts();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error in getting contacts");
            }
        });
    }

    public void addToContacts() {
        myRef.child("contacts").child(mAuth.getUid()).push().setValue(receiverUid).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                myRef.child("contacts").child(receiverUid).push().setValue(mAuth.getUid());
            }
        });
    }
}