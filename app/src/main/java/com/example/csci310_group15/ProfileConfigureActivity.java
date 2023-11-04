package com.example.csci310_group15;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ProfileConfigureActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    EditText tv;
    Spinner tx;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("CONFIGURE PROFILE");
        setContentView(R.layout.activity_profile_configure);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        load();

    }
    private void load(){

        tv = findViewById(R.id.txtName);
        tx = findViewById(R.id.stand);


        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSave = findViewById(R.id.btnSave);

        myRef.child("users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                tv.setText(user.getName());
                String standing = user.getStanding().toLowerCase();
                for(int i = 0; i < tx.getAdapter().getCount(); i++){
                    if (tx.getAdapter().getItem(i).toString().toLowerCase().equals(standing)){
                        tx.setSelection(i);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error retrieving user");
            }
        });

        // To update user value
        //
        btnCancel.setOnClickListener(view -> {
            finish();
        });

        btnSave.setOnClickListener(view -> {
            //TODO validate first
            DatabaseReference user = myRef.child("users").child(mAuth.getUid());
            user.child("name").setValue(tv.getText().toString());
            user.child("standing").setValue(tx.getSelectedItem().toString().toLowerCase());
            finish();
        });

    }

}
