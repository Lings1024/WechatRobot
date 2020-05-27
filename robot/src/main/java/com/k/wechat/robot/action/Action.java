package com.k.wechat.robot.action;

import android.view.accessibility.AccessibilityEvent;

public interface Action {
    void run(AccessibilityEvent event);
}
