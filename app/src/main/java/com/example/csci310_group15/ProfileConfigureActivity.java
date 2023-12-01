package com.example.csci310_group15;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ProfileConfigureActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private StorageReference storageRef;
    private ImageView img;
    private Uri imgUri;
    private Uri downloadURL;
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
        storageRef = FirebaseStorage.getInstance().getReference();
        imgUri = null;
        downloadURL = null;
        img = findViewById(R.id.imageView);

        load();

    }
    private void load() {

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
            // Name and standing
            DatabaseReference user = myRef.child("users").child(mAuth.getUid());
            user.child("name").setValue(tv.getText().toString());
            user.child("standing").setValue(tx.getSelectedItem().toString().toLowerCase());
            Toast.makeText(ProfileConfigureActivity.this,"Successfully updated profile", Toast.LENGTH_LONG).show();
            // Update profile pic

            if (imgUri == null) {
                finish();
                return;
            }

            StorageReference photoRef = storageRef.child("images").child(mAuth.getUid());
            photoRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadURL = uri;
                            myRef.child("users").child(mAuth.getUid()).child("uri").setValue(downloadURL.toString());
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            System.out.println("Error getting URL");
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("Error in image upload");
                }
            });
        });
    }

    public void goToGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Stil good for API 24
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            imgUri = data.getData();
            img.setImageURI(imgUri);
        }
    }

}
