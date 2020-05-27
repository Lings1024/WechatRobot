package com.k.wechat.robot;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.action.H;
import com.k.wechat.robot.action.WechatCurrentActivity;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.util.L;
import com.k.wechat.robot.util.WeChatHelper;
import com.k.wechat.robot.util.WechatUI;


public class MainService extends AccessibilityService {


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

//        AccessibilityServiceInfo mServeiceInfo = new AccessibilityServiceInfo();
//        mServeiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
//        mServeiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
//        setServiceInfo(mServeiceInfo);

        AccessibilityHelper.mService = this;

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (LibInstance.getInstance().getTaskListener() == null) {
            return;
        }

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            L.e(AccessibilityEvent.eventTypeToString(event.getEventType()) + "; " + event.getPackageName() + "; " + event.getClassName());
            String s = event.getClassName().toString();

            if (TextUtils.equals(WechatUI.CLASS_NAME_FRAMELAYOUT, event.getClassName()) || TextUtils.equals(WechatUI.UI_WECHAT_NORMAL_LOADING, event.getClassName())) {
                //FRAMELAYOUT和LOADING类型不处理,不参与页面切换监控和action执行
                return;
            }
            //微信是UI
            //1s 内触发上次的界面 ，直接忽略 (但是得排除上次的更新是手动的)
            if (s.equals(WechatCurrentActivity.getAccessibilityWechatUi()) && System.currentTimeMillis() - WechatCurrentActivity.getInstance().getUpdateTime() < 1000 && !WechatCurrentActivity.getInstance().isManualForTheLast()) {
                return;
            }

            if (WeChatHelper.hasInt) {
                WechatCurrentActivity.getInstance().updateCurrent(s);
            }

            if (TextUtils.equals(WechatUI.CLASS_NAME_LINEARLAYOUT, event.getClassName())) {
                //CLASS_NAME_LINEARLAYOUT，参与页面监控，但不参与action执行
                return;
            }
        }
        if (TaskId.get().mTaskToggle && TaskId.get().mCurrentId > 0) {
            //任务开关打开 并且有任务id
            HandleAccessibilityEventService.startWithEvent(getApplicationContext(), event);
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }
}
