package com.example.actat.flowfreesolver;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View {

    private static final int MAX_CLIC_DURATION = 200;
    private long startClickTime;

    private MainActivity mainActivity;
    private Paint paint;
    private int boardSize = 5;
    private float boardL, boardR, boardT, boardB;
    private int boardLineWidth = 3;
    private float frameInterval = 0;
    private Board boardCopy = new Board();
    private int[] color;

    public CanvasView(Context context) {
        super(context);
        init_CanvasView();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init_CanvasView();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init_CanvasView();
    }

    public void setMainActivity(MainActivity ma) {
        mainActivity = ma;
    }

    private void init_CanvasView() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setStrokeWidth(boardLineWidth);
        paint.setStyle(Paint.Style.STROKE);

        color = new int[16];
        color[0] =  getResources().getColor(R.color.colorRed);
        color[1] =  getResources().getColor(R.color.colorGreen);
        color[2] =  getResources().getColor(R.color.colorBlue);
        color[3] =  getResources().getColor(R.color.colorYellow);
        color[4] =  getResources().getColor(R.color.colorOrange);
        color[5] =  getResources().getColor(R.color.colorCyan);
        color[6] =  getResources().getColor(R.color.colorMagenta);
        color[7] =  getResources().getColor(R.color.colorMaroon);
        color[8] =  getResources().getColor(R.color.colorPurple);
        color[9] =  getResources().getColor(R.color.colorWhite);
        color[10] = getResources().getColor(R.color.colorGray);
        color[11] = getResources().getColor(R.color.colorBrightgreen);
        color[12] = getResources().getColor(R.color.colorDarkblue);
        color[13] = getResources().getColor(R.color.colorPink);
        color[14] = getResources().getColor(R.color.colorDarkcyan);
        color[15] = getResources().getColor(R.color.colorTan);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 描画処理をここに書く
        // 枠線
        paint.setStrokeWidth(boardLineWidth);
        paint.setColor(getResources().getColor(R.color.frame));
        for (int i = 0; i < boardSize + 1; i++) {
            canvas.drawLine(boardL + i * frameInterval, boardT - boardLineWidth / 2.0f, boardL + i * frameInterval, boardB + boardLineWidth / 2.0f, paint);
            canvas.drawLine(boardL - boardLineWidth / 2.0f, boardT + i * frameInterval, boardR + boardLineWidth / 2.0f,  boardT + i * frameInterval, paint);
        }

        // 枠内
        for (int i = 0; i < boardSize * boardSize; i++) {
            if (boardCopy.getColor(i / boardSize,i % boardSize) % 100 >= 0 && boardCopy.getColor(i / boardSize,i % boardSize) % 100 < 16) {
                if (boardCopy.getColor(i / boardSize,i % boardSize) >= 0 && boardCopy.getColor(i / boardSize,i % boardSize) < 16) {
                    // line
                    paint.setColor(color[boardCopy.getColor(i / boardSize,i % boardSize) % 100]);
                    canvas.drawCircle(boardL + frameInterval * ((i % boardSize) + 0.5f), boardT + frameInterval * ((i / boardSize) + 0.5f), frameInterval * 0.15f, paint);
                }
                if (boardCopy.getColor(i / boardSize,i % boardSize) >= 100 && boardCopy.getColor(i / boardSize,i % boardSize) < 116) {
                    // circle
                    paint.setColor(color[boardCopy.getColor(i / boardSize,i % boardSize) % 100]);
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    canvas.drawCircle(boardL + frameInterval * ((i % boardSize) + 0.5f), boardT + frameInterval * ((i / boardSize) + 0.5f), frameInterval * 0.35f, paint);
                }
                // 接続
                if (i / boardSize > 0 && boardCopy.getColor(i / boardSize- 1, i % boardSize) % 100 == boardCopy.getColor(i / boardSize,i % boardSize) % 100) {
                    // up
                    paint.setColor(color[boardCopy.getColor(i / boardSize,i % boardSize) % 100]);
                    canvas.drawRect(boardL + frameInterval * ((i % boardSize) + 0.5f - 0.15f), boardT + frameInterval * (i / boardSize), boardL + frameInterval * ((i % boardSize) + 0.5f + 0.15f), boardT + frameInterval * ((i / boardSize) + 0.5f), paint);
                }
                if (i % boardSize > 0 && boardCopy.getColor(i / boardSize,i % boardSize - 1) % 100 == boardCopy.getColor(i / boardSize,i % boardSize) % 100) {
                    // left
                    paint.setColor(color[boardCopy.getColor(i / boardSize,i % boardSize) % 100]);
                    canvas.drawRect(boardL + frameInterval * (i % boardSize), boardT + frameInterval * ((i / boardSize) + 0.5f - 0.15f), boardL + frameInterval * ((i % boardSize) + 0.5f), boardT + frameInterval * ((i / boardSize) + 0.5f + 0.15f), paint);
                }
                if (i / boardSize < boardSize - 1 && boardCopy.getColor(i / boardSize+ 1, i % boardSize) % 100 == boardCopy.getColor(i / boardSize,i % boardSize) % 100) {
                    // down
                    paint.setColor(color[boardCopy.getColor(i / boardSize,i % boardSize) % 100]);
                    canvas.drawRect(boardL + frameInterval * ((i % boardSize) + 0.5f - 0.15f), boardT + frameInterval * ((i / boardSize) + 0.5f), boardL + frameInterval * ((i % boardSize) + 0.5f + 0.15f), boardT + frameInterval * ((i / boardSize) + 1), paint);
                }
                if (i % boardSize < boardSize - 1 && boardCopy.getColor(i / boardSize,i % boardSize + 1) % 100 == boardCopy.getColor(i / boardSize,i % boardSize) % 100) {
                    // right
                    paint.setColor(color[boardCopy.getColor(i / boardSize,i % boardSize) % 100]);
                    canvas.drawRect(boardL + frameInterval * ((i % boardSize) + 0.5f), boardT + frameInterval * ((i / boardSize) + 0.5f - 0.15f), boardL + frameInterval * ((i % boardSize) + 1), boardT + frameInterval * ((i / boardSize) + 0.5f + 0.15f), paint);
                }
            }
            // connection
            /*
            paint.setColor(getColorOfConnection(boardCopy.getConnection(i / boardSize, i % boardSize, 0)));
            canvas.drawLine(boardL + frameInterval * (i % boardSize) + boardLineWidth, boardT + frameInterval * (i / boardSize) + boardLineWidth, boardL + frameInterval * (i % boardSize + 1) - boardLineWidth, boardT + frameInterval * (i / boardSize) + boardLineWidth, paint);
            paint.setColor(getColorOfConnection(boardCopy.getConnection(i / boardSize, i % boardSize, 1)));
            canvas.drawLine(boardL + frameInterval * (i % boardSize + 1) - boardLineWidth, boardT + frameInterval * (i / boardSize) + boardLineWidth, boardL + frameInterval * (i % boardSize + 1) - boardLineWidth, boardT + frameInterval * (i / boardSize + 1) - boardLineWidth, paint);
            paint.setColor(getColorOfConnection(boardCopy.getConnection(i / boardSize, i % boardSize, 2)));
            canvas.drawLine(boardL + frameInterval * (i % boardSize) + boardLineWidth, boardT + frameInterval * (i / boardSize + 1) - boardLineWidth, boardL + frameInterval * (i % boardSize + 1) - boardLineWidth, boardT + frameInterval * (i / boardSize + 1) - boardLineWidth, paint);
            paint.setColor(getColorOfConnection(boardCopy.getConnection(i / boardSize, i % boardSize, 3)));
            canvas.drawLine(boardL + frameInterval * (i % boardSize) + boardLineWidth, boardT + frameInterval * (i / boardSize) + boardLineWidth, boardL + frameInterval * (i % boardSize) + boardLineWidth, boardT + frameInterval * (i / boardSize + 1) - boardLineWidth, paint);
            */
        }

        invalidate();
    }

    private int getColorOfConnection(int connection) {
        if (connection == 0) {
            return getResources().getColor(R.color.connectionUnsettled);
        } else if (connection == 1) {
            return getResources().getColor(R.color.connectionExist);
        } else {
            return getResources().getColor(R.color.connectionImpossible);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startClickTime = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_UP:
                long clickDuration = System.currentTimeMillis() - startClickTime;
                if (clickDuration < MAX_CLIC_DURATION) {
                    // clicked
                    float x = event.getX();
                    float y = event.getY();

                    for (int i = 0; i < boardSize; i++) {
                        if (x > boardL + frameInterval * i && x < boardL + frameInterval * (i + 1)) {
                            for (int j = 0; j < boardSize; j++) {
                                if (y > boardT + frameInterval * j && y < boardT + frameInterval * (j + 1)) {
                                    // j行i列が押された
                                    mainActivity.button_pushed(j, i);
                                    // Log.v("tag", "x: " + String.valueOf(x) + " y: " + String.valueOf(y) + " j: " + String.valueOf(j) + " i: " + String.valueOf(i));
                                }
                            }
                        }
                    }
                }
        }
        return true;
    }

    public void drawBoard(int s, int h, int w, Board copy) {
        boardSize = s;
        boardCopy = copy;

        if (h > w) {
            boardL = 0 + boardLineWidth / 2.0f;
            boardR = w - boardLineWidth / 2.0f;
            boardT = (h - w + boardLineWidth) / 2.0f;
            boardB = (h + w - boardLineWidth) / 2.0f;
        } else {
            boardT = 0 + boardLineWidth / 2.0f;
            boardB = h - boardLineWidth / 2.0f;
            boardL = (w - h + boardLineWidth) / 2.0f;
            boardR = (w + h - boardLineWidth) / 2.0f;
        }
        frameInterval = (boardB - boardT) / boardSize;
    }
}
