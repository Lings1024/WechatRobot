package com.k.wechat.robot.condition;

import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.util.WechatUI;


public class ConditionMain implements Condition {
    @Override
    public boolean c() {

        int count = 0;
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_HOME_MAIN_BOTTOM);
        if (nodeInfosById != null) {
            for (int i = 0; i < nodeInfosById.getChildCount(); i++) {
                AccessibilityNodeInfo child = nodeInfosById.getChild(i);
                AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(child, WechatUI.ID_HOME_MAIN_BOTTOM_TEXT);
                if (nodeInfosById1 != null && nodeInfosById1.getText() != null) {
                    String s = nodeInfosById1.getText().toString();
                    if ("微信".equals(s)) {
                        count++;
                    } else if ("通讯录".equals(s)) {
                        count++;
                    } else if ("发现".equals(s)) {
                        count++;

                    } else if ("我".equals(s)) {
                        count++;
                    }
                }
            }
            return count == 4;
        }
        return false;
    }
}
