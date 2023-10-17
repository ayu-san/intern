package com.example.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ArrowView extends View {
    private float startX, startY, endX, endY;
    private Paint paint;

    public ArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFF00FF00); // 緑色
        paint.setStrokeWidth(5f);
    }

    public void setArrow(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        invalidate(); // ビューを再描画
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (startX != 0 && startY != 0 && endX != 0 && endY != 0) {
            // 矢印を描画
            canvas.drawLine(startX, startY, endX, endY, paint);
            // 矢印の先端部分を描画（ここでは単純に線分の終点）
            canvas.drawCircle(endX, endY, 8, paint);
        }
    }
}