package com.k.wechat.robot.util;

import android.view.accessibility.AccessibilityEvent;


public class ObtainWindowStateChangeEvent {
    public static AccessibilityEvent obtainLuancherEvent() {
        return obtainEvent(WechatUI.UI_LUANCHER);
    }

    public static AccessibilityEvent obtainEvent(String className) {
        try {
            AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
            accessibilityEvent.setClassName(className);
            return accessibilityEvent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
