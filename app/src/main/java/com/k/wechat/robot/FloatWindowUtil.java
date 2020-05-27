package com.k.wechat.robot;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.k.wechat.robot.action.WechatCurrentActivity;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.util.L;
import com.k.wechat.robot.util.ObtainWindowStateChangeEvent;
import com.k.wechat.robot.util.WechatUI;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;


public class FloatWindowUtil {
    //    共提供 4 种 MoveType :
//
//    MoveType.slide : 可拖动，释放后自动贴边 （默认）
//
//    MoveType.back : 可拖动，释放后自动回到原位置
//
//    MoveType.active : 可拖动
//
//    MoveType.inactive : 不可拖动
    private static final FloatWindowUtil ourInstance = new FloatWindowUtil();
    private final Handler mHandler;
    private Context mContext;
    private String shortDesc;
    private String taskName;
    private Runnable runnable;
    private int taskId;
    private boolean taskState;
    private int[] mMenuCurLocation = new int[2];
    private int[] mMenuCurLocation2 = new int[2];

    public boolean isTryShowStartControl() {
        return mTryShowStartControl;
    }

    //点击开始任务btn 到微信页面显示悬浮开始按钮之间
    private boolean mTryShowStartControl;

    public static FloatWindowUtil getInstance() {
        return ourInstance;
    }

    private FloatWindowUtil() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void startTask(int taskId, Runnable runnable) {
        this.mTryShowStartControl = true;
        this.runnable = runnable;
        this.taskId = taskId;
        if (FloatWindow.get() == null) {
            return;
        }
        mHandler.post(() -> {

            ((ImageView) FloatWindow.get().getView().findViewById(R.id.iv_task_status)).setImageResource(R.mipmap.icon_control_start);
            FloatWindow.get().show();
        });
    }

    public void taskStarted(String shortDesc, int taskId) {

        this.taskName = shortDesc;
        this.taskState = true;
        if (FloatWindow.get() == null) {
            return;
        }
        mHandler.post(() -> {
            ((ImageView) FloatWindow.get().getView().findViewById(R.id.iv_task_status)).setImageResource(R.mipmap.icon_control_stop);
            FloatWindow.get().show();
        });
    }

    public void completeTask(String completeMessage) {
        taskState = false;
        if (FloatWindow.get() == null) {
            return;
        }
        mHandler.post(() -> {
            showShortNotifyToast(completeMessage);
            ((ImageView) FloatWindow.get().getView().findViewById(R.id.iv_task_status)).setImageResource(R.mipmap.icon_control_start);
            FloatWindow.get().hide();

            //主动显示开始任务按钮的 任务 任务结束 不去清空runnable
            runnable = null;
        });
    }

    public void completeTask(String completeMessage, String leftMenuText, Runnable nextRunnable) {
        taskState = false;
        if (FloatWindow.get() == null) {
            return;
        }
        mHandler.post(() -> {
            ((ImageView) FloatWindow.get().getView().findViewById(R.id.iv_task_status)).setImageResource(R.mipmap.icon_control_start);
            FloatWindow.get().hide();
        });
    }

    /**
     * 模拟去点击控制开关按钮
     */
    public void performClickControlBtn() {
        mHandler.post(() -> {
            FloatWindow.get().show();
            //主动点击开始window 按钮 开始任务
            FloatWindow.get().getView().performClick();
        });
    }

    public boolean getTaskStatus() {
        return taskState;
    }

    public void init(Context context) {

        this.mContext = context;
        Class<?>[] selectImageClass = new Class[3];
        try {
            selectImageClass[0] = MainActivity.class;
            selectImageClass[1] = MassSendFriendsActivity.class;
            selectImageClass[2] = AddGroupFriendsActivity.class;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), Log.getStackTraceString(e));
        }
        if (FloatWindow.get() == null) {

            View taskControl = LayoutInflater.from(context).inflate(R.layout.window_task_control, null);

            taskControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (runnable != null) {
                        L.e("perform click control button");
                        //首先判断任务开始的界面对不对
                        if (!taskState) {
                            if (!TaskId.get().mStartFromWindow && WechatCurrentActivity.isWechatActivity() && !WechatCurrentActivity.isCurrentActivity(WechatUI.UI_LUANCHER) && !BackToMainUiService.concludeFilter()) {
                                //wechatId has init
                                //不是从float window开始的
                                //任务没有开始
                                //必须在主界面
                                //不是主界面 给提示 不执行任务
//                              showToast("请先回到微信主界面");
                                BackToMainUiService.start(mContext);
                                return;
                            }
                        }
                        taskState = !taskState;
                        if (taskState) {
                            TaskId.get().mTaskToggle = true;
                            runnable.run();
                            TaskId.get().mStartFromWindow = false;
                            //执行一个当前界面的event
                            AccessibilityEvent obtain = ObtainWindowStateChangeEvent.obtainEvent(WechatCurrentActivity.getInstance().getCurrentActivity());
                            if (obtain != null) {
                                if (mContext != null) {
                                    L.e("start run task");
                                    HandleAccessibilityEventService.startWithEvent(mContext, obtain);
                                }
                            }
                        } else {
                            TaskId.stop();
                        }
//                        ((TextView) taskControl.findViewById(R.id.tv_task_status_desc)).setText((taskState ? "结束" : "开始").concat(shortDesc));
                        ((ImageView) taskControl.findViewById(R.id.iv_task_status)).setImageResource(taskState ? R.mipmap.icon_control_stop : R.mipmap.icon_control_start);
                    } else {
                        //手动结束的话 如果有任务的标识 那么去掉，不能看作执行完成
                        TaskId.get().mTaskKey = null;
                        TaskId.stop();
                    }
                }
            });

            FloatWindow
                    .with(context)
                    .setView(taskControl)
                    .setWidth(DpUtil.dp2px(context, 102))                               //设置控件宽高
                    .setHeight(DpUtil.dp2px(context, 70))
                    .setX(DpUtil.getScreenWidth(context) - DpUtil.dp2px(context, 102))                                   //设置控件初始位置
                    .setY(Screen.height, 0.4f)
                    .setDesktopShow(false)                        //桌面显示
                    .setFilter(false, selectImageClass)
                    .build();
        }


        if (FloatWindow.get("window_toast") == null) {

            View toast = LayoutInflater.from(context).inflate(R.layout.window_toast, null);
            FloatWindow
                    .with(context)
                    .setView(toast)
                    .setWidth(Screen.width, 1f)                               //设置控件宽高
                    .setHeight(Screen.height, 0.15f)
                    .setX(Screen.width, 0f)                                   //设置控件初始位置
                    .setY(Screen.height, 0.75f)
                    .setDesktopShow(false)                        //桌面显示
                    .notTouchable(true)
                    .setTag("window_toast")
                    .setFilter(false, selectImageClass)
                    .setMoveType(MoveType.inactive)
                    .build();
        }

    }


    private long showTime = 0;

    public void showShortNotifyToast(String message) {
        if (FloatWindow.get("window_toast") != null) {
            mHandler.post(() -> {
                showTime = System.currentTimeMillis();
                ((TextView) FloatWindow.get("window_toast").getView().findViewById(R.id.tv_message)).setText(message);
                FloatWindow.get("window_toast").show();

                mHandler.postDelayed(() -> {
                    if (System.currentTimeMillis() - showTime >= 2000) {
                        FloatWindow.get("window_toast").hide();
                    }
                }, 2000);
            });
        }
    }

    public void startFromWindow() {
        TaskId.get().mStartFromWindow = true;
    }

    public void release() {
        try {
            if (FloatWindow.get().isShowing()) {
                FloatWindow.destroy();
            }
            if (FloatWindow.get("window_toast").isShowing()) {
                FloatWindow.destroy("window_toast");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
