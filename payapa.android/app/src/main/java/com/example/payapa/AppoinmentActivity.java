package com.example.payapa;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppoinmentActivity extends AppCompatActivity {

    private EditText etFirstName, etConcern, etReasonForStress;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appoinment);

        // Initialize views
        etFirstName = findViewById(R.id.et_first_name);
        etConcern = findViewById(R.id.et_concern);
        etReasonForStress = findViewById(R.id.et_reason_for_stress);
        btnSubmit = findViewById(R.id.btn_submit);

        fetchAndDisplayFirstName();


        // Handle submit button click
        btnSubmit.setOnClickListener(v -> submitForm());

        // Apply insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchAndDisplayFirstName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String firstName = documentSnapshot.getString("firstName");
                    if (!TextUtils.isEmpty(firstName)) {
                        etFirstName.setText(firstName);
                        etFirstName.setTypeface(null, Typeface.BOLD);
                        etFirstName.setEnabled(false);
                    }
                }
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Error fetching user first name", e);
            });
        }
    }

    private void submitForm() {
        String concern = etConcern.getText().toString().trim();
        String reasonForStress = etReasonForStress.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(concern)) {
            etConcern.setError("Concern is required");
            return;
        }
        if (TextUtils.isEmpty(reasonForStress)) {
            etReasonForStress.setError("Please provide reasons for stress");
            return;
        }

        // Get Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String uid = user.getUid();
            db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                String firstName;
                if (documentSnapshot.exists()) {
                    firstName = documentSnapshot.getString("firstName");
                } else {
                    // If first name doesn't exist in Firestore, get it from EditText
                    firstName = etFirstName.getText().toString().trim();
                }

                if (!TextUtils.isEmpty(firstName)) {
                    // Submit appointment data
                    submitAppointment(db, uid, firstName, concern, reasonForStress);
                } else {
                    Toast.makeText(AppoinmentActivity.this, "First name is missing.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Error checking user document", e);
            });
        }
    }

    private void submitAppointment(FirebaseFirestore db, String uid, String firstName, String concern, String reasonForStress) {
        // Create a new appointment data map
        Map<String, Object> appointment = new HashMap<>();
        appointment.put("firstName", firstName);
        appointment.put("concern", concern);
        appointment.put("reasonForStress", reasonForStress);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = sdf.format(new Date());
        appointment.put("timestamp", formattedDate);


        // Save the appointment data under 'appointments/{uid}/{auto-generated-id}'
        db.collection("appointments").document(uid)
                .set(appointment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AppoinmentActivity.this, "Your message has been sent to the admin.", Toast.LENGTH_SHORT).show();

                    // Clear the input fields
                    etConcern.setText("");
                    etReasonForStress.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AppoinmentActivity.this, "Failed to send the message. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }
}