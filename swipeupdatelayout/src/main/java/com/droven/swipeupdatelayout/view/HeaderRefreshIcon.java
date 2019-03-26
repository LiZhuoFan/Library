package com.droven.swipeupdatelayout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class HeaderRefreshIcon extends View {

    private static final int PILLAR_WIDTH = 18;
    private static final int PILLAR_SPACE = 8;
    private static final int PILLAR_AMOUNT = 5;
    private static final int MAX_WIDTH = PILLAR_WIDTH * PILLAR_AMOUNT + PILLAR_SPACE * (PILLAR_AMOUNT - 1);
    private static final int MAX_HEIGHT = 100;
    private static final int MIN_HEIGHT = 20;
    private static final long DURATION = 200;
    private Random random;
    private Paint mPaint;
    private Rect rect;
    private boolean isStarted;

    public HeaderRefreshIcon(Context context) {
        this(context, null);
    }

    public HeaderRefreshIcon(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderRefreshIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        random = new Random();
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#CCCCCC"));
        rect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST || MeasureSpec.getSize(widthMeasureSpec) < MAX_WIDTH)
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(MAX_WIDTH, MeasureSpec.EXACTLY);
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST || MeasureSpec.getSize(heightMeasureSpec) < MAX_HEIGHT)
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(MAX_HEIGHT, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int offsetX = (getMeasuredWidth() - MAX_WIDTH) / 2;
        final int offsetY = (getMeasuredHeight() - MAX_HEIGHT) / 2;
        for (int i = 0; i < PILLAR_AMOUNT; i++) {
            int randomHeight = random.nextInt(MAX_HEIGHT);
            if (randomHeight < MIN_HEIGHT)
                randomHeight = MIN_HEIGHT;
            rect.left = i * (PILLAR_WIDTH + PILLAR_SPACE) + offsetX;
            rect.top = MAX_HEIGHT - randomHeight + offsetY;
            rect.right = rect.left + PILLAR_WIDTH;
            rect.bottom = rect.top + randomHeight;
            canvas.drawRect(rect, mPaint);
        }
    }

    public void startAnim() {
        isStarted = true;
        post(animRunnable);
    }

    public void stopAnim() {
        isStarted = false;
        removeCallbacks(animRunnable);
    }

    private Runnable animRunnable = new Runnable() {
        @Override
        public void run() {
            if (isStarted) {
                invalidate();
                postDelayed(animRunnable, DURATION);
            }
        }
    };

}
