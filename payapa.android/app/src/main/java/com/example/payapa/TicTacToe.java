package com.example.payapa;

public class TicTacToe {
    private static final int SIZE = 3;
    private static final int EMPTY = 0;
    private static final int PLAYER_X = 1;
    private static final int PLAYER_O = 2;

    private int[][] board;
    private boolean isPlayerXTurn;

    public TicTacToe() {
        board = new int[SIZE][SIZE];
        isPlayerXTurn = true;
    }

    public boolean isPlayerXTurn() {
        return isPlayerXTurn;
    }

    public boolean makeMove(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == EMPTY) {
            board[row][col] = isPlayerXTurn ? PLAYER_X : PLAYER_O;
            isPlayerXTurn = !isPlayerXTurn;
            return true;
        }
        return false;
    }

    public boolean checkWin() {
        // Check rows, columns, and diagonals
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] != EMPTY && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return true;
            if (board[0][i] != EMPTY && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return true;
        }
        if (board[0][0] != EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return true;
        if (board[0][2] != EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return true;
        return false;
    }

    public boolean checkDraw() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) return false;
            }
        }
        return !checkWin();
    }

    public void reset() {
        board = new int[SIZE][SIZE];
        isPlayerXTurn = true;
    }

    public int[] getBestMove() {
        int bestValue = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = PLAYER_O;
                    int moveValue = minimax(0, false);
                    board[i][j] = EMPTY;

                    if (moveValue > bestValue) {
                        bestMove = new int[]{i, j};
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMax) {
        if (checkWin()) return isMax ? -10 : 10;
        if (checkDraw()) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = PLAYER_O;
                        best = Math.max(best, minimax(depth + 1, false));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = PLAYER_X;
                        best = Math.min(best, minimax(depth + 1, true));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return best;
        }
    }
}