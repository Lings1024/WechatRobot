package com.k.wechat.robot.action;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.annotation.EVENT_CLASS;
import com.k.wechat.robot.annotation.EVENT_TYPE;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.task.data.CommonListIteratMem;
import com.k.wechat.robot.task.data.SeniorGroupSendMessageMem;
import com.k.wechat.robot.util.L;
import com.k.wechat.robot.util.UiKit;
import com.k.wechat.robot.util.WechatUI;

import static com.k.wechat.robot.util.WechatUI.UI_LABLE_FRIEND_NAME;


/**
 * 获取标签的人员姓名
 */
@EVENT_TYPE
@EVENT_CLASS(UI_LABLE_FRIEND_NAME)
public class ActionObtainLableNames implements Action {
    @Override
    public void run(AccessibilityEvent event) {
        UiKit.sleep();
        final AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ANDROID_LIST_VIEW);
        clickPeople(nodeInfosById1);
    }

    private void obtainNames(AccessibilityNodeInfo nodeInfosById1) {
        for (int i = 0; nodeInfosById1 != null && i < nodeInfosById1.getChildCount(); i++) {
            AccessibilityNodeInfo child = nodeInfosById1.getChild(i);
            AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(child, WechatUI.ID_HEAD_PORTRAIT_FROM_CHAT_MESSAGE);
            if (nodeInfosById != null) {
                for (int i1 = 0; i1 < nodeInfosById.getChildCount(); i1++) {
                    AccessibilityNodeInfo child1 = nodeInfosById.getChild(i1);
                    AccessibilityNodeInfo nodeInfoName = AccessibilityHelper.findNodeInfosById(child1, WechatUI.ID_CONVERSATION_DETAIL_CONTACT_LIST_ITEM_NAME);
                    if (nodeInfoName != null) {
                        SayHelloMem.addListName(nodeInfoName.getText().toString().trim());
                    }
                }
            }
        }
        if (AccessibilityHelper.perform_scroll_forward(nodeInfosById1)) {
            UiKit.sleep();
            obtainNames(AccessibilityHelper.findNodeInfosById(WechatUI.ID_ANDROID_LIST_VIEW));
        }
    }

    private void clickPeople(AccessibilityNodeInfo list) {
        for (int i = 0; list != null && i < list.getChildCount(); i++) {
            AccessibilityNodeInfo child = list.getChild(i);
            AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(child, WechatUI.ID_HEAD_PORTRAIT_FROM_CHAT_MESSAGE);
            for (int i1 = 0; nodeInfosById != null && i1 < nodeInfosById.getChildCount(); i1++) {
                AccessibilityNodeInfo child1 = nodeInfosById.getChild(i1);
                AccessibilityNodeInfo nodename = AccessibilityHelper.findNodeInfosById(child1, WechatUI.ID_CONVERSATION_DETAIL_CONTACT_LIST_ITEM_NAME);
                if (nodename != null) {
                    String name = nodename.getText().toString();
                    if (!TextUtils.isEmpty(name) && !LabelSettingMem.isSendName(name)) {
                        L.e("标签下的姓名：" + name + " ," + LabelSettingMem.getSendNameListSize());
                        LabelSettingMem.addSendName(name);
                        SeniorGroupSendMessageMem.setmCurrent(name);
                        int i2 = SeniorGroupSendMessageMem.getmTime();
                        if (i2 > 0) {
                            UiKit.sleep(i2 * 1000);
                        }
                        CommonListIteratMem.mBitFlag = CommonListIteratMem.mBitFlag & 0b001;


                        //好友格式
                        if (!TextUtils.isEmpty(SeniorGroupSendMessageMem.getmNote()) && ("0".equals(SeniorGroupSendMessageMem.getmNote()) || "2".equals(SeniorGroupSendMessageMem.getmNote())) && (TaskId.assertTaskId(TaskId.TASK_SENIOR_MASS_SEND_FRIENDS))) {
                            SeniorGroupSendMessageMem.setmNameText(name);
                        }
                        AccessibilityHelper.performClick(nodename);
                        return;
                    }
                }
            }

        }
        if (AccessibilityHelper.perform_scroll_forward(list)) {
            UiKit.sleep();
            clickPeople(AccessibilityHelper.findNodeInfosById(WechatUI.ID_ANDROID_LIST_VIEW));
        } else {
            scrollTop(AccessibilityHelper.findNodeInfosById(WechatUI.ID_ANDROID_LIST_VIEW));
        }

    }

    private void scrollTop(AccessibilityNodeInfo list) {
        if (AccessibilityHelper.perform_scroll_backward(list)) {
            UiKit.sleep();
            scrollTop(AccessibilityHelper.findNodeInfosById(WechatUI.ID_ANDROID_LIST_VIEW));
        } else {
            AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_SEARCH_FRIEND_TEXT);
            if (nodeInfosById != null) {
                String s = nodeInfosById.getText().toString();
                L.e(s);
                LabelSettingMem.addSendLabelName(s);
                LabelSettingMem.labelListRemove(s);
                UiKit.sleep();
                AccessibilityHelper.performBack();
            }
        }

    }
}
