package com.example.payapa;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TicTacToeActivity extends AppCompatActivity {

    private TicTacToe game;
    private TextView statusTextView;
    private GridLayout gameGrid;
    private Button restartButton;
    private Handler handler = new Handler(); // Handler for posting delayed tasks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        game = new TicTacToe();

        statusTextView = findViewById(R.id.statusTextView);
        gameGrid = findViewById(R.id.gameGrid);
        restartButton = findViewById(R.id.restartButton);

        int[][] buttonIds = {
                {R.id.button00, R.id.button01, R.id.button02},
                {R.id.button10, R.id.button11, R.id.button12},
                {R.id.button20, R.id.button21, R.id.button22}
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ImageButton button = findViewById(buttonIds[i][j]);
                final int row = i;
                final int col = j;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleButtonClick(row, col, (ImageButton) v);
                    }
                });
            }
        }

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });
    }

    private void handleButtonClick(int row, int col, ImageButton button) {
        if (game.isPlayerXTurn()) {
            if (game.makeMove(row, col)) {
                button.setImageResource(R.drawable.x); // Set X icon
                statusTextView.setText("AI's turn");

                // Check for win or draw
                if (game.checkWin()) {
                    statusTextView.setText("Player X wins!");
                } else if (game.checkDraw()) {
                    statusTextView.setText("It's a draw!");
                } else {
                    // Delay before AI move
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            aiMove();
                        }
                    }, 1000); // 1-second delay
                }
            } else {
                Toast.makeText(this, "Invalid move", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void aiMove() {
        // AI makes a move
        int[] move = game.getBestMove();
        if (move != null) {
            int row = move[0];
            int col = move[1];
            ImageButton button = findViewById(getButtonId(row, col));
            if (game.makeMove(row, col)) {
                button.setImageResource(R.drawable.o); // Set O icon
                statusTextView.setText("Player X's turn");

                // Check for win or draw
                if (game.checkWin()) {
                    statusTextView.setText("Player O wins!");
                } else if (game.checkDraw()) {
                    statusTextView.setText("It's a draw!");
                }
            }
        }
    }

    private int getButtonId(int row, int col) {
        int[][] buttonIds = {
                {R.id.button00, R.id.button01, R.id.button02},
                {R.id.button10, R.id.button11, R.id.button12},
                {R.id.button20, R.id.button21, R.id.button22}
        };
        return buttonIds[row][col];
    }

    private void restartGame() {
        game.reset();
        statusTextView.setText("Player X's turn");
        for (int i = 0; i < gameGrid.getChildCount(); i++) {
            View view = gameGrid.getChildAt(i);
            if (view instanceof ImageButton) {
                ((ImageButton) view).setImageDrawable(null);
            }
        }
    }
}