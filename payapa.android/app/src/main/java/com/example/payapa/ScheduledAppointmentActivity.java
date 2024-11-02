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
    private TextView schedDate, schedTime, schedMessage, noAppointmentText;
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

        schedDate = findViewById(R.id.scheduledDate);
        schedTime = findViewById(R.id.scheduledTime);
        schedMessage = findViewById(R.id.appointmentMessage);
        noAppointmentText = findViewById(R.id.noAppointmentText);
        appointmentCard = findViewById(R.id.appointmentCard);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d("UserId", "User ID: " + userId);

            loadingIndicator.setVisibility(View.VISIBLE);
            appointmentCard.setVisibility(View.GONE);
            noAppointmentText.setVisibility(View.GONE);

            // Access the user's scheduled appointments document by userId
            db.collection("scheduledAppointments")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        Log.d("ScheduledAppointments", "User ID: " + userId);
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                appointmentCard.setVisibility(View.VISIBLE);
                                noAppointmentText.setVisibility(View.GONE);

                                String date = document.getString("date");
                                String time = document.getString("time");
                                String appointmentMessage = document.getString("message");

                                schedDate.setText("Date: " + date);
                                schedTime.setText("Time: " + time);
                                schedMessage.setText("Message: " + appointmentMessage);
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
