package com.k.wechat.robot.action;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.LibInstance;
import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.annotation.EVENT_CLASS;
import com.k.wechat.robot.annotation.EVENT_TYPE;
import com.k.wechat.robot.task.data.AddGroupFriendMem;
import com.k.wechat.robot.task.data.CommonListIteratMem;
import com.k.wechat.robot.util.UiKit;
import com.k.wechat.robot.util.WechatUI;

import static com.k.wechat.robot.util.WechatUI.TEXT_GROUP_DETAIL_SCAN_ALL_MEM;
import static com.k.wechat.robot.util.WechatUI.UI_CHATROOM;


/**
 * 滑动群组详情联系人列表 获取列表数据
 */
@EVENT_TYPE
@EVENT_CLASS(UI_CHATROOM)
public class ActionClickGroupDetailContactListItem implements Action {
    @Override
    public void run(AccessibilityEvent event) {
        if (!CommonListIteratMem.mTempFlag) {
            //没有进入过群组详情界面
            CommonListIteratMem.mTempFlag = true;
        }
        if (WechatCurrentActivity.isCurrentActivity(UI_CHATROOM)) {

            AccessibilityNodeInfo nodeListView = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ANDROID_LIST_VIEW);
            if (AddGroupFriendMem.useChattingUi()) {

                if (!AddGroupFriendMem.hasSearchOver() && nodeListView != null) {
                    //数据为空 并且没有获取过数据 开始获取数据
                    //进来需要先到list的最顶部
                    //为保证顶部 滑一下
                    AddGroupFriendMem.scroll2Top(nodeListView);

                    if (checkAllGroupMemberExist()) {
                        //存在查看全部群成员
                        //不使用这个页面 去点击查看全部群成员
                        AddGroupFriendMem.searchNotOver();
                        clickSeeAllGroupMember();
                        //返回 不去处理点击好友item了
                        return;
                    } else {
                        //找完继续滑到最顶部
                        AddGroupFriendMem.scroll2Top(nodeListView);
                        AddGroupFriendMem.searchOver();
                    }
                }

                if (!AddGroupFriendMem.isAvalible()) {

                    UiKit.sleep(AddGroupFriendMem.getmTime() * 1000);
                    //开始发送消息
                    AddGroupFriendMem.clickFriend2ContactInfo();
                }
            } else {
                //去点击查看全部群成员
                AddGroupFriendMem.searchNotOver();
                clickSeeAllGroupMember();
            }

        }

    }

    private boolean checkAllGroupMemberExist() {
        AccessibilityNodeInfo nodeListView = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ANDROID_LIST_VIEW);
        if (nodeListView != null) {

            //先看下查看全部群成员 这个item 存不存在
            for (int i = 0; i < nodeListView.getChildCount(); i++) {
                if (nodeListView.getChild(i).getChildCount() == 1 && TEXT_GROUP_DETAIL_SCAN_ALL_MEM.equals(nodeListView.getChild(i).getChild(0).getText())) {
                    //有查看全部群成员
                    AddGroupFriendMem.dontUseChattingUi();
                    return true;
                }
            }
            if (AccessibilityHelper.perform_scroll_forward(nodeListView)) {
                UiKit.sleep();
                return checkAllGroupMemberExist();
            }

        }
        return false;
    }

    private void clickSeeAllGroupMember() {
        AccessibilityNodeInfo nodeListView = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ANDROID_LIST_VIEW);
        if (nodeListView != null) {

            for (int i = 0; i < nodeListView.getChildCount(); i++) {
                if (nodeListView.getChild(i).getChildCount() == 1 && TEXT_GROUP_DETAIL_SCAN_ALL_MEM.equals(nodeListView.getChild(i).getChild(0).getText())) {
                    //有查看全部群成员
                    AccessibilityHelper.performClick(nodeListView.getChild(i));
                    return;
                }
            }
            //没找到
            if (AccessibilityHelper.perform_scroll_forward(nodeListView)) {
                UiKit.sleep();
                clickSeeAllGroupMember();
            } else {
                AddGroupFriendMem.scroll2Top(nodeListView);
                checkAllGroupMemberExist();
            }
        }
    }

}
