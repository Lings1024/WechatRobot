package com.k.wechat.robot;

import android.app.Application;
import android.content.Context;

import com.k.wechat.robot.util.L;
import com.yhao.floatwindow.FloatWindow;

public class App extends Application implements LibInstance.LibInterface {


    @Override
    public void onCreate() {
        super.onCreate();

        L.e("app created");
        LibInstance.getInstance().regist(this, this);
        FloatWindowUtil.getInstance().init(this);

    }

    @Override
    public void onTerminate() {
        FloatWindowUtil.getInstance().release();
        super.onTerminate();
    }

    @Override
    public void startTask(int taskId, Runnable runnable) {
        FloatWindowUtil.getInstance().startTask(taskId, runnable);

    }

    @Override
    public void onNewTaskStarted(String taskShortDesc, int id) {
        FloatWindowUtil.getInstance().taskStarted(taskShortDesc, id);

    }


    @Override
    public void onTaskComplete(String taskCompleteMessage) {
        FloatWindowUtil.getInstance().completeTask(taskCompleteMessage);
    }

    @Override
    public void onTaskComplete(String taskCompleteMessage, String leftMenuText, Runnable nextRunnable) {
        FloatWindowUtil.getInstance().completeTask(taskCompleteMessage, leftMenuText, nextRunnable);
    }

    @Override
    public void showWindowToast(String message) {
        FloatWindowUtil.getInstance().showShortNotifyToast(message);

    }

    @Override
    public Context provideContext() {
        return this;
    }

    @Override
    public void backToMainUiReady() {
        if (!FloatWindowUtil.getInstance().getTaskStatus()) {

            FloatWindow.get().getView().performClick();
        }
    }

}
