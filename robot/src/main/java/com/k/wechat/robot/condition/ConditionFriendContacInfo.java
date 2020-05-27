package com.k.wechat.robot.condition;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.util.WechatUI;


/**
 * 个人信息界面
 */
public class ConditionFriendContacInfo implements Condition {
    @Override
    public boolean c() {
        //昵称
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_CONTACT_INFO_NAME);
        //添加到通讯录
        AccessibilityNodeInfo nodeAdd = AccessibilityHelper.findNodeInfosById(WechatUI.ID_PP_DETAIL_ADD2CONTACTS);
        //不在关注按钮
        AccessibilityNodeInfo nodeInfosById3 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_PUBLIC_ACCOUNTS_NO_ATTENTION);
        //好友详情的判断
        boolean b1 = (nodeInfosById != null && nodeAdd != null && (TextUtils.equals("发消息", nodeAdd.getText()) || TextUtils.equals("添加到通讯录", nodeAdd.getText()) || TextUtils.equals("音视频通话", nodeAdd.getText()) || TextUtils.equals("打招呼", nodeAdd.getText()))) || (nodeInfosById3 != null && TextUtils.equals("不再关注", nodeInfosById3.getText()));
        if (!b1) {
            //群发助手详情的界面
            AccessibilityNodeInfo nodeInfosByText = AccessibilityHelper.findNodeInfosByText("我能把你的通知、祝福等消息，分别发送给各个收件人");
            AccessibilityNodeInfo nodeInfosByText1 = AccessibilityHelper.findNodeInfosByText("群发助手");
            return nodeInfosByText != null && nodeInfosByText1 != null;
        }
        return true;
    }
}
