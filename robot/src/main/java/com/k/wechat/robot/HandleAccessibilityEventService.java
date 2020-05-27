package com.k.wechat.robot;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;


import com.k.wechat.robot.action.H;
import com.k.wechat.robot.task.TaskId;

import static android.view.accessibility.AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;

public class HandleAccessibilityEventService extends IntentService {
    private static final String EXTRA_EVENT = "extra_event";

    public HandleAccessibilityEventService() {
        super("HandleAccessibilityEventService");
    }

    public static void startWithEvent(Context context, AccessibilityEvent event) {
        if (TYPE_NOTIFICATION_STATE_CHANGED == event.getEventType()) {
            H.getInstance().excuteServiceMethods(TaskId.get().mCurrentId, event);
            return;
        }
        Intent intent = new Intent(context, HandleAccessibilityEventService.class);
        intent.putExtra(EXTRA_EVENT, event);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            AccessibilityEvent event = intent.getParcelableExtra(EXTRA_EVENT);
            if (event != null) {
                H.getInstance().excuteServiceMethods(TaskId.get().mCurrentId, event);
            }
        }
    }
}
