package com.k.wechat.robot.action;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.annotation.EVENT_CLASS;
import com.k.wechat.robot.annotation.EVENT_TYPE;
import com.k.wechat.robot.task.data.AddGroupFriendMem;
import com.k.wechat.robot.util.UiKit;
import com.k.wechat.robot.util.WechatUI;

import static com.k.wechat.robot.util.WechatUI.ID_GROUP_SEE_ALL_MEMBER_GRIDVIEW;


/**
 * 滑动群组详情联系人列表 获取列表数据
 */
@EVENT_TYPE
@EVENT_CLASS(WechatUI.UI_SEE_ALL_GROUP_MEMBER)
public class ActionScrollGroupAllMemberGridView implements Action {
    @Override
    public void run(AccessibilityEvent event) {
        UiKit.sleep();
        AccessibilityNodeInfo nodeGridView = AccessibilityHelper.findNodeInfosById(ID_GROUP_SEE_ALL_MEMBER_GRIDVIEW);
        if (nodeGridView != null) {
            if (!AddGroupFriendMem.hasSearchOver()) {
                //默认进来是grid的最顶部
                //为保证顶部 滑一下
                AddGroupFriendMem.scroll2Top(nodeGridView);
                //直接完成了 ，不去获取全部人名了，挨个往下加得了
                AddGroupFriendMem.searchOver();
            }
            AddGroupFriendMem.clickFriend2ContactInfoInAllMember();
        }
    }
}
