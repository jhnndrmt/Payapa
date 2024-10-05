package com.example.payapa;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

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

        // Handle submit button click
        btnSubmit.setOnClickListener(v -> submitForm());

        // Apply insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Method to handle form submission
    private void submitForm() {
        String firstName = etFirstName.getText().toString().trim();
        String concern = etConcern.getText().toString().trim();
        String reasonForStress = etReasonForStress.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError("First name is required");
            return;
        }
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

        // Create a new appointment data map
        Map<String, Object> appointment = new HashMap<>();
        appointment.put("firstName", firstName);
        appointment.put("concern", concern);
        appointment.put("reasonForStress", reasonForStress);

        // Send data to Firestore
        db.collection("appointments")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {
                    // Show a toast message when data is successfully submitted
                    Toast.makeText(AppoinmentActivity.this, "Your message has been sent to the admin.", Toast.LENGTH_SHORT).show();

                    // Clear the input fields
                    etFirstName.setText("");
                    etConcern.setText("");
                    etReasonForStress.setText("");
                })
                .addOnFailureListener(e -> {
                    // Show an error message in case of failure
                    Toast.makeText(AppoinmentActivity.this, "Failed to send the message. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }
}