package com.k.wechat.robot.action;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.LibInstance;
import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.annotation.EVENT_CLASS;
import com.k.wechat.robot.annotation.EVENT_TYPE;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.task.data.CommonListIteratMem;
import com.k.wechat.robot.task.data.SeniorGroupSendMessageMem;
import com.k.wechat.robot.util.UiKit;
import com.k.wechat.robot.util.WechatUI;


@EVENT_TYPE
@EVENT_CLASS(WechatUI.UI_LUANCHER)
public class ActionClickMainUiLableLayout implements Action {
    @Override
    public void run(AccessibilityEvent event) {
        if (TaskId.assertTaskId(TaskId.TASK_ADD_GROUP_FRIEND)) {
            if (!CommonListIteratMem.mTempFlag) {
                //没有进入过群组详情界面
                if (!TaskId.get().mStartFromWindow) {
                    LibInstance.getInstance().getTaskListener().showWindowToast("请选择进入一个微信群");
                }
            }
        } else {
            AccessibilityNodeInfo nodeListView = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ADDRESS_BOOK_LIST_VIEW);
            AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_CLICK_SEARCH_BOX);
            if (nodeInfosById != null) {
                AccessibilityHelper.performClick(AccessibilityHelper.findNodeInfosById(WechatUI.ID_MAIN_ADDRESSBOOK, 1));
                UiKit.sleep();
                //为保证顶部 滑一下
                SeniorGroupSendMessageMem.scroll2Top(nodeListView);

                AccessibilityHelper.performClick(AccessibilityHelper.findNodeInfosByText(nodeListView, WechatUI.TEXT_LABEL));
            }
        }

    }
}
