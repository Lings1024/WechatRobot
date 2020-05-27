package com.k.wechat.robot.condition;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.util.WechatUI;


public class ConditionRoomMember implements Condition {
    @Override
    public boolean c() {
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_GROUP_SEE_ALL_MEMBER_GRIDVIEW);
        AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_FRIEND_TIME);

        return nodeInfosById != null && nodeInfosById1 != null && nodeInfosById1.getText() != null && nodeInfosById1.getText().toString().length() >= 4 && TextUtils.equals("聊天成员", nodeInfosById1.getText().toString().substring(0, 4));
    }
}
