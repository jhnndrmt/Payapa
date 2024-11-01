package com.example.payapa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ArticleActivity extends AppCompatActivity {

    private String[] levels = {"Low", "Medium", "High"};
    private String selectedLevel = "";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Button btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLevelSelectionDialog();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showLevelSelectionDialog();
    }

    private void showLevelSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ArticleActivity.this);

        TextView alertMessage = new TextView(this);
        alertMessage.setText("On a scale of 1 to 3, how did the article make you feel?");
        alertMessage.setTextSize(14);
        alertMessage.setTypeface(alertMessage.getTypeface(), Typeface.ITALIC);
        alertMessage.setGravity(Gravity.CENTER);
        alertMessage.setPadding(0, 40, 0, 0);
        alertMessage.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) alertMessage.getLayoutParams();
        params.setMargins(0, 40, 0, 20);
        alertMessage.setLayoutParams(params);

        builder.setCustomTitle(alertMessage);

        // Inflate custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.stress_level_selection, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Set listeners for each level
        dialogView.findViewById(R.id.low_stress_layout).setOnClickListener(v -> {
            selectedLevel = "Low";
            saveSelectionAndCloseDialog(dialog);
        });

        dialogView.findViewById(R.id.medium_stress_layout).setOnClickListener(v -> {
            selectedLevel = "Medium";
            saveSelectionAndCloseDialog(dialog);
        });

        dialogView.findViewById(R.id.high_stress_layout).setOnClickListener(v -> {
            selectedLevel = "High";
            saveSelectionAndCloseDialog(dialog);
        });

        dialog.show();
    }

    private void saveSelectionAndCloseDialog(AlertDialog dialog) {
        if (!selectedLevel.isEmpty()) {
            saveSelection(selectedLevel);
            Toast.makeText(ArticleActivity.this, "Selected: " + selectedLevel, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            finish();
        } else {
            Toast.makeText(ArticleActivity.this, "Please select a level", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveSelection(String level) {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            String formattedTimestamp = sdf.format(new Date());

            Map<String, Object> levelData = new HashMap<>();
            levelData.put("user_id", userId);
            levelData.put("selected_level", level);
            levelData.put("timestamp", formattedTimestamp);

            db.collection("user_selections")
                    .document("Article")
                    .set(levelData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(ArticleActivity.this, "Level saved successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ArticleActivity.this, "Error saving level: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(ArticleActivity.this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
