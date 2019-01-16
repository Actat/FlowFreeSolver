package com.example.actat.flowfreesolver;

public class Cell {
    private int color;
    // color -1 空欄
    // color 0から15 色のマス（中間）
    // color 100から115 色の始点または終点

    private int connection[];
    // connection[0] 上
    // connection[1] 右
    // connection[2] 下
    // connection[3] 左
    // connection  0 なし
    // connection  1 あり
    // connection -1 不可能

    Cell() {
        connection = new int[4];
        for (int i = 0; i < 4; i++) {
            connection[i] = 0;
        }
    }

    public void setColor(int c) {
        color = c;
    }
    public int getColor() {
        return color;
    }
    public void setConnection(int direction, int cnnctn) {
        if (direction >= 0 && direction < 4 && cnnctn >= -1 && cnnctn < 1) {
            connection[direction] = cnnctn;
        }
    }
    public int getConnection(int direction) {
        return connection[direction];
    }
}
