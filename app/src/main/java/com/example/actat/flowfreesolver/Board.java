package com.example.actat.flowfreesolver;

public class Board {
    private int size = 5;
    private final int MIN_SIZE = 5;
    private final int MAX_SIZE = 12;
    private Cell[][] board; // [row][col]

    public Board(int s) {
        if (s < MIN_SIZE) {
            s = MIN_SIZE;
        }
        if (s > MAX_SIZE) {
            s = MAX_SIZE;
        }
        size = s;
        board = new Cell[size][size];
    }

    public int getSize() {
        return size;
    }

    public boolean isInRange(int raw, int col) {
        return  raw >= 0 && raw < size && col >= 0 && col < size;
    }

    public int getColor(int raw, int col) {
        if (isInRange(raw, col)) {
            return board[raw][col].getColor();
        }
        return -100;
    }

    public void setColor(int raw, int col, int color) {
        if (isInRange(raw, col)) {
            board[raw][col].setColor(color);
        }
    }

    public int getMaxJoint(int raw, int col, int joint) {
        if (isInRange(raw, col)) {
            return board[raw][col].getMaxJoint();
        }
        return -100;
    }

    public void setEndPoint(int raw, int col, int color) {
        if (isInRange(raw, col)) {
            board[raw][col].setColor(color);
            board[raw][col].setMaxJoint(1);
        }
    }
}
