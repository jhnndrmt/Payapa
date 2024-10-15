package com.example.payapa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import java.util.Locale;
import java.util.Map;

public class DiaryActivity extends AppCompatActivity {
    private String[] levels = {"Low", "Medium", "High"};
    private String selectedLevel = "";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diary);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Button btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DiaryActivity.this);
                builder.setTitle("Select Level")
                        .setSingleChoiceItems(levels, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedLevel = levels[which];
                            }
                        })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (!selectedLevel.isEmpty()) {
                                    saveSelection(selectedLevel);
                                    Toast.makeText(DiaryActivity.this, "Selected: " + selectedLevel, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(DiaryActivity.this, "Please select a level", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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
                    .document("Diary")
                    .set(levelData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(DiaryActivity.this, "Level saved successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(DiaryActivity.this, "Error saving level: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(DiaryActivity.this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }
}