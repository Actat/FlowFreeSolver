package com.example.actat.flowfreesolver;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
//    int board[][];

    CanvasView cv;
    int viewW = 0;
    int viewH = 0;

    Board brd;

    long startTime, finishTime;

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
        brd.board_init();
        reDraw();

        ((Button)findViewById(R.id.button_minus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                handler.removeCallbacks(r);
                brd.decrementSizse();
                brd.board_init();
                reDraw();
            }
        });

        ((Button)findViewById(R.id.button_plus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(r);
                brd.incrementSize();
                brd.board_init();
                reDraw();
            }
        });

        ((Button)findViewById(R.id.button_reset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clickされた時の処理
                handler.removeCallbacks(r);
                brd.board_init();
                reDraw();
            }
        });

        ((Button)findViewById(R.id.button_solve)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clickされた時の処理
                // Log.v("SOLVE", "button_solve clicked");
                handler.post(r);
                startTime = System.currentTimeMillis();
                AsyncTask<Object, Integer, Boolean> task = new AsyncTask<Object, Integer, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Object... objects) {
                        return brd.solveProblem();
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        // Log.v("LOGIC", "solve finished." + "\t result: " + String.valueOf(result));
                        finishTime = System.currentTimeMillis();
                        handler.removeCallbacks(r);
                        if (result) {
                            Toast.makeText(getApplicationContext(), "solved in " + String.valueOf(finishTime - startTime) + " ms", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "no solution found", Toast.LENGTH_LONG).show();
                        }
                        reDraw();
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

        reDraw();
    }




    public void button_pushed(int row, int col) {
       brd.putDot(row, col);
        reDraw();
    }

    // draw
    private void reDraw() {
        int[][] copy = new int[brd.getSize()][brd.getSize()];
        for (int i = 0; i < brd.getSize(); i++) {
            for (int j = 0; j < brd.getSize(); j++) {
                copy[i][j] = brd.getBoard(i, j);
            }
        }
        cv.drawBoard(brd.getSize(), viewH, viewW, copy);
    }

}
