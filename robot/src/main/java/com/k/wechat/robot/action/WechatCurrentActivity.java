package com.k.wechat.robot.action;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.LibInstance;
import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.annotation.UI_CONDITION;
import com.k.wechat.robot.annotation.UI_CONDITION_NONE;
import com.k.wechat.robot.annotation.UI_TITLE;
import com.k.wechat.robot.condition.Condition;
import com.k.wechat.robot.util.L;
import com.k.wechat.robot.util.WeChatHelper;
import com.k.wechat.robot.util.WechatUI;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class WechatCurrentActivity {

    private static final WechatCurrentActivity ourInstance = new WechatCurrentActivity();
    private long mUpdateTIme;
    private boolean manualUpdate;//更新是否是手动更新
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private String mCurrentClassName;

    //通过title一个条件就能判断的页面<className ,title>
    private HashMap<String, String> mTitleUi = new HashMap<>();

    //通过condition一个条件就能判断的页面<className ,condition>
    private HashMap<String, Condition> mConditionUi = new HashMap<>();

    //没有特征的页面或者dialog
    private HashSet<String> mConditionNoneUi = new HashSet<>();


    public static WechatCurrentActivity getInstance() {
        return ourInstance;
    }

    private WechatCurrentActivity() {

    }

    public void initWechatUi() {
        Field[] fields = WechatUI.class.getDeclaredFields();
        for (Field field : fields) {
            analysisAnnatation(field);
        }
    }

    private void analysisAnnatation(Field field) {
        try {
            UI_TITLE titleAnnotation = field.getAnnotation(UI_TITLE.class);
            if (titleAnnotation != null) {
                String titleValue = titleAnnotation.value();
                if (!TextUtils.isEmpty(titleValue)) {
                    mTitleUi.put((String) field.get(WechatUI.class), titleValue);
                }
            } else {
                UI_CONDITION conditionAnnotation = field.getAnnotation(UI_CONDITION.class);
                if (conditionAnnotation != null) {
                    mConditionUi.put((String) field.get(WechatUI.class), conditionAnnotation.value().newInstance());
                } else {
                    UI_CONDITION_NONE conditionNoneAnnotation = field.getAnnotation(UI_CONDITION_NONE.class);
                    if (conditionNoneAnnotation != null) {
                        mConditionNoneUi.add((String) field.get(WechatUI.class));
                    }
                }
            }
        } catch (Exception e) {
            L.e(Log.getStackTraceString(e));
        }
    }

    public long getUpdateTime() {
        return mUpdateTIme;
    }

    //上次更新是否是手动的
    public boolean isManualForTheLast() {
        return manualUpdate;
    }

    public void updateCurrent(String className) {
        manualUpdate = false;
        mUpdateTIme = System.currentTimeMillis();
        mCurrentClassName = className;
    }

    public void updateCurrentManually(String className) {
        //准备舍弃这个方法
        mHandler.post(() -> {
            updateCurrent(className);
            manualUpdate = true;
        });
    }

    private HashMap<String, String> getTitleUiMap() {
        return mTitleUi;
    }

    private HashMap<String, Condition> getConditionUiMap() {
        return mConditionUi;
    }

    private HashSet<String> getConditionNoneUiSet() {
        return mConditionNoneUi;
    }

    public String getCurrentActivity() {
//        优先从Accessibility触发的界面去判断，不行的话再去全局搜索
        String accessibilityWechatUi = getAccessibilityWechatUi();
        if (isCurrentActivity(accessibilityWechatUi)) {
            return accessibilityWechatUi;
        }
        //不再采用系统触发的页面（不准确），而是根据特征获取当前页面
        AccessibilityNodeInfo nodeTitle = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ACTIVITY_TITLE_BAR_TITLE);
        if (nodeTitle != null) {
            for (Map.Entry<String, String> titleEntry : mTitleUi.entrySet()) {
                if (TextUtils.equals(titleEntry.getValue(), nodeTitle.getText())) {
                    return titleEntry.getKey();
                }
            }
        }
        //通过标题没有筛选出来，开始从condition筛选
        for (Map.Entry<String, Condition> conditionEntry : mConditionUi.entrySet()) {
            if (conditionEntry.getValue().c()) {
                return conditionEntry.getKey();
            }
        }

        return null;
    }

    public static boolean isCurrentActivity(String className) {
        if (!WeChatHelper.hasInt) {
            return false;
        }
        if (WechatCurrentActivity.getInstance().getConditionNoneUiSet().contains(className)) {
            boolean equals = TextUtils.equals(className, getAccessibilityWechatUi());
            L.e("是否是当前页面[equal]：" + equals + " ," + className);
            return equals;
        }
        String title = WechatCurrentActivity.getInstance().getTitleUiMap().get(className);
        if (!TextUtils.isEmpty(title)) {
            AccessibilityNodeInfo nodeTitle = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ACTIVITY_TITLE_BAR_TITLE);
            boolean b = nodeTitle != null && TextUtils.equals(title, nodeTitle.getText());
            L.e("是否是当前页面[title]：" + b + " ," + className);
            return b;
        }
        Condition condition = WechatCurrentActivity.getInstance().getConditionUiMap().get(className);
        boolean b = condition != null && condition.c();
        L.e("是否是当前页面[condi]：" + b + " ," + className);
        return b;
    }

    /**
     * 获取由Accessibility触发的当前页面
     *
     * @return
     */
    public static String getAccessibilityWechatUi() {
        return WechatCurrentActivity.getInstance().mCurrentClassName;
    }

    public static boolean isWechatActivity() {
        return !TextUtils.isEmpty(getAccessibilityWechatUi()) && getAccessibilityWechatUi().startsWith(WechatUI.WECHAT_PACKAGE_NAME);
    }

}
