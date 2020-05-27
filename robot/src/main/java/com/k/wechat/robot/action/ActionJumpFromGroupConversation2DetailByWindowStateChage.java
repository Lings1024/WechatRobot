package com.k.wechat.robot.action;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.LibInstance;
import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.annotation.EVENT_CLASS;
import com.k.wechat.robot.annotation.EVENT_TYPE;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.task.data.CommonListIteratMem;
import com.k.wechat.robot.util.WechatUI;

import static com.k.wechat.robot.util.WechatUI.ID_CONVERSATION_MORE;
import static com.k.wechat.robot.util.WechatUI.UI_CHATTING_UI;


@EVENT_TYPE
@EVENT_CLASS(UI_CHATTING_UI)
public class ActionJumpFromGroupConversation2DetailByWindowStateChage implements Action {
    @Override
    public void run(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(ID_CONVERSATION_MORE);
        AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_DOWN_MORE_FROM_FRIEND);
        if (nodeInfosById != null && !TextUtils.isEmpty(nodeInfosById.getContentDescription()) && "聊天信息".equals(nodeInfosById.getContentDescription()) || nodeInfosById1 != null) {
            AccessibilityHelper.performClick(ID_CONVERSATION_MORE);
        }
    }
}
