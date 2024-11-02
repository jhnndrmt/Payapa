package com.example.payapa;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import androidx.annotation.NonNull;

import java.util.Map;

public class ScheduledAppointmentActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView textFirstName, textReasonForStress, textConcern, textTimestamp, noAppointmentText;
    private androidx.cardview.widget.CardView appointmentCard;
    private ProgressBar loadingIndicator;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scheduled_activity);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        textFirstName = findViewById(R.id.textFirstName);
        textReasonForStress = findViewById(R.id.textReasonForStress);
        textConcern = findViewById(R.id.textConcern);
        textTimestamp = findViewById(R.id.textTimestamp);
        noAppointmentText = findViewById(R.id.noAppointmentText);
        appointmentCard = findViewById(R.id.appointmentCard);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        if (currentUser != null) {
            String userId = currentUser.getUid();

            loadingIndicator.setVisibility(View.VISIBLE);
            appointmentCard.setVisibility(View.GONE);
            noAppointmentText.setVisibility(View.GONE);

            // Access the user's appointments document by userId
            db.collection("appointments")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                appointmentCard.setVisibility(View.VISIBLE);
                                noAppointmentText.setVisibility(View.GONE);

                                String firstName = document.getString("firstName");
                                String reasonForStress = document.getString("reasonForStress");
                                String concern = document.getString("concern");
                                String timestamp = document.getString("timestamp");

                                textFirstName.setText("First Name: " + firstName);
                                textReasonForStress.setText("Reason for Stress: " + reasonForStress);
                                textConcern.setText("Concern: " + concern);
                                textTimestamp.setText("Date: " + timestamp);
                            }  else {
                                // Display 'No Appointment' message
                                appointmentCard.setVisibility(View.GONE);
                                noAppointmentText.setVisibility(View.VISIBLE);
                                loadingIndicator.setVisibility(View.GONE);

                                Log.d("ScheduledAppointment", "No appointments found for the user");
                            }
                        } else {
                            Log.e("ScheduledAppointment", "Error fetching data", task.getException());
                        }
                    });
        }
    }
}
