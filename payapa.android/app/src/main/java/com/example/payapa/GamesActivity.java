package com.example.payapa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class GamesActivity extends AppCompatActivity {
    private String[] levels = {"Low", "Medium", "High"};
    private String selectedLevel = "";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView tvTask;
    private Button btnCompleteTask;
    private List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        tvTask = findViewById(R.id.tv_task);
        Button btnBack = findViewById(R.id.btn_back);
        btnCompleteTask = findViewById(R.id.btn_complete_task);

        // Load and display a random task
        loadTasksFromJson();
        displayRandomTask();


        btnBack.setOnClickListener(v -> showLevelSelectionDialog());

        btnCompleteTask.setOnClickListener(v -> {
            markTaskAsComplete();
        });
    }

    private void markTaskAsComplete() {
        Toast.makeText(GamesActivity.this, "Task completed!", Toast.LENGTH_SHORT).show();
        displayRandomTask(); // Display next task after marking completion
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showLevelSelectionDialog();
    }

    private void showLevelSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GamesActivity.this);

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
            Toast.makeText(GamesActivity.this, "Selected: " + selectedLevel, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            finish();
        } else {
            Toast.makeText(GamesActivity.this, "Please select a level", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTasksFromJson() {
        try {
            InputStream inputStream = getAssets().open("tasks.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Task>>() {}.getType();
            tasks = gson.fromJson(json, listType);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading tasks", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayRandomTask() {
        if (tasks != null && !tasks.isEmpty()) {
            Task randomTask = getRandomTask();
            tvTask.setText(randomTask.task);
        } else {
            tvTask.setText("No tasks available");
        }
    }

    private Task getRandomTask() {
        Random random = new Random();
        int index = random.nextInt(tasks.size());
        return tasks.get(index);
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
                    .document("DailyTask")
                    .set(levelData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(GamesActivity.this, "Level saved successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(GamesActivity.this, "Error saving level: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(GamesActivity.this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }

    // Task class for JSON mapping
    private static class Task {
        int id;
        String task;
    }
}
