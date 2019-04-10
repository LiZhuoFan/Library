package com.droven.swipeupdatelayout.view;

import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droven.swipeupdatelayout.R;
import com.droven.swipeupdatelayout.base.BaseEmptyView;


public class SwipeUpdateEmptyView extends BaseEmptyView {

    private ImageView emptyIv;
    private TextView emptyTv;
    private int emptyIcon = R.mipmap.icon_no_data;
    private String emptyStr = "暂无数据";
    private int failIcon = R.mipmap.icon_data_error;

    @Override
    protected int layoutResId() {
        return R.layout.loading_empty_view;
    }

    @Override
    protected void initView(View view) {
        emptyIv = view.findViewById(R.id.empty_iv);
        emptyTv = view.findViewById(R.id.empty_tv);
        emptyIv.setImageResource(failIcon);
    }

    @Override
    public View[] clickToReloadView() {
        return new View[]{emptyIv, emptyTv};
    }

    @Override
    public void onEmpty() {
        emptyIv.setImageResource(emptyIcon);
        emptyTv.setText(emptyStr);
    }

    @Override
    public void onFail(String errMsg) {
        emptyIv.setImageResource(failIcon);
        emptyTv.setText(errMsg);
    }

    public void setValue(@DrawableRes int emptyIcon, String emptyText, @DrawableRes int failIcon) {
        if (emptyIcon != -1) {
            this.emptyIcon = emptyIcon;
        }
        if (!TextUtils.isEmpty(emptyText)) {
            this.emptyStr = emptyText;
        }
        if (failIcon != -1) {
            this.failIcon = failIcon;
        }
    }

}