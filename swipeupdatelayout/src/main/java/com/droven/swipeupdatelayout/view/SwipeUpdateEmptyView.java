package com.droven.swipeupdatelayout.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droven.swipeupdatelayout.R;
import com.droven.swipeupdatelayout.base.BaseEmptyView;


public class SwipeUpdateEmptyView extends BaseEmptyView {

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
        emptyIv.setImageResource(R.mipmap.icon_data_error);
    }

    @Override
    public View[] clickToReloadView() {
        return new View[]{emptyIv, emptyTv};
    }

    @Override
    public void onEmpty() {
        emptyIv.setImageResource(R.mipmap.icon_no_data);
        emptyTv.setText("暂无数据");
    }

    @Override
    public void onFail(String errMsg) {
        emptyIv.setImageResource(R.mipmap.icon_data_error);
        emptyTv.setText(errMsg);
    }

}