package com.k.wechat.robot.task;

import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;

import com.k.wechat.robot.DeamonService;
import com.k.wechat.robot.HandleAccessibilityEventService;
import com.k.wechat.robot.LibInstance;
import com.k.wechat.robot.action.H;
import com.k.wechat.robot.annotation.TASK_NAME;
import com.k.wechat.robot.annotation.TASK_SHORT_NAME;
import com.k.wechat.robot.task.data.AddGroupFriendMem;
import com.k.wechat.robot.task.data.SeniorGroupSendMessageMem;
import com.k.wechat.robot.util.L;

import java.lang.reflect.Field;


public class TaskId {
    /**
     * 开启任务是否从悬浮框开始
     */
    public boolean mStartFromWindow = false;
    /**
     * 开启任务总开关 是否准备好了（返回到了微信主界面）
     */
    public boolean mTaskToggle = false;

    /**
     * 服务器营销任务id
     */
    public String mTaskKey;

    /**
     * 当前任务总开关
     */
    public int mCurrentId = -1;

    /**
     * 添加群组好友任务
     */
    @TASK_NAME("群内加人")
    @TASK_SHORT_NAME("加人")
    public static final int TASK_ADD_GROUP_FRIEND = 1;

    /**
     * 发图文到好友
     */
    @TASK_NAME("发图文到好友")
    @TASK_SHORT_NAME("群发")
    public static final int TASK_SENIOR_MASS_SEND_FRIENDS = 6;

    private static final TaskId ourInstance = new TaskId();

    public static TaskId get() {
        return ourInstance;
    }

    private TaskId() {

    }

    private SparseArray<String> mTaskNames = new SparseArray<>();
    private SparseArray<String> mTaskFieldNames = new SparseArray<>();


    /**
     * 判断是否是当前任务
     *
     * @param taskId
     * @return
     */
    public static boolean assertTaskId(int taskId) {
        return taskId == TaskId.get().mCurrentId;
    }

    /**
     * 开始任务
     *
     * @param id
     * @return
     */
    public static void start(int id) {

        try {
            String taskShortName = TaskId.get().mTaskNames.get(id);
            if (TextUtils.isEmpty(taskShortName) && TaskId.get().mTaskNames.size() == 0) {
                for (Field field : TaskId.class.getDeclaredFields()) {
                    if (field.getType().equals(int.class)) {
                        TASK_NAME annotation = field.getAnnotation(TASK_NAME.class);
                        if (annotation != null && !TextUtils.isEmpty(annotation.value())) {
                            TaskId.get().mTaskNames.put((int) field.get(TaskId.get()), annotation.value());
                        }
                        TaskId.get().mTaskFieldNames.put((int) field.get(TaskId.get()), field.getName());
                    }
                }
                taskShortName = TaskId.get().mTaskNames.get(id);
            }
            if (LibInstance.getInstance().getTaskListener() != null) {
                LibInstance.getInstance().getTaskListener().onNewTaskStarted(taskShortName, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TaskId.get().mCurrentId = id;
        DeamonService.startWithTaskId(LibInstance.getInstance().getTaskListener().provideContext(), id);

    }


    /**
     * 只开始任务 不回调 window
     *
     * @param id
     * @return
     */
    public static void onlyStartTask(int id) {
        TaskId.get().mCurrentId = id;
    }

    /**
     * 结束任务
     *
     * @param id
     * @return
     */
    public static void stop(int id) {
        if (TaskId.get().mCurrentId == -1 || TaskId.get().mCurrentId != id) {
            return;
        }
        L.e("stop task, id : " + id);
        String message = "任务完成";
        stop(id, message);
        stopNotVipTryOn(id);
    }

    /**
     * 停止非会员体验
     */
    public static void stopNotVipTryOn(int id) {
    }

    /**
     * 结束任务
     *
     * @return
     */
    public static void stop() {
        stop(TaskId.get().mCurrentId);
    }

    /**
     * 结束任务
     *
     * @return
     */
    public static void stop(String message) {
        if (TaskId.get().mCurrentId != -1) {
            stop(TaskId.get().mCurrentId, message);
        }
    }

    /**
     * 结束任务
     *
     * @param id
     * @return
     */
    public static void stop(int id, String message) {
        if (TaskId.get().mCurrentId == id) {
            if (LibInstance.getInstance().getTaskListener() != null) {
                LibInstance.getInstance().getTaskListener().onTaskComplete(message);
            }
            TaskId.get().mCurrentId = -1;
            TaskId.get().mTaskToggle = false;
            TaskId.get().mTaskKey = null;

            //release H.actions
            H.getInstance().releaseActions();

            //stop HandleAccessibilityEventService
            LibInstance.getInstance().getTaskListener().provideContext().stopService(new Intent(LibInstance.getInstance().getTaskListener().provideContext(), HandleAccessibilityEventService.class));
        }
    }

    public static void onAppResume() {
        if (TaskId.get().mCurrentId > 0) {
            onlyStopTask(TaskId.get().mCurrentId);
        }
    }

    /**
     * 获取微信图片缓存存储文件列表 ，比对任务开始前的列表， 筛选出新增的文件 缓存的本地sp， 下次任务开始执行的时候，删除掉
     *
     * @param id
     */

    /**
     * 只结束任务 不回调 window
     *
     * @param id
     * @return
     */
    public static void onlyStopTask(int id) {
        if (TaskId.get().mCurrentId == id) {
            TaskId.get().mCurrentId = -1;
            TaskId.get().mTaskToggle = false;
            TaskId.get().mTaskKey = null;
        }
    }

    /**
     * 只结束任务 不回调 window
     *
     * @return
     */
    public static void onlyStopTask() {
        onlyStopTask(TaskId.get().mCurrentId);
    }

    /**
     * 结束任务
     *
     * @param id
     * @return
     */
    public static void stop(int id, String message, String leftMenutext, Runnable nextRunnable) {
        if (TaskId.get().mCurrentId == id) {
            if (LibInstance.getInstance().getTaskListener() != null) {
                LibInstance.getInstance().getTaskListener().onTaskComplete(message, leftMenutext, nextRunnable);
            }
            TaskId.get().mCurrentId = -1;
            TaskId.get().mTaskToggle = false;
            TaskId.get().mTaskKey = null;
        }
    }

}
