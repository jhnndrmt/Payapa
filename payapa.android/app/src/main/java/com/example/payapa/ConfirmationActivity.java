package com.example.payapa;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;

public class ConfirmationActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private Button next;
    private Button selectPhotoButton;
    private TextView signin;
    private EditText firstNameEditText, lastNameEditText, nicknameEditText, studentIDEditText;
    private TextView idPhotoFileNameTextView; // Renamed this to reflect ID photo purpose

    private Uri photoUri;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Initialize Firebase Storage and Firestore
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        next = findViewById(R.id.next);
        selectPhotoButton = findViewById(R.id.selectPhotoButton);
        signin = findViewById(R.id.signin);
        firstNameEditText = findViewById(R.id.fname);
        lastNameEditText = findViewById(R.id.lname);
        nicknameEditText = findViewById(R.id.nname);
        studentIDEditText = findViewById(R.id.studentid);
        idPhotoFileNameTextView = findViewById(R.id.photoFileNameTextView); // Renamed this

        // Set up listeners
        next.setOnClickListener(v -> fillConfirmation());

        signin.setOnClickListener(v -> startActivity(new Intent(ConfirmationActivity.this, LoginActivity.class)));

        selectPhotoButton.setOnClickListener(v -> openImagePicker());

        // Check for the READ_EXTERNAL_STORAGE permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photoUri = data.getData();
            String photoFileName = getPhotoFileName(photoUri);
            idPhotoFileNameTextView.setText(photoFileName); // Updated to reflect ID photo
        } else {
            Toast.makeText(this, "Image selection canceled or failed", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPhotoFileName(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
            }
        }
        return "No photo selected";
    }

    private void fillConfirmation() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String nickname = nicknameEditText.getText().toString().trim();
        String studentIDString = studentIDEditText.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            firstNameEditText.setError("First Name is required.");
            return;
        }

        if (TextUtils.isEmpty(lastName)) {
            lastNameEditText.setError("Last Name is required.");
            return;
        }

        if (TextUtils.isEmpty(nickname)) {
            nicknameEditText.setError("Nickname is required.");
            return;
        }

        if (TextUtils.isEmpty(studentIDString)) {
            studentIDEditText.setError("Student ID is required.");
            return;
        }

        if (photoUri == null) {
            Toast.makeText(this, "Please select an ID photo.", Toast.LENGTH_SHORT).show(); // Updated message
            return;
        }

        int studentID;
        try {
            studentID = Integer.parseInt(studentIDString);
        } catch (NumberFormatException e) {
            studentIDEditText.setError("Student ID must be an integer.");
            return;
        }

        // Get a reference to the storage location where you want to save the image
        StorageReference photoRef = storageReference.child("id_photos/" + studentID + ".jpg"); // Updated folder name

        // Upload the image to Firebase Storage
        UploadTask uploadTask = photoRef.putFile(photoUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String downloadUrl = uri.toString();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                User userProfile = new User(user.getEmail(), firstName, lastName, nickname, studentID, downloadUrl);

                // Save user data to Firestore
                db.collection("users").document(userId).set(userProfile)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(ConfirmationActivity.this, "ID photo uploaded and user data saved successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ConfirmationActivity.this, Confirmation2Activity.class));
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ConfirmationActivity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "User is not authenticated.", Toast.LENGTH_SHORT).show();
            }
        })).addOnFailureListener(e -> Toast.makeText(ConfirmationActivity.this, "Failed to upload ID photo.", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}