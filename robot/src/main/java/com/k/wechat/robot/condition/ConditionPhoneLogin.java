package com.k.wechat.robot.condition;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.util.WechatUI;


public class ConditionPhoneLogin implements Condition {
    @Override
    public boolean c() {
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_PHONE_LOADING);
        AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_MORE_LOADING);
        return nodeInfosById != null && TextUtils.equals("手机号登录", nodeInfosById.getText()) && nodeInfosById1 != null && TextUtils.equals("用微信号/QQ号/邮箱登录", nodeInfosById1.getText());
    }
}
