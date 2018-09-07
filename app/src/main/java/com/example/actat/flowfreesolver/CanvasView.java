package com.example.actat.flowfreesolver;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View {

    private Paint paint;
    private int boardSize = 5;
    private int viewH;
    private int viewW;

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 描画処理をここに書く
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void drawBoard(int s, int h, int w) {
        boardSize = s;
        viewH = h;
        viewW = w;
    }
}
