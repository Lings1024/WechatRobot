package com.k.wechat.robot.condition;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.util.WechatUI;


public class ConditionChatting implements Condition {
    @Override
    public boolean c() {
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_CONVERSATION_MORE);
        //聊天页面标题
        AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_CHATTING_UI_TITLE);

        AccessibilityNodeInfo nodeInfosById2 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_DOWN_MORE_LINEAR_LAYOUT);

        return nodeInfosById != null && TextUtils.equals("聊天信息", nodeInfosById.getContentDescription()) && nodeInfosById1 != null && nodeInfosById2 != null;
    }
}
