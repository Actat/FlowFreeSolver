package com.example.actat.flowfreesolver;


import android.util.Log;

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
    private boolean isBoardQualified() {
        // 2x2のマスが全部同色の場合は不成立
        for (int row = 0; row < SIZE - 1; row++) {
            for (int col = 0; col < SIZE - 1; col++) {
                if ((isConnected(row, col, row, col + 1)
                        && isConnected(row, col + 1, row + 1, col + 1)
                        && isConnected(row + 1, col + 1, row + 1, col)
                        && isConnected(row + 1, col, row, col)
                ||  board[row][col].getColor() != -1
                        &&(board[row][col].getColor() % 100 == board[row][col + 1].getColor() % 100
                        && board[row][col + 1].getColor() % 100 == board[row + 1][col + 1].getColor() % 100
                        && board[row + 1][col + 1].getColor() == board[row + 1][col].getColor() % 100
                        && board[row + 1][col].getColor() % 100 == board[row][col].getColor() % 100))) {
                    return false;
                }
            }
        }
        // loopがある場合は不成立
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col].getColor() == -1 && numConnection(board[row][col]) == 2) {
                    for (int direction = 0; direction < 4; direction++) {
                        if (board[row][col].getConnection(direction) == 1) {
                            if (isLoopExistRecursive(getRowOfNeighbor(row, col, direction), getColOfNeighbor(row, col, direction), oppositeDirection(direction), row, col)) {
                                return false;
                            }
                            break;
                        }
                    }
                }
            }
        }

        return true;
    }
    public boolean solveProblem() {
        if (isBoardSolved()) {
            return true;
        }

        eraseImpossibleConnection();
        // 接続が確定する限り接続を作り続ける
        boolean newConnectionFlag = true;
        while (newConnectionFlag) {
            newConnectionFlag = false;
            if (!isBoardQualified()) {
                return false;
            }
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (numPossibleNewConnection(board[row][col]) < numNeededNewConnection(board[row][col])) {
                        // 矛盾が発生した場合はfalseを返しておしまい
                        return false;
                    } else if (numPossibleNewConnection(board[row][col]) != 0 && numPossibleNewConnection(board[row][col]) == numNeededNewConnection(board[row][col])) {
                        createAllPossibleConnection(row, col);
                        newConnectionFlag = true;
                    } else if (board[row][col].getColor() != -1 && numPossibleNewConnection(board[row][col]) > 0) {
                        for (int d = 0; d < 4; d++) {
                            Cell neighbor = getNeighborCell(row, col, d);
                            if (neighbor != null && board[row][col].getColor() % 100 == neighbor.getColor() % 100) {
                                connect(row, col, d);
                            }
                        }
                    }
                }
            }
        }

        if (isBoardSolved()) {
            return true;
        }

        // 可能な接続の数 > 必要な接続 のところに接続を仮置きして探索を進める
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (numPossibleNewConnection(board[row][col]) > numNeededNewConnection(board[row][col])) {
                    // 子供に渡すboardをつくる
                    Board boardBackup = new Board();
                    boardBackup.copyFrom(this);
                    // 接続を仮置きして解いてみる
                    for (int d = 0; d < 4; d++) {
                        if (canConnect(row, col, d)) {
                            connect(row, col, d);
                            if (solveProblem()) {
                                return true;
                            } else {
                                copyFrom(boardBackup);
                                disconnect(row, col, d);
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
    private int numPossibleNewConnection(Cell cell) {
        int num = 0;
        for (int direction = 0; direction < 4; direction++) {
            if (cell.getConnection(direction) == 0) {
                num++;
            }
        }
        return num;
    }
    private int numNeededNewConnection(Cell cell) {
        int num = 2;
        if (cell.getColor() >= 100 && cell.getColor() <= 115){
            num = 1;
        }
        for (int direction = 0; direction < 4; direction++) {
            if (cell.getConnection(direction) == 1) {
                num--;
            }
        }
        return num;
    }
    private int numConnection(Cell cell) {
        int num = 0;
        for (int direction = 0; direction < 4; direction++) {
            if (cell.getConnection(direction) == 1) {
                num++;
            }
        }
        return num;
    }
    private void createAllPossibleConnection(int row, int col) {
        for (int direction = 0; direction < 4; direction++) {
            if (canConnect(row, col, direction)) {
                connect(row, col, direction);
            }
        }
    }
    private void connect(int row, int col, int direction) {
        if (canConnect(row, col, direction)) {
            Cell neighbor = getNeighborCell(row, col, direction);
            board[row][col].setConnection(direction, 1);
            neighbor.setConnection(oppositeDirection(direction), 1);
            if (board[row][col].getColor() == -1 && neighbor.getColor() != -1) {
                setColorRecursive(getRowOfNeighbor(row, col, direction), getColOfNeighbor(row, col, direction), oppositeDirection(direction));
            }
            if (board[row][col].getColor() != -1 && neighbor.getColor() == -1) {
                setColorRecursive(row, col, direction);
            }
            eraseImpossibleConnection();
            processConnectionSaturation(row, col);
            processConnectionSaturation(getRowOfNeighbor(row, col, direction), getColOfNeighbor(row, col, direction));
        }
    }
    private void disconnect(int row, int col, int direction) {
        Cell neighbor = getNeighborCell(row, col, direction);
        board[row][col].setConnection(direction, -1);
        neighbor.setConnection(oppositeDirection(direction), -1);
        processConnectionSaturation(row, col);
        processConnectionSaturation(getRowOfNeighbor(row, col, direction), getColOfNeighbor(row, col, direction));
    }
    private void processConnectionSaturation(int row, int col) {
        int neededConnection = 2;
        int existingConnection = 0;
        // 始点終点では最大接続数1
        if (board[row][col].getColor() >= 100 && board[row][col].getColor() <=115) {
            neededConnection = 1;
        }
        for (int i = 0; i < 4; i++) {
            if (board[row][col].getConnection(i) == 1) {
                existingConnection++;
            }
        }

        if (existingConnection >= neededConnection) {
            for(int i = 0; i < 4; i++) {
                if (board[row][col].getConnection(i) == 0) {
                    board[row][col].setConnection(i, -1);
                    getNeighborCell(row, col, i).setConnection(oppositeDirection(i), -1);
                }
            }
        }
    }
    private boolean canConnect (int row, int col, int direction) {
        Cell neighbor = getNeighborCell(row, col, direction);
        if (neighbor == null) {
            return false;
        }
        return board[row][col].getConnection(direction) == 0 && neighbor.getConnection(oppositeDirection(direction)) == 0;
    }
    private int oppositeDirection (int direction) {return (direction + 2) % 4;}
    private Cell getNeighborCell(int row, int col, int direction) {
        if (direction == 0 && row > 0) {
            return board[row - 1][col];
        } else if (direction == 1 && col < SIZE - 1) {
            return board[row][col + 1];
        } else if (direction == 2 && row < SIZE - 1) {
            return board[row + 1][col];
        } else if (direction == 3 && col > 0) {
            return board[row][col - 1];
        } else {
            return null;
        }
    }
    private void eraseImpossibleConnection() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                for (int direction = 0; direction < 4; direction++) {
                    Cell neighbor = getNeighborCell(row, col, direction);
                    if (neighbor != null && neighbor.getColor() != -1 && board[row][col].getColor() != -1 && neighbor.getColor() % 100 != board[row][col].getColor() % 100) {
                        board[row][col].setConnection(direction, -1);
                        neighbor.setConnection(oppositeDirection(direction), -1);
                    }
                }
            }
        }
    }
    private void setColorRecursive(int row, int col, int direction) {
        Cell neighbor = board[getRowOfNeighbor(row, col, direction)][getColOfNeighbor(row, col, direction)];
        if (neighbor.getColor() == -1) {
            neighbor.setColor(board[row][col].getColor() % 100);
            for (int d = 0; d < 4; d++) {
                if (d != oppositeDirection(direction) && neighbor.getConnection(d) == 1) {
                    setColorRecursive(getRowOfNeighbor(row, col, direction), getColOfNeighbor(row, col, direction), d);
                }
            }
        }
    }
    private int getRowOfNeighbor(int row, int col, int direction) {
        if (direction == 0) {
            return row - 1;
        } else if (direction == 2) {
            return row + 1;
        } else {
            return row;
        }
    }
    private int getColOfNeighbor(int row, int col, int direction) {
        if (direction == 1) {
            return col + 1;
        } else if (direction == 3) {
            return col - 1;
        } else {
            return col;
        }
    }
    private boolean isLoopExistRecursive(int row, int col, int direction, int startRow, int startCol) {
        // 調べるcellのrow,colと，前のcellの向き，スタートのrow, col

        // cellが空欄でなければ関係ないし，両端が接続されていなければ関係ない
        if (board[row][col].getColor() == -1 && numConnection(board[row][col]) == 2) {
            for (int d = 0; d < 4; d++) {
                if (d != direction && board[row][col].getConnection(d) == 1) {
                    if (getRowOfNeighbor(row, col, d) == startRow && getColOfNeighbor(row, col, d) == startCol) {
                        // スタートに戻ってきたらloop
                        return true;
                    } else {
                        // 続いているのでその先を調べる
                        return isLoopExistRecursive(getRowOfNeighbor(row, col, d), getColOfNeighbor(row, col, d), oppositeDirection(d), startRow, startCol);
                    }
                }
            }
        }
        return false;
    }
    private boolean isConnected(int rowStart, int colStart, int rowGoal, int colGoal) {
        for (int direction = 0; direction < 4; direction++) {
            if (board[rowStart][colStart].getConnection(direction) == 1) {
                if (getRowOfNeighbor(rowStart, colStart, direction) == rowGoal && getColOfNeighbor(rowStart, colStart, direction) == colGoal) {
                    return true;
                }
                if (isConnectedRecursive(getRowOfNeighbor(rowStart, colStart, direction), getColOfNeighbor(rowStart, colStart, direction), oppositeDirection(direction), rowGoal, colGoal)) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isConnectedRecursive(int row, int col, int direction, int rowGoal, int colGoal) {
        if (numConnection(board[row][col]) == 2) {
            for (int d = 0; d < 4; d++) {
                if (d != direction && board[row][col].getConnection(d) == 1) {
                    if (getRowOfNeighbor(row, col, d) == rowGoal && getColOfNeighbor(row, col, d) == colGoal) {
                        return true;
                    } else {
                        return isConnectedRecursive(getRowOfNeighbor(row, col, d), getColOfNeighbor(row, col, d), oppositeDirection(d), rowGoal, colGoal);
                    }
                }
            }
        }
        return false;
    }

    public void board_init() {
        board = new Cell[getSize()][getSize()];
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                board[i][j] = new Cell();
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
    public int getSize() { return SIZE; }
    public int getMinSize() { return MIN_SIZE; }
    public int getMaxSize() { return MAX_SIZE; }
    public int getColor(int col, int raw) { return board[col][raw].getColor(); }
    public void incrementSize() {
        if (SIZE < getMaxSize()) {
            SIZE++;
        }
    }
    public void decrementSize() {
        if (SIZE > getMinSize()) {
            SIZE--;
        }
    }
    private void setSIZE(int s) {
        if (s >= MIN_SIZE && s <= MAX_SIZE) {
            SIZE = s;
        }
    }
    public void copyFrom(Board from) {
        if (SIZE != from.getSize() || board == null) {
            setSIZE(from.getSize());
            board_init();
        }
        setNumDot(from.getNumDot());
        for (int row = 0; row < getSize(); row++) {
            for (int col = 0; col < getSize(); col++) {
                board[row][col].setColor(from.getColor(row, col));
                for (int direction = 0; direction < 4; direction++) {
                    board[row][col].setConnection(direction, from.getConnection(row, col, direction));
                }
            }
        }
    }
    public int getConnection(int row, int col, int direction) {
        return board[row][col].getConnection(direction);
    }
    public void setNumDot(int n) { numDot = n; }
    public int getNumDot() { return numDot; }
}
