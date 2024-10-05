package com.example.payapa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GamesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        LinearLayout tictac = findViewById(R.id.tictac);
        LinearLayout cardflip = findViewById(R.id.cardflip);

        tictac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamesActivity.this, TicTacToeActivity.class);
                startActivity(intent);
            }
        });

        cardflip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GamesActivity.this, CardFlipActivityActivity.class);
                startActivity(intent);
            }
        });
    }
}