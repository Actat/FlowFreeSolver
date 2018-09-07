package com.example.actat.flowfreesolver;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    int SIZE = 5;
    // int button_default;
    // Drawable button_ring[] = new Drawable[16];
    // int button_rect[] = new int[16];
    // Button button[] = new Button[SIZE * SIZE];

    int board[][];
    int selectCount = 0;

    CanvasView cv;
    int viewW = 0;
    int viewH = 0;

    long startTime, finishTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv = (CanvasView) findViewById(R.id.canvasview);
        board_init();
        // Log.v("INIT", "board_init finished");
        // button_init();
        // Log.v("INIT", "button_init finished");

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                reDraw();
                handler.postDelayed(this, 50);
            }
        };
        handler.post(r);

        ((Button)findViewById(R.id.button_reset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clickされた時の処理
                // Log.v("RESET", "button_reset clicked");
                // board_init();
                // button_init();
                // reDraw();
            }
        });

        ((Button)findViewById(R.id.button_solve)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clickされた時の処理
                // Log.v("SOLVE", "button_solve clicked");
                startTime = System.currentTimeMillis();
                AsyncTask<Object, Integer, Boolean> task = new AsyncTask<Object, Integer, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Object... objects) {
                        return solveProblem();
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        // Log.v("LOGIC", "solve finished." + "\t result: " + String.valueOf(result));
                        finishTime = System.currentTimeMillis();
                        if (result) {
                            Toast.makeText(getApplicationContext(), "solved in " + String.valueOf(finishTime - startTime) + " ms", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "no solution found", Toast.LENGTH_LONG).show();
                        }
                        // reDraw();
                    }
                };
                task.execute(this);

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        viewH = cv.getHeight();
        viewW = cv.getWidth();
    }


    // init
    private void board_init() {
        board = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = -1;
            }
        }
        selectCount = 0;
    }
    /*
    private void button_pushed(int num) {
        int row = num / SIZE;
        int col = num % SIZE;
        int color = 0;

        if (board[row][col] != -1) return;

        if (selectCount % 2 == 0) {
            color = selectCount / 2;
        } else {
            color = (selectCount - 1) / 2;
        }
        selectCount++;

        board[row][col] = color + 100;
        button[num].setBackground(button_ring[color]);
    }
    */
    // draw
    private void reDraw() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                copy[i][j] = board[i][j];
            }
        }
        cv.drawBoard(SIZE, viewH, viewW, copy);
    }

    // solve problems
    boolean solveProblem() {
        // 解決していたらtrueを返す
        if (isBoardSolved()) {
            // Log.v("LOGIC", "Board is Solved. return true");
            return true;
        }

        // debug用のdelay
        /*
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {}*/

        // 埋まっているのに解けていないということはダメ
        if (isBoardFilled() && !isBoardSolved()) {
            // Log.v("LOGIC", "Board is filled but not solved. return false");
            return false;
        }

        // まるでダメなら引き返す
        if (!isBoardQualified()) {
            // Log.v("LOGIC", "There is no future. return false");
            return false;
        }

        // 次に調べるマスを決める
        int row = -1, col = -1;
        {
            int point_max = 0;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == -1) {
                        // pointの高いマスを選択する
                        int point = 0;

                        // 始点終点の隣はポイントが高い
                        if (i - 1 >= 0 && board[i - 1][j] >= 100) point++;
                        if (j - 1 >= 0 && board[i][j - 1] >= 100) point++;
                        if (i + 1 < SIZE && board[i + 1][j] >= 100) point++;
                        if (j + 1 < SIZE && board[i][j + 1] >= 100) point++;

                        // すでに色がついているマスの隣はポイントが高い
                        if (i - 1 >= 0 && board[i - 1][j] >= 0) point++;
                        if (j - 1 >= 0 && board[i][j - 1] >= 0) point++;
                        if (i + 1 < SIZE && board[i + 1][j] >= 0) point++;
                        if (j + 1 < SIZE && board[i][j + 1] >= 0) point++;

                        // 周囲の空欄が少ないとポイントが高い
                        point += 4; // 負の数にならないための下駄
                        if (i - 1 >= 0 && board[i - 1][j] < 0) point--;
                        if (j - 1 >= 0 && board[i][j - 1] < 0) point--;
                        if (i + 1 < SIZE && board[i + 1][j] < 0) point--;
                        if (j + 1 < SIZE && board[i][j + 1] < 0) point--;

                        // 壁沿いだとポイントが高い
                        if (i - 1 < 0) point++;
                        if (j - 1 < 0) point++;
                        if (i + 1 >= SIZE) point++;
                        if (j + 1 >= SIZE) point++;

                        if (point >= point_max) {
                            point_max = point;
                            row = i;
                            col = j;
                        }
                    }
                }
            }
            if (row == -1 || col == -1) {
                // Log.v("SOLVE_LOGIC", "next box is not selected...");
                return false;
            }
        }

        // マスに色を与えて次に進む
        for (int i = 0; i < selectCount / 2; i++) {
            board[row][col] = i;
            // Log.v("SOLVE_LOGIC", "row: " + String.valueOf(row) + ",\tcol: " + String.valueOf(col) + ",\ti: " + String.valueOf(i));
            if (solveProblem()) {
                return true;
            } else {
                // Log.v("LOGIC", "This color is not good. Try next.");
                continue;
            }
        }
        board[row][col] = -1;
        return false;
    }
    private boolean isBoardFilled () {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] <0) return false;
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
                    if (i - 1 >= 0 && board[i - 1][j] % 100 == board[i][j] % 100) count++;
                    if (j - 1 >= 0 && board[i][j - 1] % 100 == board[i][j] % 100) count++;
                    if (i + 1 < SIZE && board[i + 1][j] % 100 == board[i][j] % 100) count++;
                    if (j + 1 < SIZE && board[i][j + 1] % 100 == board[i][j] % 100) count++;

                    if (board[i][j] >= 100 && count != 1) return false;
                    if (board[i][j] < 100 && count != 2) return false;
                }
            }
        }

        // 問題がない場合はtrue
        return true;
    }
    private boolean isBoardQualified () {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // 始点終点の場合
                if (board[i][j] >= 100) {
                    // 2本以上生えている場合はfalse
                    {
                        int count = 0;
                        if (i - 1 >= 0 && board[i - 1][j] % 100 == board[i][j] % 100) count++;
                        if (j - 1 >= 0 && board[i][j - 1] % 100 == board[i][j] % 100) count++;
                        if (j + 1 < SIZE && board[i][j + 1] % 100 == board[i][j] % 100) count++;
                        if (i + 1 < SIZE && board[i + 1][j] % 100 == board[i][j] % 100) count++;

                        if (count > 1) {
                            // Log.v("LOGIC", "Two lines start from this dot. return false");
                            return false;
                        }
                    }
                    // 周囲が埋まっているのに何も生えていない場合はfalse
                    {
                        // 周囲が埋まっているか確認
                        int blank = 0;
                        if (i - 1 >= 0 && board[i - 1][j] == -1) blank++;
                        if (j - 1 >= 0 && board[i][j - 1] == -1) blank++;
                        if (j + 1 < SIZE && board[i][j + 1] == -1) blank++;
                        if (i + 1 < SIZE && board[i + 1][j] == -1) blank++;
                        if (blank == 0) {
                            // 生えているかどうか確認
                            int count = 0;
                            if (i - 1 >= 0 && board[i - 1][j] % 100 == board[i][j] % 100) count++;
                            if (j - 1 >= 0 && board[i][j - 1] % 100 == board[i][j] % 100) count++;
                            if (j + 1 < SIZE && board[i][j + 1] % 100 == board[i][j] % 100) count++;
                            if (i + 1 < SIZE && board[i + 1][j] % 100 == board[i][j] % 100) count++;

                            if (count < 1) {
                                // Log.v("LOGIC", "No line starts from this dot. return false");
                                return false;
                            }
                        }
                    }

                }

                // マスの場合
                if (board[i][j] < 100) {
                    // 周囲の3マス以上が1色になっている場合はfalse
                    {
                        // Log.v("LOGIC", "3 box check start");
                        // 3マスの選び方は4通り
                        if (i - 1 >= 0 && j - 1 >= 0 && j + 1 < SIZE && board[i - 1][j] != -1 && board[i - 1][j] % 100 == board[i][j - 1] % 100 &&  board[i - 1][j] % 100 == board[i][j + 1] % 100) return false;
                        if (j - 1 >= 0 && j + 1 < SIZE && i + 1 < SIZE && board[i][j - 1] != -1 && board[i][j - 1] % 100 == board[i][j + 1] % 100 && board[i][j - 1] % 100 == board[i + 1][j] % 100) return false;
                        if (i - 1 >= 0 && j + 1 < SIZE && i + 1 < SIZE && board[i - 1][j] != -1 && board[i - 1][j] % 100 == board[i][j + 1] % 100 && board[i - 1][j] % 100 == board[i + 1][j] % 100) return false;
                        if (i - 1 >= 0 && j - 1 >= 0 && i + 1 < SIZE && board[i - 1][j] != -1 && board[i - 1][j] % 100 == board[i][j - 1] % 100 &&  board[i - 1][j] % 100 == board[i + 1][j] % 100) return false;
                    }

                    // 周囲が埋まっているが同じ色２つと接していない
                    {
                        // Log.v("LOGIC", "dead end check start");
                        // 周囲が埋まっているか確認
                        int blank = 0;
                        if (i - 1 >= 0 && board[i - 1][j] == -1) blank++;
                        if (j - 1 >= 0 && board[i][j - 1] == -1) blank++;
                        if (j + 1 < SIZE && board[i][j + 1] == -1) blank++;
                        if (i + 1 < SIZE && board[i + 1][j] == -1) blank++;
                        if (blank == 0 && board[i][j] != -1) {
                            // 同じ色2つと接しているか確認
                            // 注目しているマスに色が入っている場合はその色との比較をすることを考える
                            int count = 0;
                            if (i - 1 >= 0 && board[i - 1][j] % 100 == board[i][j] % 100) count++;
                            if (j - 1 >= 0 && board[i][j - 1] % 100 == board[i][j] % 100) count++;
                            if (j + 1 < SIZE && board[i][j + 1] % 100 == board[i][j] % 100) count++;
                            if (i + 1 < SIZE && board[i + 1][j] % 100 == board[i][j] % 100) count++;

                            if (count != 2) return false;
                        }
                    }

                    // 最大限努力しても同じ色2つと接することができない場合はfalse
                    // すでに同じ色になっている部分と空欄を合わせて2に到達しない場合はfalse
                    {
                        int count = 0;
                        if (i - 1 >= 0 && board[i][j] != -1 && (board[i - 1][j] == -1 || board[i - 1][j] % 100 == board[i][j] % 100)) count++;
                        if (j - 1 >= 0 && board[i][j] != -1 && (board[i][j - 1] == -1 || board[i][j - 1] % 100 == board[i][j] % 100)) count++;
                        if (i + 1 < SIZE && board[i][j] != -1 && (board[i + 1][j] == -1 || board[i + 1][j] % 100 == board[i][j] % 100)) count++;
                        if (j + 1 < SIZE && board[i][j] != -1 && (board[i][j + 1] == -1 || board[i][j + 1] % 100 == board[i][j] % 100)) count++;

                        // Log.v("LOGIC", "forword check" + "count: " + String.valueOf(count));
                        if (board[i][j] != -1 && count < 2) return false;
                    }
                    // Log.v("LOGIC", "checks for box is finished");
                }
            }
        }

        // 特に悪い部分が見つからなければtrue
        return true;
    }
}
