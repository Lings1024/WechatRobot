package com.k.wechat.robot.action;

import android.view.accessibility.AccessibilityEvent;

import com.k.wechat.robot.annotation.RUN;
import com.k.wechat.robot.annotation.TASK_IDS;

import static com.k.wechat.robot.task.TaskId.TASK_ADD_GROUP_FRIEND;
import static com.k.wechat.robot.task.TaskId.TASK_SENIOR_MASS_SEND_FRIENDS;


public interface ActionService {

    /**
     * 查看群组全部成员页面 获取数据
     *
     * @param event
     */
    @TASK_IDS({TASK_ADD_GROUP_FRIEND})
    @RUN(ActionScrollGroupAllMemberGridView.class)
    void a3(AccessibilityEvent event);


    /**
     * 点击【通讯录】列表 item
     *
     * @param event
     */
    @TASK_IDS({TASK_SENIOR_MASS_SEND_FRIENDS, TASK_ADD_GROUP_FRIEND})
    @RUN(ActionClickMainUiLableLayout.class)
    void nn(AccessibilityEvent event);

    /**
     * 点击联系人详情 添加到通讯录按钮
     *
     * @param event
     */
    @TASK_IDS({TASK_ADD_GROUP_FRIEND})
    @RUN(ActionClickContactInfoAddFriendBtn.class)
    void b(AccessibilityEvent event);

    /**
     * 点击联系人详情 添加到通讯录按钮 之后弹出添加失败的 toast
     *
     * @param event
     */
    @TASK_IDS({TASK_ADD_GROUP_FRIEND})
    @RUN(ActionAddFirendFailForToastBlackYou.class)
    void b1(AccessibilityEvent event);

    /**
     * 输入好友验证信息
     *
     * @param event
     */
    @TASK_IDS({TASK_ADD_GROUP_FRIEND})
    @RUN(ActionInputAddFriendVerifyMessage.class)
    void d(AccessibilityEvent event);

    @TASK_IDS({TASK_ADD_GROUP_FRIEND})
    @RUN(ActionJumpFromGroupConversation2DetailByWindowStateChage.class)
    void e(AccessibilityEvent event);

    @TASK_IDS({TASK_ADD_GROUP_FRIEND})
    @RUN(ActionClickGroupDetailContactListItem.class)
    void g1(AccessibilityEvent event);


    /**
     * 点击联系人详情界面【发送消息】按钮
     *
     * @param event
     */
    @TASK_IDS({TASK_SENIOR_MASS_SEND_FRIENDS})
    @RUN(ActionClickSendNews.class)
    void o(AccessibilityEvent event);// 点击发消息

    @TASK_IDS({TASK_SENIOR_MASS_SEND_FRIENDS})
    @RUN(ActionObtanAllLable.class)
    void r(AccessibilityEvent event);

    @TASK_IDS({TASK_SENIOR_MASS_SEND_FRIENDS})
    @RUN(ActionObtainLableNames.class)
    void r1(AccessibilityEvent event);


    /**
     * 【聊天窗口】界面 【输入】内容
     *
     * @param event
     */
    @TASK_IDS({TASK_SENIOR_MASS_SEND_FRIENDS})
    @RUN(ActionChattingUiInputText.class)
    void jj(AccessibilityEvent event);//高级群发好友

    @TASK_IDS({TASK_ADD_GROUP_FRIEND})
    @RUN(ActionClickIdAbnormal.class)
    void sss(AccessibilityEvent event);

}
