package com.droven.swipeupdatelayout.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
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

    private static final int VIEW_MODE_PILLAR = 1;
    private static final int VIEW_MODE_ICON = 2;
    private int viewMode = VIEW_MODE_PILLAR;

    private static final int COLOR_LIGHT_GRAY = Color.parseColor("#DDDDDD");
    private static final int COLOR_DARK_GRAY = Color.parseColor("#CCCCCC");

    private Random random;
    private Paint mPaint;
    private Rect rect;
    private boolean isStarted;
    private Bitmap iconBitmap;
    private int offsetY = -1;
    private ValueAnimator valueAnimator;

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
        mPaint.setColor(COLOR_DARK_GRAY);
        rect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0, maxHeight = 0;
        switch (viewMode) {
            case VIEW_MODE_PILLAR:
                maxWidth = MAX_WIDTH;
                maxHeight = MAX_HEIGHT;
                break;
            case VIEW_MODE_ICON:
                maxWidth = iconBitmap.getWidth();
                maxHeight = iconBitmap.getHeight();
                break;
        }
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST
                || MeasureSpec.getSize(widthMeasureSpec) < maxWidth)
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(MAX_WIDTH, MeasureSpec.EXACTLY);
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST
                || MeasureSpec.getSize(heightMeasureSpec) < maxHeight)
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(MAX_HEIGHT, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (viewMode) {
            case VIEW_MODE_PILLAR:
                drawPillar(canvas);
                break;
            case VIEW_MODE_ICON:
                drawIcon(canvas);
                break;
        }

    }

    private void drawPillar(Canvas canvas) {
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
            mPaint.setColor(COLOR_DARK_GRAY);
            canvas.drawRect(rect, mPaint);
        }
    }

    private void drawIcon(Canvas canvas) {
        final int left = (getMeasuredWidth() - iconBitmap.getWidth()) / 2;
        final int top = (getMeasuredHeight() - iconBitmap.getHeight()) / 2;
        mPaint.setColor(COLOR_LIGHT_GRAY);
        canvas.drawBitmap(iconBitmap, left, top, mPaint);
        canvas.save();
        if (offsetY != -1) {
            canvas.clipRect(left, top + offsetY,
                    left + iconBitmap.getWidth(), top + iconBitmap.getHeight());
            mPaint.setColor(COLOR_DARK_GRAY);
            canvas.drawBitmap(iconBitmap, left, top, mPaint);
            canvas.restore();
        }
    }

    public void startAnim() {
        switch (viewMode) {
            case VIEW_MODE_PILLAR:
                isStarted = true;
                post(animRunnable);
                break;
            case VIEW_MODE_ICON:
                valueAnimator.start();
                break;
        }
    }

    public void stopAnim() {
        switch (viewMode) {
            case VIEW_MODE_PILLAR:
                isStarted = false;
                removeCallbacks(animRunnable);
                break;
            case VIEW_MODE_ICON:
                valueAnimator.cancel();
                offsetY = 0;
                invalidate();
                break;
        }
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

    public void initIcon(@DrawableRes int iconId) {
        iconBitmap = BitmapFactory.decodeResource(getResources(), iconId).extractAlpha();//取透明Bitmap
        viewMode = VIEW_MODE_ICON;
        valueAnimator = ValueAnimator.ofInt(iconBitmap.getHeight(), 0);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offsetY = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        invalidate();
    }

    public void dragging(int top, int maxHeight) {
        if (top >= maxHeight)
            return;
        switch (viewMode) {
            case VIEW_MODE_PILLAR:
                if (top % 10 == 0) {
                    invalidate();
                }
                break;
            case VIEW_MODE_ICON:
                offsetY = iconBitmap.getHeight() - (iconBitmap.getHeight() * top / maxHeight);
                invalidate();
                break;
        }
    }

}
