package com.k.wechat.robot.action;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.accessibility.AccessibilityEvent;

import com.k.wechat.robot.action.Action;
import com.k.wechat.robot.action.ActionAssertEntity;
import com.k.wechat.robot.action.ActionService;
import com.k.wechat.robot.action.WechatCurrentActivity;
import com.k.wechat.robot.annotation.EVENT_CLASS;
import com.k.wechat.robot.annotation.EVENT_CLASSES;
import com.k.wechat.robot.annotation.EVENT_TYPE;
import com.k.wechat.robot.annotation.RUN;
import com.k.wechat.robot.annotation.TASK_IDS;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.util.L;
import com.k.wechat.robot.util.ObtainWindowStateChangeEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



public class H {

    private ActionService service;
    private SparseArray<List<Method>> mTaskMethods = new SparseArray<>();
    private HashMap<Method, ActionAssertEntity> mActions = new HashMap<>();
    //是否匹配到当前真正的ui
    private boolean mMatchCurrentRealUi;

    private static final H ourInstance = new H();
    private long mUpdateTime;

    public static H getInstance() {
        return ourInstance;
    }

    private H() {

    }

    public ActionService createService() {
        if (service == null) {
            service = (ActionService) Proxy.newProxyInstance(ActionService.class.getClassLoader(), new Class<?>[]{ActionService.class},
                    new InvocationHandler() {

                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args)
                                throws Throwable {
                            AccessibilityEvent event = (AccessibilityEvent) args[0];
                            String wechatCurrentUi = event.getClassName().toString();
                            ActionAssertEntity actionAssertEntity = mActions.get(method);
                            if (actionAssertEntity == null) {
                                int type = -1;
                                String className = null;
                                String[] classNames = null;
                                Annotation[] annotations = method.getAnnotations();
                                if (annotations != null) {
                                    Class<? extends Action> run = method.getAnnotation(RUN.class).value();

                                    Annotation[] annotations1 = run.getAnnotations();
                                    if (annotations1 != null) {
                                        for (Annotation annotation : annotations1) {
                                            if (annotation instanceof EVENT_TYPE) {
                                                type = ((EVENT_TYPE) annotation).value();
                                            } else if (annotation instanceof EVENT_CLASS) {
                                                className = ((EVENT_CLASS) annotation).value();
                                            } else if (annotation instanceof EVENT_CLASSES) {
                                                classNames = ((EVENT_CLASSES) annotation).value();
                                            }
                                        }
                                    }
                                    if (type < 0) {
                                        throw new IllegalArgumentException("类 " + run.getName() + "必须指定一个触发事件的类型 EVENT_TYPE 来过滤触发的事件");
                                    }
                                    if (TextUtils.isEmpty(className) && (classNames == null || classNames.length == 0)) {
                                        throw new IllegalArgumentException("类 " + run.getName() + "必须指定一个类名 EVENT_CLASS 来过滤触发的事件");
                                    }

                                    mActions.put(method, actionAssertEntity = new ActionAssertEntity().eventType(type).className(className).classNames(classNames).action(run.newInstance()));
                                } else {
                                    throw new IllegalArgumentException("you should identify the required annotations!");
                                }

                            }
                            if (actionAssertEntity.getEventType() == event.getEventType() &&
                                    actionAssertEntity.getClassNames() != null ? Arrays.asList(actionAssertEntity.getClassNames()).contains(wechatCurrentUi) : TextUtils.equals(actionAssertEntity.getClassName(), wechatCurrentUi)) {
                                L.e((actionAssertEntity.getClassNames() != null ? Arrays.toString(actionAssertEntity.getClassNames()) : actionAssertEntity.getClassName()) + "-" + actionAssertEntity.getAction().getClass().getSimpleName());
                                actionAssertEntity.getAction().run(event);
                            }

                            return null;
                        }
                    });
        }
        return service;
    }

    public void excuteServiceMethods(int taskId, AccessibilityEvent event) {
        try {
            List<Method> methods = mTaskMethods.get(taskId);
            if (methods == null) {
                List<Method> taskMethods = new ArrayList<>();
                for (Method method : ActionService.class.getDeclaredMethods()) {
                    int[] task_ids = method.getAnnotation(TASK_IDS.class).value();
                    for (int task_id : task_ids) {
                        if (task_id == taskId) {
                            taskMethods.add(method);
                            break;
                        }
                    }
                }
                mTaskMethods.put(taskId, methods = taskMethods);
            }
            AccessibilityEvent targetEvent = null;
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                //页面切换，用页面特征去指明当前界面
                String currentActivity = WechatCurrentActivity.getInstance().getCurrentActivity();
                if (!TextUtils.isEmpty(currentActivity)) {
                    if (!TextUtils.equals(currentActivity, event.getClassName())) {
                        if (!mMatchCurrentRealUi) {
                            //连续两次页面不匹配，则放弃第二次 ,并且置标记为true
                            L.e("准备执行Actions,识别页面和原始不同" + " ,原始页面：" + event.getClassName() + " ,实际页面：" + currentActivity + " ,这是连续第二次不同，放弃执行这次...");
                            mMatchCurrentRealUi = true;
                        } else {
                            //不是这个event事件中的class，重新生成新的正确的
                            L.e("准备执行Actions,识别页面和原始不同" + " ,原始页面：" + event.getClassName() + " ,实际页面：" + currentActivity);
                            mMatchCurrentRealUi = false;
                            targetEvent = ObtainWindowStateChangeEvent.obtainEvent(currentActivity);
                        }
                    } else {
                        //和原来的event事件一样，用原来的
                        L.e("准备执行Actions,识别页面和原始相同" + " ,实际页面：" + currentActivity);
                        mMatchCurrentRealUi = true;
                        targetEvent = event;
                    }
                } else {
                    L.e("准备执行Actions,识别页面为 null" + " ,原始页面：" + event.getClassName());
                }
            } else {
                //页面切换之外的其他事件，用原来的
                targetEvent = event;
            }
            if (targetEvent != null) {
                //只有获取到当前页面才去执行，否则不执行
                for (Method method : methods) {
                    method.invoke(H.getInstance().createService(), targetEvent);
                }
            }
            //update lastest oprate timestrap
            long l = System.currentTimeMillis();
            mUpdateTime = l > mUpdateTime ? l : mUpdateTime;
        } catch (Exception e) {
            L.e(Log.getStackTraceString(e));
        }
    }

    public void releaseActions() {
        mActions.clear();
    }

    public long getUpdateTime() {
        return mUpdateTime;
    }

    public void updateOpreateTime() {
        mUpdateTime = System.currentTimeMillis();
    }

    public void updateOpreateTimeForward(long forwardRange) {
        mUpdateTime = System.currentTimeMillis() + forwardRange;
    }
}

