package com.wxxiaomi.ming.electricbicycle.presenter.callback;

import com.wxxiaomi.ming.electricbicycle.presenter.base.BasePresenter;

/**
 * Created by 12262 on 2016/6/15.
 */
public interface FriendAddPresenter<T> extends BasePresenter<T> {
    void onFindClick(String name);
}
