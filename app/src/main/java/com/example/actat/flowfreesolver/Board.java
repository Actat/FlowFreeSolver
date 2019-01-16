package com.example.actat.flowfreesolver;


public class Board {
    private int SIZE = 5;
    private int MIN_SIZE = 5;
    private int MAX_SIZE = 12;
    private int numDot = 0;
    private Cell board[][];

    // solve problem
    private boolean isBoardFilled () {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].getColor() <0) return false;
            }
        }
        return true;
    }
    private boolean isBoardSolved () {
        // 埋まっていないマスがあったらfalse
        if (!isBoardFilled()) return false;

        // 必要な数のマスとつながっていない場合はfalse
        {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    int count = 0;
                    if (i - 1 >= 0 && board[i - 1][j].getColor() % 100 == board[i][j].getColor() % 100) count++;
                    if (j - 1 >= 0 && board[i][j - 1].getColor() % 100 == board[i][j].getColor() % 100) count++;
                    if (i + 1 < SIZE && board[i + 1][j].getColor() % 100 == board[i][j].getColor() % 100) count++;
                    if (j + 1 < SIZE && board[i][j + 1].getColor() % 100 == board[i][j].getColor() % 100) count++;

                    if (board[i][j].getColor() >= 100 && count != 1) return false;
                    if (board[i][j].getColor() < 100 && count != 2) return false;
                }
            }
        }

        // 問題がない場合はtrue
        return true;
    }
    public boolean solveProblem() {
        if (isBoardSolved()) {
            return true;
        }

        // 接続が確定する限り接続を作り続ける
        boolean newConnectionFlag = true;
        while (newConnectionFlag) {
            newConnectionFlag = false;
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (numPossibleNewConnection(row, col) < numNeededNewConnection(row, col)) {
                        // 矛盾が発生した場合はfalseを返しておしまい
                        return false;
                    } else if (numPossibleNewConnection(row, col) == numNeededNewConnection(row, col)) {
                        createAllPossibleConnection(row, col);
                        newConnectionFlag = true;
                    }
                }
            }
        }

        // 可能な接続の数 > 必要な接続 のところに接続を仮置きして探索を進める
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (numPossibleNewConnection(row, col) > numNeededNewConnection(row, col)) {
                    // 子供に渡すboardをつくる
                    Board boardChild = new Board();
                    boardChild.copyFrom(this);
                    // 接続を仮置きする
                    boardChild.putConnectionTemporary(row, col);
                    // 子供に渡す
                    if (boardChild.solveProblem()) {
                        copyFrom(boardChild);
                        return true;
                    }
                }
            }
        }

        return false;
    }
    private int numPossibleNewConnection(int row, int col) {
        int num = 0;
        for (int direction = 0; direction < 4; direction++) {
            if (board[row][col].getConnection(direction) == 0) {
                num++;
            }
        }
        return num;
    }
    private int numNeededNewConnection(int row, int col) {
        int num = 2;
        if (board[row][col].getColor() >= 100 && board[row][col].getColor() <= 115){
            num = 1;
        }
        for (int direction = 0; direction < 4; direction++) {
            if (board[row][col].getConnection(direction) == 1) {
                num--;
            }
        }
        return num;
    }

    public void board_init() {
        board = new Cell[getSize()][getSize()];
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                board[i][j].setColor(-1);
            }
        }
        for (int i = 0; i < getSize(); i++) {
            board[0][i].setConnection(0, -1);
        }
        for (int i = 0; i < getSize(); i++) {
            board[i][SIZE - 1].setConnection(1, -1);
        }
        for (int i = 0; i < getSize(); i++) {
            board[SIZE - 1][i].setConnection(2, -1);
        }
        for (int i = 0; i < getSize(); i++) {
            board[i][0].setConnection(3, -1);
        }
        numDot = 0;
    }
    public void putDot(int row, int col) {
        int color = 0;

        if (board[row][col].getColor() != -1) return;

        if (numDot % 2 == 0) {
            color = numDot / 2;
        } else {
            color = (numDot - 1) / 2;
        }
        numDot++;

        board[row][col].setColor(color + 100);
    }
    public int getSize() {
        return SIZE;
    }
    public int getMinSize() {
        return MIN_SIZE;
    }
    public int getMaxSize() {
        return MAX_SIZE;
    }
    public int getBoard(int col, int raw) {
        return board[col][raw].getColor();
    }
    public void incrementSize() {
        if (SIZE < getMaxSize()) {
            SIZE++;
        }
    }
    public void decrementSizse() {
        if (SIZE > getMinSize()) {
            SIZE--;
        }
    }

}
