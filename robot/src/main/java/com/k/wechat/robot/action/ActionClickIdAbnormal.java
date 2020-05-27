package com.k.wechat.robot.action;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.annotation.EVENT_CLASSES;
import com.k.wechat.robot.annotation.EVENT_TYPE;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.task.data.AddGroupFriendMem;
import com.k.wechat.robot.util.UiKit;
import com.k.wechat.robot.util.WechatUI;

import static com.k.wechat.robot.util.WechatUI.UI_COLLECT_CONTENT_POPUP_WINDOW;
import static com.k.wechat.robot.util.WechatUI.UI_COLLECT_CONTENT_POPUP_WINDOW7012;
import static com.k.wechat.robot.util.WechatUI.UI_COLLECT_CONTENT_POPUP_WINDOW705;
import static com.k.wechat.robot.util.WechatUI.UI_COLLECT_CONTENT_POPUP_WINDOW707;


/**
 * 删除死账号异常弹窗 ,添加群组好友 [提示, 由于对方的隐私设置，你无法通过群聊将其添加至通讯录。, 确定]
 */
@EVENT_TYPE
@EVENT_CLASSES({UI_COLLECT_CONTENT_POPUP_WINDOW, UI_COLLECT_CONTENT_POPUP_WINDOW705,UI_COLLECT_CONTENT_POPUP_WINDOW707,UI_COLLECT_CONTENT_POPUP_WINDOW7012})
public class ActionClickIdAbnormal implements Action {
    @Override
    public void run(AccessibilityEvent event) {

            AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_TRUE_DELETE_FRIEND);
            if (nodeInfosById != null) {
                AccessibilityHelper.performClick(nodeInfosById);
            }

            if (TaskId.assertTaskId(TaskId.TASK_ADD_GROUP_FRIEND)) {
                AddGroupFriendMem.currentSuccess();
                UiKit.sleep();
                AccessibilityHelper.performBack();
            }
        }

}
