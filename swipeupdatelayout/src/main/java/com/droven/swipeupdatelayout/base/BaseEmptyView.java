package com.droven.swipeupdatelayout.base;

import android.view.View;

public abstract class BaseEmptyView extends BaseComponentView {

    /**
     * 点击重新加载的ViewId，返回0则不实现点击重新加载
     */
    public abstract View[] clickToReloadView();

    /**
     * 没数据时
     */
    public abstract void onEmpty();


    /**
     * 加载出错时
     */
    public abstract void onFail(String errMsg);

}