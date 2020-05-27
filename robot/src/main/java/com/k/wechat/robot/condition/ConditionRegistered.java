package com.k.wechat.robot.condition;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.util.WechatUI;


public class ConditionRegistered implements Condition {
    @Override
    public boolean c() {
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_REGISTERED_TEXT);
        AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_PHONE_REGISTERED_TEXT);
        return nodeInfosById != null && TextUtils.equals("注册", nodeInfosById.getText()) && nodeInfosById1 != null && TextUtils.equals("手机号注册", nodeInfosById1.getText());
    }
}
