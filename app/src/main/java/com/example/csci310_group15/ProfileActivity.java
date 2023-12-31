package com.example.csci310_group15;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

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
    TextView tz;

    ImageView profilePic;
    private boolean onPage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PROFILE");

        // Fix navigation bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        setContentView(R.layout.activity_user_profile);
        load();
        //setContentView(R.layout.activity_profile);
    }

    private void load(){
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        tv = findViewById(R.id.name);
        tx = findViewById(R.id.stand);
        tz = findViewById(R.id.uscid);

        profilePic = findViewById(R.id.imageViewProfile);
        Button btnChat = findViewById(R.id.btnChat);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnProf = findViewById(R.id.btnProf);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnConfigure = findViewById(R.id.btnConfigure);

        myRef.child("users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                tv.setText(user.getName());
                tx.setText(user.getStanding());
                tz.setText(user.getUscID());
                Picasso.get().load(user.getUri()).into(profilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error retrieving user");
            }
        });

        myRef.child("usersNotify").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(mAuth.getUid()) && onPage) {
                    myRef.child("usersNotify").child(mAuth.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // Still good for API 24
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(ProfileActivity.this)
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setContentTitle("Find My Classmates")
                                    .setContentText("You have a new message!")
                                    .setAutoCancel(true);
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ProfileActivity.this);
                            // Ignore
                            notificationManager.notify(1, builder.build());
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error in getting notifications");
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
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        btnProf.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        btnConfigure.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileConfigureActivity.class);
            startActivity(intent);
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
