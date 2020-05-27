package com.k.wechat.robot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.action.WechatCurrentActivity;
import com.k.wechat.robot.task.TaskController;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.util.L;
import com.k.wechat.robot.util.WeChatHelper;

import java.util.Arrays;
import java.util.List;


public class LibInstance {
    private LibInterface libInterface;
    private static final LibInstance ourInstance = new LibInstance();

    public static LibInstance getInstance() {
        return ourInstance;
    }

    private LibInstance() {
    }

    public void regist(Context context, LibInterface libInterface) {
        this.libInterface = libInterface;
        WeChatHelper.initByWechatVersionInApp(context);
        WechatCurrentActivity.getInstance().initWechatUi();
    }

    public LibInterface getTaskListener() {
        return libInterface;
    }

    public interface LibInterface {

        void startTask(int taskId, Runnable runnable);

        void onNewTaskStarted(String taskShortName, int id);

        void onTaskComplete(String taskCompleteMessage);

        void onTaskComplete(String taskCompleteMessage, String leftMenuText, Runnable nextRunnable);

        void showWindowToast(String message);

        Context provideContext();

        void backToMainUiReady();

    }

    public boolean run(Activity activity, Runnable runnable) {
        return run(true, activity, -1, runnable);
    }

    public boolean run(Activity activity, int taskId, Runnable runnable) {
        return run(true, activity, taskId, runnable);
    }

    public boolean run(boolean checkIdConfig, Activity activity, int taskId, Runnable runnable) {
        //判断微信安装？
        if (!WeChatHelper.hasInstallWechat()) {
            L.e("微信未安装！");
            return false;
        }
        if (activity == null || LibInstance.getInstance().checkAccesibilityAndIdConfig(checkIdConfig, activity)) {
            if (libInterface != null) {
                libInterface.startTask(taskId, runnable);
            }
            WeChatHelper.openWechat(LibInstance.getInstance().getTaskListener().provideContext());
            if (!showStartControl(taskId)) {
                TaskId.get().mTaskToggle = false;
                BackToMainUiService.start(LibInstance.getInstance().getTaskListener().provideContext());
            }
            return true;
        }
        return false;
    }

    //显示开始按钮，让用户自己点击的任务列表
    private static final List<Integer> showStartControlTaskList = Arrays.asList();

    public static boolean showStartControl(int taskId) {
        return showStartControlTaskList.contains(taskId);
    }

    public boolean checkAccesibilityAndIdConfig(boolean checkIdConfig, Activity activity) {
        if (!Settings.canDrawOverlays(activity)) {
            new AlertDialog.Builder(activity).setTitle("请求悬浮窗权限").setMessage("开启悬浮窗权限后，robot的功能按钮才能在微信的界面上显示，如果不开启将无法使用robot的所有功能。").setPositiveButton("前往开启", (dialogInterface, i) -> {
                if ("Meizu".equals(Build.MANUFACTURER)) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivity(intent);
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivity(intent);
                }
            }).show();
            return false;
        } else if (!AccessibilityHelper.isServiceRunning(AccessibilityHelper.mService)) {
            new AlertDialog.Builder(activity).setTitle("开启辅助服务").setMessage("到【设置>辅助功能>无障碍>robot辅助服务】去开启辅助功能服务。\n如果已开启，请重启手机重试。").setPositiveButton("去开启", (dialogInterface, i) -> AccessibilityHelper.openAccessibilityServiceSettings(activity)).show();
            return false;
        } else if (checkIdConfig && !WeChatHelper.hasInt) {
            //没有配置文件
            new AlertDialog.Builder(activity).setTitle("校对微信版本").setMessage("请前往微信主界面，然后返回即可").setPositiveButton("前往", (dialogInterface, i) -> WeChatHelper.openWechat(activity)).show();
            return false;
        }
        return true;
    }
}
