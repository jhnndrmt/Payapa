package com.example.payapa;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ScheduledAppointmentActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView noAppointmentText;
    private LinearLayout appointmentsContainer;
    private ProgressBar loadingIndicator;
    private Button acceptBtn, declineBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scheduled_activity);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        noAppointmentText = findViewById(R.id.noAppointmentText);
        appointmentsContainer = findViewById(R.id.appointmentsContainer);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d("UserId", "User ID: " + userId);

            loadingIndicator.setVisibility(View.VISIBLE);
            noAppointmentText.setVisibility(View.GONE);

            db.collection("scheduledAppointments")
                    .document(userId)
                    .collection("appointments")
                    .get()
                    .addOnCompleteListener(task -> {
                        loadingIndicator.setVisibility(View.GONE);

                        if (task.isSuccessful() && task.getResult() != null) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (!querySnapshot.isEmpty()) {
                                appointmentsContainer.removeAllViews();

                                for (DocumentSnapshot document : querySnapshot.getDocuments()) {


                                    String date = document.getString("date");
                                    String time = document.getString("time");
                                    String message = document.getString("message");
                                    String documentId = document.getId();

                                    addAppointmentCard(date, time, message, documentId);
                                }
                            } else {
                                noAppointmentText.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.e("ScheduledAppointment", "Error fetching data", task.getException());
                        }
                    });
        }
    }

    private void addAppointmentCard(String date, String time, String message, String documentId) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.card_appointment, appointmentsContainer, false);

        TextView schedDate = cardView.findViewById(R.id.scheduledDate);
        TextView schedTime = cardView.findViewById(R.id.scheduledTime);
        TextView schedMessage = cardView.findViewById(R.id.appointmentMessage);
        Button acceptBtn = cardView.findViewById(R.id.accept_btn);
        Button declineBtn = cardView.findViewById(R.id.decline_btn);

        schedDate.setText("Date: " + date);
        schedTime.setText("Time: " + time);
        schedMessage.setText("Message: " + message);

        // Fetch the response field from Firestore
        db.collection("scheduledAppointments")
                .document(currentUser.getUid())
                .collection("appointments")
                .document(documentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String response = documentSnapshot.getString("respond");

                        // Set button states based on the response
                        if ("Accepted".equals(response)) {
                            acceptBtn.setEnabled(false);
                            declineBtn.setEnabled(true);
                            acceptBtn.setBackgroundColor(getResources().getColor(R.color.activeButtonColor));
                            declineBtn.setBackgroundColor(getResources().getColor(R.color.inactiveButtonColor));
                        } else if ("Declined".equals(response)) {
                            acceptBtn.setEnabled(true);
                            declineBtn.setEnabled(false);
                            acceptBtn.setBackgroundColor(getResources().getColor(R.color.inactiveButtonColor));
                            declineBtn.setBackgroundColor(getResources().getColor(R.color.activeButtonColor));
                        }
                    }
                });

        // Button click listener
        View.OnClickListener responseClickListener = v -> {
            boolean isAcceptButton = (v.getId() == R.id.accept_btn);
            String response = isAcceptButton ? "Accepted" : "Declined";

            // Update response in Firestore
            updateResponseField(documentId, response);

            // Activate the selected button and disable the other
            acceptBtn.setEnabled(!isAcceptButton);
            declineBtn.setEnabled(isAcceptButton);

            // Change appearance for active and inactive buttons
            acceptBtn.setBackgroundColor(getResources().getColor(
                    isAcceptButton ? R.color.activeButtonColor : R.color.inactiveButtonColor));
            declineBtn.setBackgroundColor(getResources().getColor(
                    isAcceptButton ? R.color.inactiveButtonColor : R.color.activeButtonColor));
        };

        // Assign listener to buttons
        acceptBtn.setOnClickListener(responseClickListener);
        declineBtn.setOnClickListener(responseClickListener);

        appointmentsContainer.addView(cardView);
    }




    private void updateResponseField(String documentId, String response) {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("scheduledAppointments")
                    .document(userId)
                    .collection("appointments")
                    .document(documentId)
                    .update("respond", response)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Response updated successfully"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating response", e));
        }
    }
}
