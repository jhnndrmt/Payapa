package com.example.payapa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ManageprofileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseAuth auth;

    private EditText nicknameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ImageView profileImageView;
    private Button uploadProfilePictureButton;
    private Button saveChangesButton, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageprofile);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        nicknameEditText = findViewById(R.id.nicknameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        profileImageView = findViewById(R.id.profileImageView);
        uploadProfilePictureButton = findViewById(R.id.uploadProfilePictureButton);
        saveChangesButton = findViewById(R.id.saveChangesButton);
        logoutBtn = findViewById(R.id.logoutBtn);

        String userId = auth.getCurrentUser().getUid();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // Fetch user data from Firestore
        db.collection("users").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String firstName = documentSnapshot.getString("firstName");
                            String username = documentSnapshot.getString("username");
                            String email = documentSnapshot.getString("email");
                            String profilePictureUrl = documentSnapshot.getString("profilePictureUrl");

                            // Debug logs to verify data
                            Log.d("ManageprofileActivity", "Name: " + firstName);
                            Log.d("ManageprofileActivity", "Nickname: " + username);
                            Log.d("ManageprofileActivity", "Email: " + email);
                            Log.d("ManageprofileActivity", "Profile Picture URL: " + profilePictureUrl);

                            // Set data to EditTexts
                            nicknameEditText.setText(firstName != null ? firstName : (username != null ? username : ""));
                            emailEditText.setText(email != null ? email : "");

                            // Load profile picture
                            if (profilePictureUrl != null) {
                                Glide.with(ManageprofileActivity.this)
                                        .load(profilePictureUrl)
                                        .into(profileImageView);
                            }
                        } else {
                            Toast.makeText(ManageprofileActivity.this, "No user data found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ManageprofileActivity", "Error getting documents.", e);
                        Toast.makeText(ManageprofileActivity.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                    }
                });

        // Upload profile picture
        uploadProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Save changes to Firestore and Firebase Authentication
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void logoutUser() {
        auth.signOut();
        Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ManageprofileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadProfilePicture(imageUri);
        }
    }

    private void uploadProfilePicture(Uri imageUri) {
        String userId = auth.getCurrentUser().getUid();
        StorageReference profilePictureRef = storage.getReference().child("profile_pictures/" + userId + ".jpg");

        profilePictureRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        profilePictureRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String profilePictureUrl = uri.toString();
                                updateProfilePictureUrl(profilePictureUrl);
                                Glide.with(ManageprofileActivity.this)
                                        .load(profilePictureUrl)
                                        .into(profileImageView);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManageprofileActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateProfilePictureUrl(String url) {
        String userId = auth.getCurrentUser().getUid();
        db.collection("users").document(userId).update("profilePictureUrl", url);
    }

    private void saveChanges() {
        String userId = auth.getCurrentUser().getUid();
        String newNickname = nicknameEditText.getText().toString();
        String newEmail = emailEditText.getText().toString();
        String newPassword = passwordEditText.getText().toString();

        // Update Firestore
        db.collection("users").document(userId).update("nickname", newNickname, "email", newEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // If the user has updated their email, apply the change
                        auth.getCurrentUser().updateEmail(newEmail)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Update user profile with new nickname
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(newNickname)
                                                .build();

                                        auth.getCurrentUser().updateProfile(profileUpdates)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        if (!newPassword.isEmpty()) {
                                                            auth.getCurrentUser().updatePassword(newPassword)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(ManageprofileActivity.this, "Changes saved successfully.", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(ManageprofileActivity.this, "Failed to save password change.", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        } else {
                                                            Toast.makeText(ManageprofileActivity.this, "Changes saved successfully.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ManageprofileActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ManageprofileActivity.this, "Failed to update email.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManageprofileActivity.this, "Failed to save changes.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}