package com.example.payapa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView settings;
    private ImageButton message;
    private ImageButton sentimentAnalysis;
    private ImageButton userProfile;
    private View games;
    private View music;
    private View diary;
    private View article;
    private TextView consultation, scheduledAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);


        settings = findViewById(R.id.settings);
        message = findViewById(R.id.message);
        sentimentAnalysis = findViewById(R.id.sentiment_analysis);
        userProfile = findViewById(R.id.profile);
        games = findViewById(R.id.games);
        music = findViewById(R.id.music);
        diary = findViewById(R.id.diary);
        article = findViewById(R.id.article);
        consultation = findViewById(R.id.clicktoschedule);
        scheduledAppointment = findViewById(R.id.scheduledAppointment);

        scheduledAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfileActivity.this, ScheduledAppointmentActivity.class));
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ManageprofileActivity.class));
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MessageActivity.class));
            }
        });

        sentimentAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, HomepageActivity.class));
            }
        });


        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, GamesActivity.class));
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MusicsActivity.class));
            }
        });

        diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, DiaryActivity.class));
            }
        });

        article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ArticleActivity.class));
            }
        });

        consultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ConsultationActivity.class));
            }
        });

    }
}