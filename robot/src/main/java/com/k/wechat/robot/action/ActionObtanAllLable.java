package com.k.wechat.robot.action;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.annotation.EVENT_CLASS;
import com.k.wechat.robot.annotation.EVENT_TYPE;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.util.L;
import com.k.wechat.robot.util.UiKit;
import com.k.wechat.robot.util.WechatUI;

import java.util.Set;

import static com.k.wechat.robot.util.WechatUI.UI_LABLE_PAGE;


@EVENT_TYPE
@EVENT_CLASS(UI_LABLE_PAGE)
public class ActionObtanAllLable implements Action {
    @Override
    public void run(AccessibilityEvent event) {
        UiKit.sleep();
        findLabel();
        if (TaskId.get().mCurrentId > -1) {
            if (!LabelSettingMem.getLabelListSize()) {
                chooseLabel();
            } else {
                TaskId.stop();
            }
        }
    }

    private void obtainAllLable(AccessibilityNodeInfo nodeInfosById, Set<String> labels) {
        for (int i = 0; nodeInfosById != null && i < nodeInfosById.getChildCount(); i++) {//数量
            AccessibilityNodeInfo child = nodeInfosById.getChild(i);
            AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(child, WechatUI.ID_SON_LABLE);
            if (nodeInfosById1 != null && !TextUtils.isEmpty(nodeInfosById1.getText())) {
                labels.add(nodeInfosById1.getText().toString());
            }
        }
        if (AccessibilityHelper.perform_scroll_forward(nodeInfosById)) {
            UiKit.sleep();
            obtainAllLable(AccessibilityHelper.findNodeInfosById(WechatUI.ID_ALL_LABLE), labels);
        }
    }

    private void obtainLableAndClick(AccessibilityNodeInfo nodeInfosById) {
        for (int i = 0; nodeInfosById != null && i < nodeInfosById.getChildCount(); i++) {//数量
            AccessibilityNodeInfo child = nodeInfosById.getChild(i);
            AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(child, WechatUI.ID_SON_LABLE);
            AccessibilityNodeInfo labelCount = AccessibilityHelper.findNodeInfosById(child, WechatUI.ID_LABELS_COUNT);
            if (nodeInfosById1 != null && !TextUtils.isEmpty(nodeInfosById1.getText().toString()) && labelCount != null && !"(0)".equals(labelCount.getText().toString())) {
                if (!SayHelloMem.keyContains(nodeInfosById1.getText().toString())) {
                    SayHelloMem.addLablesName(nodeInfosById1.getText().toString());
                    AccessibilityHelper.performClick(nodeInfosById1);
                    return;
                }
            }
        }
        if (AccessibilityHelper.perform_scroll_forward(AccessibilityHelper.findNodeInfosById(WechatUI.ID_ALL_LABLE))) {
            UiKit.sleep();
            obtainLableAndClick(AccessibilityHelper.findNodeInfosById(WechatUI.ID_ALL_LABLE));
        } else {
            //小米8手机有时候滑动不到底部就结束任务了，在尝试滑动一次
            UiKit.sleep();
            if (AccessibilityHelper.perform_scroll_forward(AccessibilityHelper.findNodeInfosById(WechatUI.ID_ALL_LABLE))) {
                UiKit.sleep();
                obtainLableAndClick(AccessibilityHelper.findNodeInfosById(WechatUI.ID_ALL_LABLE));
            } else {
                AccessibilityHelper.performBack();
            }
        }
    }

    private void chooseLabel() {
        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ALL_LABLE);
        for (int i = 0; nodeInfosById != null && i < nodeInfosById.getChildCount(); i++) {
            AccessibilityNodeInfo child = nodeInfosById.getChild(i);
            AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(child, WechatUI.ID_SON_LABLE);
            if (nodeInfosById1 != null && !TextUtils.isEmpty(nodeInfosById1.getText().toString())) {
                String name = nodeInfosById1.getText().toString();
                L.e(name);
                if (LabelSettingMem.containsLabelName(name)) {
                    if (!LabelSettingMem.isSendLabel(name)) {
//                    LabelSettingMem.addSendLabelName(name);
                        AccessibilityHelper.performClick(nodeInfosById1);
                        return;
                    }
                }
            }

        }
        if (AccessibilityHelper.perform_scroll_forward(nodeInfosById)) {
            UiKit.sleep();
            chooseLabel();
        } else if (WechatCurrentActivity.isCurrentActivity(UI_LABLE_PAGE)) {
            TaskId.stop();
        }
    }

    /**
     * 一直寻找标签list，找不到也不停止任务
     */
    private void findLabel() {
        if (WechatCurrentActivity.isCurrentActivity(UI_LABLE_PAGE)) {
            AccessibilityNodeInfo nodeLableList = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ALL_LABLE);
            //标签列表
            if (nodeLableList == null) {
                UiKit.sleep(1000);
                findLabel();
            }
        }
    }
}
