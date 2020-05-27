package com.k.wechat.robot.condition;

import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.util.WechatUI;


/**
 * 群聊的聊天信息界面
 * 成员列表和群聊名称
 */
public class ConditionGroupMore implements Condition {
    @Override
    public boolean c() {
        //查看返回按钮，title，人员
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_CONVERSATION_DETAIL_PP_LIST);
        AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_CARD_SEARCH_FRIEND_BACK_PUS);
        AccessibilityNodeInfo nodeInfosById2 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_FRIEND_TIME);
        boolean b = false;
        if (nodeInfosById2 != null && nodeInfosById2.getText() != null && nodeInfosById2.getText().length() >= 4) {
            if ("聊天信息".equals(nodeInfosById2.getText().toString().substring(0, 4))) {
                b = true;
            }
        }
        String s = "";
        if (nodeInfosById1 != null && nodeInfosById1.getContentDescription() != null) {
            s = nodeInfosById1.getContentDescription().toString();

        }
        return nodeInfosById != null && b && "返回".equals(s);
    }
}
