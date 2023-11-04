package com.example.csci310_group15;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private EditText role;
    private EditText ident;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private StorageReference storageRef;
    private ImageView img;
    private Uri imgUri;
    private Uri downloadURL;
    private static String[] roles = {"undergrad", "grad", "faculty", "staff"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("SIGN UP");

        // Fix navigation bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        role = findViewById(R.id.role);
        ident = findViewById(R.id.ID);
        img = findViewById(R.id.imageView);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        imgUri = null;
        downloadURL = null;

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

            StorageReference photoRef = storageRef.child("images").child(mAuth.getUid());
            photoRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadURL = uri;
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
        }
    }

    public void goToMain(View view) {
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        String nam = name.getText().toString();
        String id = ident.getText().toString();
        String standing = role.getText().toString().toLowerCase();

        if (mail.isEmpty() || pass.isEmpty() || nam.isEmpty() || id.isEmpty() || standing.isEmpty()) {
            Toast.makeText(SignUpActivity.this,"Please fill out all fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (!mail.contains("@usc.edu")) {
            Toast.makeText(SignUpActivity.this,"Please enter a valid USC email", Toast.LENGTH_LONG).show();
            return;
        }

        boolean flag = true;
        for (String role: roles) {
            if (standing.equalsIgnoreCase(role)) {
                flag = false;
            }
        }

        if (flag) {
            Toast.makeText(SignUpActivity.this,"Please enter a valid role", Toast.LENGTH_LONG).show();
            return;
        }

        if (imgUri == null) {
            Toast.makeText(SignUpActivity.this,"Please add a picture", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getUid();
                            User user = new User(nam, mail, standing, downloadURL.toString(), id, uid);
                            myRef.child("users").child(uid).setValue(user);

                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign up failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}