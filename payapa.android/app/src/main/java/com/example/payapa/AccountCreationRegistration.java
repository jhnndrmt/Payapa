package com.example.payapa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountCreationRegistration extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_creation);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        EditText confirmPasswordEditText = findViewById(R.id.confirm_password);
        EditText studentIdEditText = findViewById(R.id.student_id);
        CheckBox termsConditionCheckBox = findViewById(R.id.terms_condition);
        Button registerBtn = findViewById(R.id.register_btn);
        TextView signInBtn = findViewById(R.id.signin);


        // Style the "Terms and Condition" text in CheckBox
        String fullText = "I have read and agreed to the Terms and Condition";
        String termsText = "Terms and Condition";

        SpannableString spannable = new SpannableString(fullText);
        int startIndex = fullText.indexOf(termsText);
        int endIndex = startIndex + termsText.length();

        // Make "Terms and Condition" bold, underlined, and clickable
        spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(AccountCreationRegistration.this, TermsConditionActivity.class);
                startActivity(intent);
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsConditionCheckBox.setText(spannable);
        termsConditionCheckBox.setMovementMethod(LinkMovementMethod.getInstance()); // Enable clickable link


        Intent intent = getIntent();

        String firstName = intent.getStringExtra("firstName");
        String middleName = intent.getStringExtra("middleName");
        String lastName = intent.getStringExtra("lastName");
        String age = intent.getStringExtra("age");
        String homeAddress = intent.getStringExtra("homeAddress");
        String contactNumber = intent.getStringExtra("contactNumber");
        String emailAddress = intent.getStringExtra("emailAddress");
        String fbLink = intent.getStringExtra("fbLink");
        String gender = intent.getStringExtra("gender");
        String role = intent.getStringExtra("role");

        String course;
        String year;
        String department;

        // Initialize based on role
        if (role.equals("Student")) {
            department = null;
            course = intent.getStringExtra("course");
            year = intent.getStringExtra("year");
        } else {
            course = null;
            year = null;
            if (role.equals("Personnel") || role.equals("Faculty")) {
                department = intent.getStringExtra("department");
            } else {
                department = null;
            }
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String confirmPassword = confirmPasswordEditText.getText().toString();
                final String studentIdString = studentIdEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || studentIdString.isEmpty()) {
                    Toast.makeText(AccountCreationRegistration.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(AccountCreationRegistration.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!termsConditionCheckBox.isChecked()) {
                    Toast.makeText(AccountCreationRegistration.this, "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show();
                    return;
                }

                final int studentId;
                try {
                    studentId = Integer.parseInt(studentIdString);
                } catch (NumberFormatException e) {
                    Toast.makeText(AccountCreationRegistration.this, "Invalid student ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(emailAddress, password)
                        .addOnCompleteListener(AccountCreationRegistration.this, task -> {
                            if (task.isSuccessful()) {
                                String uid = mAuth.getCurrentUser().getUid();

                                User newUser;
                                if (role.equals("Student")) {
                                    newUser = new User(firstName, middleName, lastName, age, homeAddress, contactNumber,
                                            emailAddress, fbLink, gender, course, year, studentId, username, null, role);
                                } else {
                                    newUser = new User(firstName, middleName, lastName, age, homeAddress, contactNumber,
                                            emailAddress, fbLink, gender, null, null, studentId, username, department, role);
                                }

                                db.collection("users").document(uid).set(newUser)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(AccountCreationRegistration.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(AccountCreationRegistration.this, HomepageActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(AccountCreationRegistration.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Toast.makeText(AccountCreationRegistration.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountCreationRegistration.this, LoginActivity.class));

            }
        });

    }
}
