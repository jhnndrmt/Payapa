package com.example.payapa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.view.WindowInsetsCompat;

import androidx.activity.OnBackPressedCallback;

public class HomepageActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 123;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;
    private boolean doubleBackToExitPressedOnce = false;
    private Button camera;
    private ImageView profile, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        profile = findViewById(R.id.profile);
        message = findViewById(R.id.message);
        camera = findViewById(R.id.start_button);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, proceed with your camera-related code
            // ...
        }


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

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            if (imageBitmap != null) {
                Intent intent = new Intent(HomepageActivity.this, MainDetectActivity.class);
                intent.putExtra("captured_image", imageBitmap);
                startActivity(intent);
            }
        }
    }
}

