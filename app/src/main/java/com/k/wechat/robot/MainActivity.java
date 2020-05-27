package com.k.wechat.robot;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startAddFriends(View view) {
        AddGroupFriendsActivity.startSelf(this);
    }

    public void startMassSend(View view) {
        MassSendFriendsActivity.startSelf(this);
    }
}
