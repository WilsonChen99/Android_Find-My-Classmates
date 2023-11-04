package com.example.csci310_group15;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private static final int GALLERY_REQUEST_CODE = 0;
    private FirebaseUser currentUser;
    private ImageView img;
    private Uri imgUri;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    TextView tv;
    TextView tx;

    ImageView profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PROFILE");
        setContentView(R.layout.activity_user_profile);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        load();
        //setContentView(R.layout.activity_profile);



    }
    private void load(){
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        tv = findViewById(R.id.name);
        tx = findViewById(R.id.stand);

        profilePic = findViewById(R.id.imageViewProfile);
        Button btnChat = findViewById(R.id.btnChat);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnConfigure = findViewById(R.id.btnConfigure);

        myRef.child("users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                tv.setText(user.getName());
                tx.setText(user.getStanding());
                profilePic.setImageBitmap(getBitmap(user.getUri()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error retrieving user");
            }
        });

        // To update user value
        // myRef.child("users").child(mAuth.getUid()).child("name").setValue("sdknfkfj");
        btnChat.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ChatListActivity.class);
            startActivity(intent);
        });
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(view -> {
            mAuth.signOut();
            finish();
        });
        btnConfigure.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileConfigureActivity.class);
            startActivity(intent);
        });

    }

    private Bitmap getBitmap(String uri){
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            Bitmap profileBitmap = BitmapFactory.decodeStream(con.getInputStream());
            return profileBitmap;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
