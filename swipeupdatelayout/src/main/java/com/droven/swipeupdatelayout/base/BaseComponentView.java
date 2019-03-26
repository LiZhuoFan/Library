package com.droven.swipeupdatelayout.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseComponentView {

    /**
     * layout的ID
     */
    protected abstract int layoutResId();

    /**
     * 初始化view
     */
    protected abstract void initView(View view);

    protected View view;

    public View getView() {
        return view;
    }

    public View inflateView(Context context, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(layoutResId(), parent, false);
        initView(view);
        return view;
    }

}