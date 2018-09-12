package com.example.actat.flowfreesolver;

public class Cell {
    private int color = -1;
    private int maxJoint = 2;

    public Cell() {
        color = -1;
        maxJoint = 2;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int c) {
        if (c > -2 && c < 16) {
            color = c;
        }
    }

    public int getMaxJoint() {
        return maxJoint;
    }

    public void setMaxJoint(int j) {
        if (j == 1 || j == 2) {
            maxJoint = j;
        }
    }
}
