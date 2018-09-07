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

    private Paint paint;
    private int boardSize = 5;
    private float boardL, boardR, boardT, boardB;
    private int boardLineWidth = 3;
    private float frameInterval = 0;

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

    private void init_CanvasView() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setStrokeWidth(boardLineWidth);
        paint.setStyle(Paint.Style.STROKE);
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

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void drawBoard(int s, int h, int w) {
        boardSize = s;

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
