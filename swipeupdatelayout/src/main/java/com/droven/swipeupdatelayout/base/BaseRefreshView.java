package com.droven.swipeupdatelayout.base;

import android.view.View;
import android.view.ViewGroup;

public abstract class BaseRefreshView extends BaseComponentView {

    /**
     * 下拉、下拉最大高度
     */
    protected int maxDragHeight() {
        return view.getMeasuredHeight();
    }

    /**
     * 可刷新、加载更多的高度
     * 用于拖拽时切换文字内容
     */
    public abstract int canRefreshHeight();


    /**
     * 拖拽中但未达到可刷新的高度
     */
    public abstract void onNormal();

    /**
     * 拖拽中且已达到可刷新的高度
     */
    public abstract void onCanRefresh();

    /**
     * 释放后，刷新中
     */
    public abstract void onRefreshing();

    /**
     * 刷新完成
     */
    public abstract void onComplete();

    /**
     * 不多拖拽过程的回调，可用于边拖拽边慢慢切换图片状态（动画）
     *
     * @param top 拖拽高度
     */
    public abstract void onDragging(int top);

    public void changeViewPosition(int top, int dy) {
        view.setTop(view.getTop() + dy);
        view.setBottom(view.getBottom() + dy);
    }

    public int clampViewPositionVertical(int top) {
        if (Math.abs(top) > canRefreshHeight()) {
            onCanRefresh();
            if (Math.abs(top) > maxDragHeight())
                top = top < 0 ? -maxDragHeight() : maxDragHeight();
        } else {
            onNormal();
        }
        onDragging(top);
        return top;
    }

    protected int getViewVerticalSpace(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) layoutParams;
            return view.getMeasuredHeight() + mlp.topMargin + mlp.bottomMargin;
        } else {
            return view.getMeasuredHeight();
        }
    }

}