package com.example.assignment2;import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDrawingArea extends View {

    public MyDrawingArea(Context context) {
        super(context);
    }

    public MyDrawingArea(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDrawingArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyDrawingArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    Path path = new Path();

    Bitmap bmp;

    private List<Ball> balls = new ArrayList<>();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Fill background with white
        canvas.drawColor(Color.WHITE);

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5f);

        canvas.drawPath(path, p);

        Paint ballPaint = new Paint();
        ballPaint.setColor(Color.RED);

        for (Ball b : balls) {
            b.update();
            canvas.drawCircle(b.x, b.y, b.radius, ballPaint);
        }

        if (!balls.isEmpty()) {
            invalidate();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            path.moveTo(x, y); //path is global. Same thing that onDraw uses.//
        } else if (action == MotionEvent.ACTION_MOVE) {
            path.lineTo(x, y);
        }

        invalidate();
        return true;
    }

    public Bitmap getBitmap() {
        bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        // Fill background with white
        c.drawColor(Color.WHITE);
        Paint bitPaint = new Paint();

        bitPaint.setColor(Color.BLACK);
        bitPaint.setStyle(Paint.Style.STROKE);
        bitPaint.setAntiAlias(true);
        bitPaint.setStrokeWidth(5f);

        c.drawPath(path, bitPaint);
        return bmp;
    }

    public void putBallsOnPath() {
        PathMeasure pm = new PathMeasure(path, false);

        do {
            float length = pm.getLength();
            if (length > 0) {
                float distance = 0f;
                float step = 23f;
                float[] pos = new float[2];

                while (distance < length) {
                    if (pm.getPosTan(distance,pos,null)) {
                        balls.add(new Ball(pos[0],pos[1]));
                    }
                    distance += step;
                }
            }
        } while (pm.nextContour());

        invalidate();
    }

    public void clearDrawing() {
        path.reset();
        invalidate();
    }
}

