package com.example.payapa;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.view.WindowInsetsCompat;

import androidx.activity.OnBackPressedCallback;

public class HomepageActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;

    private ImageView profile, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        profile = findViewById(R.id.profile);
        message = findViewById(R.id.message);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this,ProfileActivity.class));
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this,MessageActivity.class));
            }
        });


        handleBackPress();
    }
    private void handleBackPress() {
        // Handle back press to exit app
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    // Exit the app
                    finishAffinity();
                }

                doubleBackToExitPressedOnce = true;
                Toast.makeText(HomepageActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();

                // Reset flag after 2 seconds
                new android.os.Handler().postDelayed(
                        () -> doubleBackToExitPressedOnce = false,
                        2000 // 2 seconds delay
                );
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}

