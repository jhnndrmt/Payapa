package com.example.payapa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GamesActivity extends AppCompatActivity {

    private String[] levels = {"Low", "Medium", "High"};
    private String selectedLevel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        LinearLayout tictac = findViewById(R.id.tictac);
        LinearLayout cardflip = findViewById(R.id.cardflip);
        Button btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GamesActivity.this);
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
                                    Toast.makeText(GamesActivity.this, "Selected: " + selectedLevel, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(GamesActivity.this, "Please select a level", Toast.LENGTH_SHORT).show();
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