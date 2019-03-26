package com.droven.swipeupdatelayout.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.droven.swipeupdatelayout.R;
import com.droven.swipeupdatelayout.base.BaseLoadingView;

public class SwipeUpdateLoadingView extends BaseLoadingView {

    private ObjectAnimator objectAnimator;

    @Override
    protected int layoutResId() {
        return R.layout.loading_empty_view;
    }

    @Override
    protected void initView(View view) {
        ImageView emptyIv = view.findViewById(R.id.empty_iv);
        TextView emptyTv = view.findViewById(R.id.empty_tv);
        emptyTv.setText("正在加载中");
        emptyIv.setImageResource(R.drawable.icon_loading);
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


}