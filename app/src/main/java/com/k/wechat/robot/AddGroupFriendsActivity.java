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


public class AddGroupFriendsActivity extends AppCompatActivity {

    private EditText mEtMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_friends);
        mEtMessage = (EditText) findViewById(R.id.et_content);
    }

    public static void startSelf(Activity activity) {
        activity.startActivity(new Intent(activity, AddGroupFriendsActivity.class));
    }

    public void start(View view) {
        if (TextUtils.isEmpty(mEtMessage.getText())) {
            Toast.makeText(this, "请先补全字段", Toast.LENGTH_SHORT).show();
        } else {
            TaskController.startAddGroupFriendsTask(this,
                    mEtMessage.getText().toString());
        }
    }
}
