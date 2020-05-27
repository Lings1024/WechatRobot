package com.k.wechat.robot.action;

import android.view.accessibility.AccessibilityEvent;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.annotation.EVENT_CLASS;
import com.k.wechat.robot.annotation.EVENT_TYPE;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.task.data.AddGroupFriendMem;
import com.k.wechat.robot.util.UiKit;

import static android.view.accessibility.AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
import static com.k.wechat.robot.util.WechatUI.CLASS_NAME_TOAST;
import static com.k.wechat.robot.util.WechatUI.UI_ADD_FRIEND_VERIFY_APPLY;
import static com.k.wechat.robot.util.WechatUI.UI_CONTACT_INFO;


@EVENT_TYPE(TYPE_NOTIFICATION_STATE_CHANGED)
@EVENT_CLASS(CLASS_NAME_TOAST)
public class ActionAddFirendFailForToastBlackYou implements Action {
    @Override
    public void run(AccessibilityEvent event) {
        if ((WechatCurrentActivity.isCurrentActivity(UI_CONTACT_INFO) || WechatCurrentActivity.isCurrentActivity(UI_ADD_FRIEND_VERIFY_APPLY)) && event.getText() != null
                && !event.getText().isEmpty()) {
            String text = event.getText().get(0).toString();
            if (text.contains("添加联系人失败") || text.contains("操作过于频繁") || text.contains("你回复太快了，请休息一下稍后再试") || text.contains("对方帐号异常，无法添加朋友。") || text.contains("由于对方的隐私设置") || text.contains("发送失败")) {
                if (TaskId.assertTaskId(TaskId.TASK_ADD_GROUP_FRIEND)) {
                    AddGroupFriendMem.currentSuccess();
                }
                AccessibilityHelper.performBack();
                UiKit.sleep();
                if (WechatCurrentActivity.isCurrentActivity(UI_ADD_FRIEND_VERIFY_APPLY)) {
                    AccessibilityHelper.performBack();
                }
            }
        }
    }
}
