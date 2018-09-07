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
    private int boardL, boardR, boardT, boardB;

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
        paint.setColor(Color.rgb(255, 0, 0));
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 描画処理をここに書く
        canvas.drawRect(boardL, boardT, boardR, boardB, paint);

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void drawBoard(int s, int h, int w) {
        boardSize = s;

        if (h > w) {
            boardL = 0;
            boardR = w;
            boardT = (h - w) / 2;
            boardB = (h + w) / 2;
        } else {
            boardT = 0;
            boardB = h;
            boardL = (w - h) / 2;
            boardR = (w + h) / 2;
        }
    }
}
