package com.k.wechat.robot.action;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.annotation.EVENT_CLASS;
import com.k.wechat.robot.annotation.EVENT_TYPE;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.task.data.AddGroupFriendMem;
import com.k.wechat.robot.util.UiKit;
import com.k.wechat.robot.util.WechatUI;

import static com.k.wechat.robot.util.WechatUI.UI_CONTACT_INFO;


@EVENT_TYPE
@EVENT_CLASS(UI_CONTACT_INFO)
public class ActionClickContactInfoAddFriendBtn implements Action {
    @Override
    public void run(AccessibilityEvent event) {
        UiKit.sleepL();
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_PP_DETAIL_ADD2CONTACTS);
        AccessibilityNodeInfo nodeInfoGender = AccessibilityHelper.findNodeInfosById(WechatUI.ID_CONTACT_INFO_GENDER);
        if (TaskId.assertTaskId(TaskId.TASK_ADD_GROUP_FRIEND)) {
            if (nodeInfosById != null && AddGroupFriendMem.isAvalible() && "添加到通讯录".equals(nodeInfosById.getText().toString())) {
                if (!TextUtils.isEmpty(AddGroupFriendMem.genderCondition()) && nodeInfoGender == null) {
                    //有性别条件的时候 需要等待 等性别加载出来
                    UiKit.sleepLL();
                    UiKit.sleepL();
                    nodeInfoGender = AccessibilityHelper.findNodeInfosById(WechatUI.ID_CONTACT_INFO_GENDER);
                }
                if (TextUtils.isEmpty(AddGroupFriendMem.genderCondition()) || (nodeInfoGender != null && AddGroupFriendMem.genderCondition().equals(nodeInfoGender.getContentDescription()))) {
                    UiKit.sleepLL();
                    AccessibilityHelper.performClick(WechatUI.ID_PP_DETAIL_ADD2CONTACTS);
                } else {
                    AddGroupFriendMem.currentSuccess();
                    AccessibilityHelper.performBack();
                }
            } else if (AddGroupFriendMem.isAvalible()) {
                UiKit.sleepL();
                AddGroupFriendMem.currentSuccess();
                AccessibilityHelper.performBack(UI_CONTACT_INFO);
            } else {
                UiKit.sleepL();
                AccessibilityHelper.performBack(UI_CONTACT_INFO);
            }
        }
    }
}
