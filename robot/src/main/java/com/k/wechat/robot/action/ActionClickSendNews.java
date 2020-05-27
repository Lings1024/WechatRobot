package com.k.wechat.robot.action;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.annotation.EVENT_CLASS;
import com.k.wechat.robot.annotation.EVENT_TYPE;
import com.k.wechat.robot.task.data.SeniorGroupSendMessageMem;
import com.k.wechat.robot.util.UiKit;
import com.k.wechat.robot.util.WechatUI;

import java.util.Arrays;
import java.util.List;

import static com.k.wechat.robot.util.WechatUI.UI_CONTACT_INFO;


/**
 * 点击好友详情界面 发送消息按钮
 */
@EVENT_TYPE
@EVENT_CLASS(UI_CONTACT_INFO)
public class ActionClickSendNews implements Action {

    @Override
    public void run(AccessibilityEvent event) {

        if (WechatCurrentActivity.isCurrentActivity(UI_CONTACT_INFO)) {

            UiKit.sleep();
            AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_CONTACT_SEND_INFO);
            if (nodeInfosById != null) {
                AccessibilityNodeInfo nodeInfosLabelText = AccessibilityHelper.findNodeInfosById(WechatUI.ID_FIREND_DETAIL_LABEL_TEXT);
                if ("2".equals(SeniorGroupSendMessageMem.getLabelType()) && !TextUtils.isEmpty(SeniorGroupSendMessageMem.getNeedExcludedLabels())
                        && nodeInfosLabelText != null
                        && !TextUtils.isEmpty(nodeInfosLabelText.getText()) && isContainExcludedLabel(SeniorGroupSendMessageMem.getNeedExcludedLabels(), nodeInfosLabelText.getText().toString())
                ) {//屏蔽标签
                    //需要排除的标签不为空 并且包含了当前好友的标签之一 那么不发送 直接返回
                    AccessibilityHelper.performBack();
                    SeniorGroupSendMessageMem.currentSuccess();
                } else {
                    AccessibilityHelper.performClick(nodeInfosById);
                }
            } else {
                AccessibilityHelper.performBack();
                SeniorGroupSendMessageMem.currentSuccess();
            }
        }
    }


    private boolean isContainExcludedLabel(String needExcludedLabels, String labels) {
        List<String> excludedLabels = Arrays.asList(needExcludedLabels.split(","));

        //注意微信显示的是中文逗号"，"
        for (String label : labels.split("，")) {
            if (excludedLabels.contains(label)) {

                return true;
            }
        }
        return false;
    }
}
