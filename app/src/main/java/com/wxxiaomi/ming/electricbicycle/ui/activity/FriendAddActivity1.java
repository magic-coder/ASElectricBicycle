package com.wxxiaomi.ming.electricbicycle.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wxxiaomi.ming.electricbicycle.R;
import com.wxxiaomi.ming.electricbicycle.presenter.callback.FriendAddPresenter;
import com.wxxiaomi.ming.electricbicycle.presenter.impl.FriendAddPresenterImpl;
import com.wxxiaomi.ming.electricbicycle.ui.base.BaseMvpActivity;
import com.wxxiaomi.ming.electricbicycle.ui.view.FriendAddView;
import com.wxxiaomi.ming.electricbicycle.view.adapter.NearFriendRecommendAdapter1;

/**
 * Created by 12262 on 2016/6/15.
 */
public class FriendAddActivity1 extends BaseMvpActivity<FriendAddView,FriendAddPresenter<FriendAddView>> implements FriendAddView {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Button btn_ok;
    private EditText et_username;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_friend_add);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView)findViewById(R.id.list);
        btn_ok = (Button)findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        et_username = (EditText) findViewById(R.id.et_username);
        // 标题的文字需在setSupportActionBar之前，不然会无效
        toolbar.setTitle("添加好友");
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); // 设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected FriendAddPresenter<FriendAddView> initPre() {
        return new FriendAddPresenterImpl();
    }

    @Override
    public void setListAdaper(NearFriendRecommendAdapter1 adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                presenter.onFindClick(et_username.getText().toString().trim());
        }
    }
}
