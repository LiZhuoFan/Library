package com.droven.swipeupdatelayout.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.droven.swipeupdatelayout.R;
import com.droven.swipeupdatelayout.base.BaseLoadingView;

public class SwipeUpdateLoadingView extends BaseLoadingView {

    private ObjectAnimator objectAnimator;
    private ImageView emptyIv;
    private TextView emptyTv;

    @Override
    protected int layoutResId() {
        return R.layout.loading_empty_view;
    }

    @Override
    protected void initView(View view) {
        emptyIv = view.findViewById(R.id.empty_iv);
        emptyTv = view.findViewById(R.id.empty_tv);
        emptyTv.setText("正在加载中");
        emptyIv.setImageResource(R.mipmap.icon_loading);
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofFloat(emptyIv, "rotation", 0, 360);
            objectAnimator.setDuration(800);
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.setRepeatMode(ValueAnimator.RESTART);
            objectAnimator.setInterpolator(new LinearInterpolator());
        }
    }

    @Override
    public void onLoading() {
        objectAnimator.start();
    }

    @Override
    public void onLoadFinished() {
        objectAnimator.cancel();
    }

    public void setValue(Drawable icon, String text) {
        if (icon != null) {
            emptyIv.setImageDrawable(icon);
        }
        if (!TextUtils.isEmpty(text)) {
            emptyTv.setText(text);
        }
    }

}