package com.example.payapa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Confirmation2Activity extends AppCompatActivity {

    private EditText emailEditText, courseEditText, ageEditText, genderEditText;
    private Button signup;
    private TextView signin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation2);

        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
        emailEditText = findViewById(R.id.email);
        courseEditText = findViewById(R.id.course);
        ageEditText = findViewById(R.id.age);
        genderEditText = findViewById(R.id.gender);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signup.setOnClickListener(v -> confirmProfile());

        signin.setOnClickListener(v -> startActivity(new Intent(Confirmation2Activity.this, LoginActivity.class)));
    }

    private void confirmProfile() {
        String email = emailEditText.getText().toString().trim();
        String course = courseEditText.getText().toString().trim();
        String age = ageEditText.getText().toString().trim();
        String gender = genderEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(course)) {
            courseEditText.setError("Course is required.");
            return;
        }

        if (TextUtils.isEmpty(age)) {
            ageEditText.setError("Age is required.");
            return;
        }

        if (TextUtils.isEmpty(gender)) {
            genderEditText.setError("Gender is required.");
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            UserProfileUpdate profileUpdate = new UserProfileUpdate(email, course, age, gender);

            // Update Firestore with additional user data
            db.collection("users").document(userId).update(
                    "email", email,
                    "course", course,
                    "age", age,
                    "gender", gender
            ).addOnSuccessListener(aVoid -> {
                Toast.makeText(Confirmation2Activity.this, "Profile completed successfully.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Confirmation2Activity.this, TermsConditionActivity.class));
            }).addOnFailureListener(e -> {
                Toast.makeText(Confirmation2Activity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "User is not authenticated.", Toast.LENGTH_SHORT).show();
        }
    }
}