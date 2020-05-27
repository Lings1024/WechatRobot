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
import com.k.wechat.robot.util.UiKit;
import com.k.wechat.robot.util.WechatUI;

import static com.k.wechat.robot.task.TaskId.TASK_SENIOR_MASS_SEND_FRIENDS;
import static com.k.wechat.robot.util.WechatUI.ID_DOWN_MORE_FROM_FRIEND;
import static com.k.wechat.robot.util.WechatUI.TEXT_NEW_PICTURE_REMIND;
import static com.k.wechat.robot.util.WechatUI.UI_CHATTING_UI;


/**
 * 发送群聊消息——进入群聊界面——输入数据
 */
@EVENT_TYPE
@EVENT_CLASS(UI_CHATTING_UI)
public class ActionChattingUiInputText implements Action {
    @Override
    public void run(AccessibilityEvent event) {
        if (TaskId.assertTaskId(TASK_SENIOR_MASS_SEND_FRIENDS)) {
            String message = getMessage(SeniorGroupSendMessageMem.message());
            UiKit.sleep();
            if (!TextUtils.isEmpty(message)) {//有文字才发文字
                sendText(message);
                UiKit.sleep();
            }
            if (sendPhoto()) return;//有图片发送，没有就直接返回
            //重置 发图片文本的标记位
            CommonListIteratMem.mBitFlag = CommonListIteratMem.mBitFlag | 0b100;
            if (TaskId.assertTaskId(TASK_SENIOR_MASS_SEND_FRIENDS)) {
                SeniorGroupSendMessageMem.currentSuccess();
            }
            UiKit.sleep();
            //发送完了返回
            AccessibilityHelper.performClick(WechatUI.ID_GROUP_OVER_RETURN);

        }

    }


    private boolean sendPhoto() {

        if (CommonListIteratMem.mBitFlag == 0b001) {//有图片还没发送过
            AccessibilityHelper.performClick(ID_DOWN_MORE_FROM_FRIEND);
            UiKit.sleep();
            AccessibilityNodeInfo pictureRemind = AccessibilityHelper.findNodeInfosByText(TEXT_NEW_PICTURE_REMIND);
            if (pictureRemind != null) {
                AccessibilityHelper.performClick(pictureRemind);
                return true;
            }
            if (!clickMyAlbum()) {
                AccessibilityHelper.performClick(ID_DOWN_MORE_FROM_FRIEND);
                UiKit.sleep(1000);
                clickMyAlbum();
            }
            return true;
        }
        return false;
    }

    private void sendText(String message) {
        /////////////////////////输入消息/////////////////////////
        AccessibilityNodeInfo nodeInfosById2 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_PRESS_SPEAK);
        if (nodeInfosById2 != null) {
            AccessibilityHelper.performClick(WechatUI.ID_SWITCH_KEYBOARD);
        }
        AccessibilityHelper.performSetText(AccessibilityHelper.findNodeInfosById(WechatUI.ID_INPUT_TEXT_BOX), message);

        //点击发送按钮
        AccessibilityHelper.performClick(WechatUI.ID_INPUT_TEXT_SEND_KEYS);
        ////////////////////////输入消息/////////////////////////
    }

    private String getMessage(String s) {
        String message;//昵称打开发送昵称
        if (!TextUtils.isEmpty(s)) {
            if (!TextUtils.isEmpty(SeniorGroupSendMessageMem.getmNote()) && ("0".equals(SeniorGroupSendMessageMem.getmNote()) || "2".equals(SeniorGroupSendMessageMem.getmNote()))) {
                String remark = SeniorGroupSendMessageMem.getmNameText();
                if (!TextUtils.isEmpty(remark)) {
                    if ("2".equals(SeniorGroupSendMessageMem.getmNote())) {
                        if (remark.contains("@") && !remark.endsWith("@")) {
                            remark = remark.substring(remark.lastIndexOf("@") + 1, remark.length());
                        } else {
                            remark = "";
                        }
                    }
                }
                message = remark + (!TextUtils.isEmpty(remark) ? "，" : "") + s;
            } else {
                message = s;
            }
            return message;
        }
        return "";
    }

    private boolean clickMyAlbum() {
        AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_DOWN_MORE_LINEAR_LAYOUT);
        for (int i = 0; nodeInfosById1 != null && i < nodeInfosById1.getChildCount(); i++) {
            AccessibilityNodeInfo child = nodeInfosById1.getChild(i);
            AccessibilityNodeInfo nodeInfosByText = AccessibilityHelper.findNodeInfosByText(child, "相册");
            if (nodeInfosByText != null) {
                AccessibilityHelper.performClick(nodeInfosByText);
                return true;
            }
        }
        return false;
    }

    //判断文字是不是发送过了
    private void checkText() {
        AccessibilityNodeInfo nodeInfosById12 = AccessibilityHelper.findNodeInfosById(WechatUI.ID_FRIEND_CHAT_LINEAR_LAYOUT);
        AccessibilityNodeInfo nodeInfosByClassName = AccessibilityHelper.findNodeInfosByClassName(nodeInfosById12, WechatUI.CLASS_NAME_LIST_VIEW);
        if (nodeInfosByClassName != null && nodeInfosByClassName.getChildCount() > 0) {
            AccessibilityNodeInfo lastItem = nodeInfosByClassName.getChild(nodeInfosByClassName.getChildCount() - 1);
            //最后一条 是正常消息的话 跳出
            AccessibilityNodeInfo lastItemRed = AccessibilityHelper.findNodeInfosById(lastItem, WechatUI.ID_RESEND_PUSH);
            AccessibilityNodeInfo lastItemMessage = AccessibilityHelper.findNodeInfosById(lastItem, WechatUI.ID_SEND_KEYS_NEWS);
            AccessibilityNodeInfo lastItemAvater = AccessibilityHelper.findNodeInfosById(lastItem, WechatUI.ID_MESSAGE_LIST_ITEM_AVATER);
            if (lastItemMessage != null && lastItemAvater != null && lastItemRed == null) {

            } else {
                //最后一条可能不正常 有可能对方账号异常! 7.0.0 拿不到text
                for (int i = nodeInfosByClassName.getChildCount() - 1; i > -1; i--) {
                    AccessibilityNodeInfo child = nodeInfosByClassName.getChild(i);
                    AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(child, WechatUI.ID_RESEND_PUSH);
                    if (nodeInfosById1 != null) {
//                        DetectDieFansMem.addNeedDeleteName(SeniorGroupSendMessageMem.current());
                        AccessibilityHelper.performClick(WechatUI.ID_SNS_CAMERA_BTN);
                        SeniorGroupSendMessageMem.currentSuccess();
                        return;
                    }
                }
            }
        }
        SeniorGroupSendMessageMem.currentSuccess();
        AccessibilityHelper.performClick(WechatUI.ID_GROUP_OVER_RETURN);
    }
}
