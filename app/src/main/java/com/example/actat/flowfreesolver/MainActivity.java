package com.example.actat.flowfreesolver;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    CanvasView cv;
    int viewW = 0;
    int viewH = 0;

    Board board;

    long startTime, finishTime;

    private class SolveProblemInBackground extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            return board.solveProblem();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            finishTime = System.currentTimeMillis();
            handler.removeCallbacks(r);
            if (result) {
                Toast.makeText(getApplicationContext(), "solved in " + String.valueOf(finishTime - startTime) + " ms", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "no solution found", Toast.LENGTH_LONG).show();
            }
            reDraw();
        }
    }
    private AsyncTask solve;

    // 一定時間ごとに画面を更新する
    private final Handler handler = new Handler();
    private final Runnable r = new Runnable() {
        @Override
        public void run() {
            reDraw();
            handler.postDelayed(this, 50);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv = (CanvasView) findViewById(R.id.canvasview);
        cv.setMainActivity(this);
        board = new Board();
        board.board_init();
        reDraw();

        ((Button)findViewById(R.id.button_minus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                if (solve == null) {
                    handler.removeCallbacks(r);
                    board.decrementSize();
                    board.board_init();
                    reDraw();
                }
            }
        });

        ((Button)findViewById(R.id.button_plus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (solve == null) {
                    handler.removeCallbacks(r);
                    board.incrementSize();
                    board.board_init();
                    reDraw();
                }
            }
        });

        ((Button)findViewById(R.id.button_reset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(r);
                if (solve != null) {
                    solve.cancel(true);
                    solve = null;
                }
                board.board_init();
                reDraw();
            }
        });

        ((Button)findViewById(R.id.button_solve)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.v("SOLVE", "button_solve clicked");
                handler.post(r); // 画面更新開始
                startTime = System.currentTimeMillis();

                // バックグラウンドで処理
                solve = new SolveProblemInBackground().execute();
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        viewH = cv.getHeight();
        viewW = cv.getWidth();

        reDraw();
    }

    public void button_pushed(int row, int col) {
        board.putDot(row, col);
        reDraw();
    }

    // draw
    private void reDraw() {
        Board copy = new Board();
        copy.copyFrom(board);
        cv.drawBoard(board.getSize(), viewH, viewW, copy);
    }

}