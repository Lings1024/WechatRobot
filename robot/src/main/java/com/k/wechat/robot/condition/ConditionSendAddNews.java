package com.k.wechat.robot.condition;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.LibInstance;
import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.util.WeChatHelper;
import com.k.wechat.robot.util.WechatUI;


/**
 * 添加朋友填写申请界面
 */
public class ConditionSendAddNews implements Condition {
    @Override
    public boolean c() {

        if (WeChatHelper.above7012(LibInstance.getInstance().getTaskListener().provideContext())) {
            AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ADD_FRIEND_TEXT);
            //设置备注的编辑框
            AccessibilityNodeInfo nodeNameEditText = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ADD_FIREND_VERIFY_REMARK);
            String s = "";
            if (nodeInfosById != null && nodeInfosById.getText() != null) {
                s = nodeInfosById.getText().toString();
            }
            return ("申请添加朋友".equals(s) || "通过朋友验证".equals(s)) && nodeNameEditText != null;
        } else {
            AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_FRIEND_TIME);
            return nodeInfosById1 != null && (TextUtils.equals("验证申请", nodeInfosById1.getText()) || TextUtils.equals("朋友验证", nodeInfosById1.getText()));
        }

    }
}
