package com.k.wechat.robot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.k.wechat.robot.task.TaskController;


public class MassSendFriendsActivity extends AppCompatActivity {

    private EditText mEtMessage;
    private EditText mEtLable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass_send_friends);
        mEtMessage = (EditText) findViewById(R.id.et_content);
        mEtLable = (EditText) findViewById(R.id.et_lable);
    }

    public static void startSelf(Activity activity) {
        activity.startActivity(new Intent(activity, MassSendFriendsActivity.class));
    }

    public void start(View view) {
        if (TextUtils.isEmpty(mEtMessage.getText()) || TextUtils.isEmpty(mEtLable.getText())) {
            Toast.makeText(this, "请先补全字段", Toast.LENGTH_SHORT).show();
        } else {
            TaskController.startSeniorMassSendFriend(this,
                    mEtMessage.getText().toString(),
                    mEtLable.getText().toString());
        }
    }
}
