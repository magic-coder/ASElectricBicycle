package com.wxxiaomi.ming.electricbicycle.presenter.impl;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.wxxiaomi.ming.chatwidget.bean.CoustomEmojGroupData;
import com.wxxiaomi.ming.chatwidget.bean.Emojicon;
import com.wxxiaomi.ming.chatwidget.weidgt.ChatInputMenu;
import com.wxxiaomi.ming.electricbicycle.R;
import com.wxxiaomi.ming.electricbicycle.bean.User;
import com.wxxiaomi.ming.electricbicycle.dao.impl.UserDaoImpl2;
import com.wxxiaomi.ming.electricbicycle.model.impl.EmEngine;
import com.wxxiaomi.ming.electricbicycle.presenter.base.BasePresenterImpl;
import com.wxxiaomi.ming.electricbicycle.presenter.callback.ChatPresenter;
import com.wxxiaomi.ming.electricbicycle.ui.view.ChatView;
import com.wxxiaomi.ming.electricbicycle.view.em.adapter.ChatRowItemAdapter;

import rx.functions.Action1;

/**
 * Created by 12262 on 2016/6/14.
 */
public class ChatPresenterImpl extends BasePresenterImpl<ChatView> implements ChatPresenter<ChatView> {

    private String toChatUsername;
    /**
     * 是否为添加好友之后跳转的
     */
    private boolean isAdd;
    private ChatRowItemAdapter messageAdapter;



    public void initIntentData(Intent intent) {
        // 聊天人或群id

        toChatUsername = intent.getBundleExtra("value").getString("userId");
        isAdd = intent.getExtras().getBoolean("isAdd",false);
    }

    @Override
    public void sendTextMessage(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content,
                toChatUsername);
        sendMessage(message);
    }

    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;
    protected int[] itemStrings = { R.string.send_locat };
    protected int[] itemdrawables = { R.drawable.ease_chat_takepic_selector };
    protected int[] itemIds = { ITEM_TAKE_PICTURE};

    public void initChatInputMenu(ChatInputMenu inputMenu) {
        inputMenu.init(CoustomEmojGroupData.getData(), itemStrings, itemdrawables, itemIds
                , new ChatInputMenu.ChatInputMenuListener() {
                    @Override
                    public void onSendMessage(String content) {
                        Log.i("wang","发送了:"+content);
                        sendTextMessage(content);
                    }

                    @Override
                    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                        return false;
                    }

                    @Override
                    public void onCustomItemClick(int position) {
                        Log.i("wang","点击了第"+position+"的item");
                    }

                    @Override
                    public void onBigExpressionClicked(Emojicon emojicon) {
                        Log.i("wang","点击了大表情");
                    }
                });
    }

    @Override
    public void attach(ChatView s) {
        super.attach(s);
        initIntentData(mView.getIntentData());
        initChatInputMenu(mView.getInputMenu());
        UserDaoImpl2.getInstance().getFriendInfoByEmname(toChatUsername)
                .subscribe(new Action1<User.UserCommonInfo>() {
                    @Override
                    public void call(User.UserCommonInfo userCommonInfo) {
                        messageAdapter = new ChatRowItemAdapter(mView.getContext(), toChatUsername, mView.getListView(),userCommonInfo);
                        mView.setChatRowAdapter(messageAdapter);
                        messageAdapter.refreshSelectLast();
                        if(isAdd){
                            sendTextMessage("我已经成为你的好友啦");
                        }
                    }
                });
    }

    protected void sendMessage(EMMessage message) {
        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
//        handler.sendEmptyMessage(SCROLLTOBOTTOM);
        messageAdapter.scrollToBottom();
    }

    @Override
    public void onResume() {
        EmEngine.getInstance().setFriendMsgLis(new EmEngine.FriendMessageListener() {
            @Override
            public void FriendMsgReceive() {
                messageAdapter.refreshSelectLast();
            }
        });
    }
}
