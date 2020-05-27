package com.k.wechat.robot;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.action.H;
import com.k.wechat.robot.action.WechatCurrentActivity;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.util.L;
import com.k.wechat.robot.util.ObtainWindowStateChangeEvent;
import com.k.wechat.robot.util.WeChatHelper;
import com.k.wechat.robot.util.WechatUI;

import java.util.ArrayList;
import java.util.List;

public class DeamonService extends IntentService {
    private static final String EXTRA_TASK_ID = "extra_task_id";

    private AccessibilityEvent event;
    private List<String> mTodoQueue;

    public static List<Integer> list = new ArrayList<>();

    static {
    }

    public DeamonService() {
        super("DeamonService");
    }

    public static void startWithTaskId(Context context, int taskId) {
        Intent intent = new Intent(context, DeamonService.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mTodoQueue = new ArrayList<>();
        //更新当前操作时间戳
        H.getInstance().updateOpreateTime();
        long todoTimeStrap = System.currentTimeMillis();
        if (intent != null) {
            int taskId = intent.getIntExtra(EXTRA_TASK_ID, -2);
            while (WeChatHelper.hasInt && !list.contains(taskId) && TaskId.assertTaskId(taskId)) {
                //不在这个list中
                if (isResponseTimeOut()) {
                    //超时
                    //手动触发动作
                    if (System.currentTimeMillis() - todoTimeStrap > 5000) {
                        if (TextUtils.isEmpty(WechatCurrentActivity.getAccessibilityWechatUi()) || System.currentTimeMillis() - todoTimeStrap > 12000) {
                            //微信页面为空 或者
                            TaskId.stop();
                        } else if (!WechatCurrentActivity.isCurrentActivity(WechatUI.UI_LUANCHER)) {
                            //不是在主界面
                            if (WechatCurrentActivity.getInstance().getCurrentActivity() != null) {
                                //界面不为空
                                L.e("超时5000，返回");
                                //超时5s以上 并且不是在主界面 ,那么执行返回
                                AccessibilityHelper.performBack();
                                H.getInstance().updateOpreateTime();
                                todoTimeStrap = System.currentTimeMillis();
                            }
                        }
                    } else if (!WechatCurrentActivity.isCurrentActivity(WechatUI.UI_LUANCHER) && mTodoQueue.size() == 2 && mTodoQueue.get(0).equals(mTodoQueue.get(1)) && mTodoQueue.get(0).equals(WechatCurrentActivity.getInstance().getCurrentActivity())) {
                        //不是在主界面
                        L.e("连续3次相同的todo，返回");
                        //连续三次触发相同的界面 并且不是在主界面 ,那么执行返回
                        AccessibilityHelper.performBack();
                        H.getInstance().updateOpreateTime();
                        todoTimeStrap = System.currentTimeMillis();
                        //清空缓存
                        mTodoQueue.clear();
                    } else {
                        todo(taskId);
                    }
                } else {
                    //没有超时，页面切换正常
                    if (mTodoQueue.size() > 0) {
                        //queue中有旧数据
                        if (!TextUtils.equals(WechatCurrentActivity.getInstance().getCurrentActivity(), mTodoQueue.get(mTodoQueue.size() - 1))) {
                            //当前页面和queue中的倒数一个不同，那么清空queue
                            mTodoQueue.clear();
                        }
                    }
                    todoTimeStrap = System.currentTimeMillis();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void todo(int taskId) {
        String currentActivity = WechatCurrentActivity.getInstance().getCurrentActivity();
        if (currentActivity != null) {
            //不需要判断页面名字了，现在都是WechatUI中的页面或者dialog
            if (mTodoQueue.size() == 2) {
                mTodoQueue.remove(0);
            }
            mTodoQueue.add(currentActivity);
            //是activity页面才去触发
            if (event != null) {
                event.recycle();
            }
            event = ObtainWindowStateChangeEvent.obtainEvent(currentActivity);
            if (event != null) {
                L.e("手动触发Event ：" + event.getClassName());
                HandleAccessibilityEventService.startWithEvent(this, event);
            }
        }
    }

    /**
     * 操作超时
     *
     * @return
     */
    public static boolean isResponseTimeOut() {
        return System.currentTimeMillis() - H.getInstance().getUpdateTime() > 1000;
    }
}
