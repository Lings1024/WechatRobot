package com.k.wechat.robot;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;


import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.action.WechatCurrentActivity;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.util.L;
import com.k.wechat.robot.util.WeChatHelper;
import com.k.wechat.robot.util.WechatUI;

import java.util.ArrayList;
import java.util.List;

public class BackToMainUiService extends IntentService {


    private static List<String> list = new ArrayList<>();

    static {
        list.add(WechatUI.UI_WORLD);
        list.add(WechatUI.UI_LANDING);
        list.add(WechatUI.UI_PHONE_LANDING);
        list.add(WechatUI.UI_SMS_LANDING);
        list.add(WechatUI.UI_FORGET_PASSWORD);
        list.add(WechatUI.UI_REGISTERED);
        list.add(WechatUI.UI_WECHAT_REST);
        list.add(WechatUI.UI_LOGIN);
    }

    public BackToMainUiService() {
        super("BackToMainUiService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, BackToMainUiService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (WeChatHelper.hasInstallWechat() && !TaskId.get().mTaskToggle) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                while (WeChatHelper.hasInt && !WechatCurrentActivity.isCurrentActivity(WechatUI.UI_LUANCHER)) {
                    L.e("不是主界面 ... ");
                    if (!concludeFilter()) {
                        if (AccessibilityHelper.performBack()) {
                        } else {
                            AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_GROUP_OVER_RETURN);
                            if (nodeInfosById1 != null) {
                                AccessibilityHelper.performClick(nodeInfosById1);
                            } else {
                                AccessibilityNodeInfo nodeInfosById2 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_CARD_SEARCH_FRIEND_BACK_PUS);
                                AccessibilityHelper.performClick(nodeInfosById2);
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                L.e("ready...");
                TaskId.get().mTaskToggle = true;
                //模拟 点击开始按钮
                new Handler(Looper.getMainLooper()).postDelayed(() -> LibInstance.getInstance().getTaskListener().backToMainUiReady(), 1000);
            } catch (Exception e) {
                L.e(Log.getStackTraceString(e));
            }
        }
    }

    public static boolean concludeFilter() {
        String currentActivity = WechatCurrentActivity.getInstance().getCurrentActivity();
        if (currentActivity == null) {
            return true;
        }
        for (String s : list) {

            if (TextUtils.equals(currentActivity ,s)) {
                return true;
            }
        }
        return false;
    }
}
