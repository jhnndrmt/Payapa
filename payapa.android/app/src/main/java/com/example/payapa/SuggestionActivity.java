package com.example.payapa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SuggestionActivity extends AppCompatActivity {

    private TextView stressLevelTextView, suggestionTextView;
    private Button doneButton;
    private ImageView stressIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        stressLevelTextView = findViewById(R.id.stress_level_text);
        doneButton = findViewById(R.id.btn_done);
        stressIcon = findViewById(R.id.stress_icon);
        suggestionTextView = findViewById(R.id.suggestion_text);

        // Get the data from Intent
        Intent intent = getIntent();
        String stressLevel = intent.getStringExtra("stressLevel");

        // Display the results
        stressLevelTextView.setText(stressLevel);

        if ("Low".equals(stressLevel)) {
            stressIcon.setImageResource(R.drawable.smileicon);
        } else if ("Mid".equals(stressLevel)) {
            stressIcon.setImageResource(R.drawable.sadicon);
        } else if ("High".equals(stressLevel)) {
            stressIcon.setImageResource(R.drawable.cryicon);
        }

        // Set the suggestion based on the stress level
        switch (stressLevel) {
            case "Low":
                suggestionTextView.setText(R.string.suggestion_low);
                break;
            case "Mid":
                suggestionTextView.setText(R.string.suggestion_mid);
                break;
            case "High":
                suggestionTextView.setText(R.string.suggestion_high);
                break;
            default:
                suggestionTextView.setText("");
                break;
        }


        // Set up the Done button listener
        doneButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(SuggestionActivity.this, HomepageActivity.class);
            startActivity(homeIntent);
            finish(); // Optionally finish this activity to remove it from the back stack
        });
    }
}
