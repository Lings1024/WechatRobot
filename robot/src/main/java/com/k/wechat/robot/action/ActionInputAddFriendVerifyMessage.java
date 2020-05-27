package com.k.wechat.robot.action;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.LibInstance;
import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.annotation.EVENT_CLASS;
import com.k.wechat.robot.annotation.EVENT_TYPE;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.task.data.AddGroupFriendMem;
import com.k.wechat.robot.util.L;
import com.k.wechat.robot.util.UiKit;
import com.k.wechat.robot.util.WeChatHelper;
import com.k.wechat.robot.util.WechatUI;

import static com.k.wechat.robot.util.WechatUI.UI_ADD_FRIEND_VERIFY_APPLY;
import static com.k.wechat.robot.util.WechatUI.UI_CONTACT_LABLE;

@EVENT_TYPE
@EVENT_CLASS(UI_ADD_FRIEND_VERIFY_APPLY)
public class ActionInputAddFriendVerifyMessage implements Action {
    @Override
    public void run(AccessibilityEvent event) {
        UiKit.sleepL();
        //如果 需要设置标签 先点击标签 去设置标签
        L.e("AddGroupFriendMem.label():" + AddGroupFriendMem.label() + "AddGroupFriendMem.flag()" + AddGroupFriendMem.flag());
        if (!TextUtils.isEmpty(AddGroupFriendMem.label()) && !AddGroupFriendMem.flag()) {
            if (!WeChatHelper.above709(LibInstance.getInstance().getTaskListener().provideContext())) {//不是709就进来，709改版了
                AccessibilityNodeInfo nodeLabel = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ADD_FIREND_VERIFY_LABEL);
                L.e("nodeLabel:" + nodeLabel);
                if (nodeLabel != null) {
                    AccessibilityHelper.performClick(nodeLabel);
                } else {
                    AccessibilityHelper.performClick(WechatUI.ID_ADD_FIREND_VERIFY_LABEL_CONTAINER);
                }
                WechatCurrentActivity.getInstance().updateCurrentManually(UI_CONTACT_LABLE);
                L.e("return");
                return;
            }
        } else {
            L.e("12345");
        }
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ADD_FIREND_VERIFY_MESSAGE);
        AccessibilityNodeInfo nodeInfosByIdRemark = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ADD_FIREND_VERIFY_REMARK);
        if (nodeInfosById != null) {
            AccessibilityHelper.performClick(nodeInfosById);
        } else {
            AccessibilityHelper.performClick(nodeInfosByIdRemark);
        }
        if (TaskId.assertTaskId(TaskId.TASK_ADD_GROUP_FRIEND)) {
            if (nodeInfosById != null) {
                AccessibilityHelper.performSetText(nodeInfosById, TextUtils.isEmpty(AddGroupFriendMem.message()) ? nodeInfosById.getText().toString() : AddGroupFriendMem.message());
            }
            if (nodeInfosByIdRemark != null) {
                //设置备注为 群成员内的备注姓名
                if (!TextUtils.isEmpty(AddGroupFriendMem.getmNote())) {
                    AccessibilityHelper.performClick(nodeInfosByIdRemark);

                    String remark = AddGroupFriendMem.getmNote();
                    String s = nodeInfosByIdRemark.getText().toString();
                    if (!s.startsWith(AddGroupFriendMem.getmNote())) {
                        remark = AddGroupFriendMem.getmNote().concat(s);
                    }
                    AccessibilityHelper.performSetText(nodeInfosByIdRemark, remark);
                }
            }
        }

        UiKit.sleepLL();
        UiKit.sleepLL();
        AccessibilityNodeInfo n = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ADD_FIREND_VERIFY_SEND);
        AccessibilityHelper.performClick(n);
        //点击发送按钮之后手动触发到好友详情界面

        if (TaskId.assertTaskId(TaskId.TASK_ADD_GROUP_FRIEND)) {
            AddGroupFriendMem.currentSendVerifySuccess();
        }
    }
}
