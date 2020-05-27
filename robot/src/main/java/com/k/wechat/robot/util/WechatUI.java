package com.k.wechat.robot.util;


import com.k.wechat.robot.annotation.UI_CONDITION;
import com.k.wechat.robot.annotation.UI_CONDITION_NONE;
import com.k.wechat.robot.annotation.UI_TITLE;
import com.k.wechat.robot.condition.ConditionChatting;
import com.k.wechat.robot.condition.ConditionFriendContacInfo;
import com.k.wechat.robot.condition.ConditionGroupMore;
import com.k.wechat.robot.condition.ConditionMain;
import com.k.wechat.robot.condition.ConditionPhoneLogin;
import com.k.wechat.robot.condition.ConditionRegistered;
import com.k.wechat.robot.condition.ConditionRoomMember;
import com.k.wechat.robot.condition.ConditionSendAddNews;

/**
 * 微信版本 默认6.7.3
 */
public class WechatUI {

    public static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";

    //ID
    public static final String BASE_ID = "com.tencent.mm:id/";

    public static String ID_CLICK_SEARCH_BOX;// 主界面搜索按钮所在标题栏
    public static String ID_MAIN_ADDRESSBOOK;//主界下方菜单
    public static String ID_ALL_LABLE;//标签的ListView
    public static String ID_SON_LABLE;//标签的子标签LinearLayout
    public static String ID_INPUT_TEXT_BOX;//群发输入数据
    public static String ID_INPUT_TEXT_SEND_KEYS;//群发输入数据 发送按钮
    public static String ID_GROUP_OVER_RETURN;//群发消息——返回按钮
    public static String ID_ADDRESS_BOOK_LIST_VIEW;//通讯录模块的listview
    public static String ID_TRUE_DELETE_FRIEND;//群组删除好友弹出框确认按钮
    public static String ID_FROM_BOOK_FRIENG;//通讯录好友的id
    public static String ID_DOWN_MORE_FROM_FRIEND;//好友聊天界面下方的更多按钮
    public static String ID_DOWN_MORE_LINEAR_LAYOUT;//好友聊天界面下方的更多展开之后的LinearLayout
    public static String ID_CARD_SEARCH_FRIEND_BACK_PUS;//搜索名片后，选择联系人界面的返回按钮
    public static String ID_FRIEND_CHAT_LINEAR_LAYOUT;//好友聊天界面的LinearLayout
    public static String ID_SEND_KEYS_NEWS;//好友，聊天内容的ID
    public static String ID_MESSAGE_LIST_ITEM_AVATER;//好友，聊天内容头像
    public static String ID_SEARCH_FRIEND_TEXT;//主页面搜索框，输入的字体
    public static String ID_HEAD_PORTRAIT_FROM_CHAT_MESSAGE;//聊天信息界面的LinearLayout
    public static String ID_GROUP_SEE_ALL_MEMBER_GRIDVIEW;//详细资料界面好友微信号
    public static String ID_RESEND_PUSH;//重发
    public static String ID_FIREND_DETAIL_LABEL_TEXT;//好友详情 标签内容
    public static String ID_SEE_ALL_GROUP_MEMBER_ITEM_NAME;//查看全部群组成员界面 每个人的名称
    public static String ID_PRESS_SPEAK;//按住说话
    public static String ID_SWITCH_KEYBOARD;//切换到键盘
    public static String ID_PUBLIC_ACCOUNTS_NO_ATTENTION;//点击不再关注
    public static String ID_CONVERSATION_MORE;//更多
    public static String ID_CONVERSATION_DETAIL_PP_LIST;//聊天信息成员列表
    public static String ID_PP_DETAIL_ADD2CONTACTS;//pp详细资料 添加到通讯录按钮
    public static String ID_ADD_FIREND_VERIFY_MESSAGE;//添加好友验证 请求信息
    public static String ID_ADD_FIREND_VERIFY_REMARK;//添加好友验证 备注
    public static String ID_ADD_FIREND_VERIFY_SEND;//添加好友验证 发送按钮
    public static String ID_CONVERSATION_DETAIL_CONTACT_LIST_ITEM_NAME;//群聊详情每个好友的昵称
    public static String ID_CONTACT_INFO_NAME;//好友详情 名称
    public static String ID_CONTACT_INFO_GENDER;//联系人详情 性别
    public static String ID_ADD_FIREND_VERIFY_LABEL;//添加好友验证 标签
    public static String ID_ADD_FIREND_VERIFY_LABEL_CONTAINER;//添加好友验证 标签已经存在的情况下的container
    public static String ID_SNS_CAMERA_BTN;//朋友圈照相按钮

    public static String ID_ANDROID_LIST_VIEW = "android:id/list";//listview通用id
    public static String ID_CONTACT_SEND_INFO;//好友详情 发送消息按钮
    public static String ID_FRIEND_TIME = "android:id/text1";//好友朋友圈图片详情日期
    public static String ID_ACTIVITY_TITLE_BAR_TITLE = "android:id/text1";//微信activitytitlebar标题

    public static String ID_LABELS_COUNT;//标签名字后面的数字
    public static String ID_CHATTING_UI_TITLE;//聊天页面标题
    public static String ID_HOME_MAIN_BOTTOM;//主页底部四个控件的父亲
    public static String ID_HOME_MAIN_BOTTOM_TEXT;//主页底部四个控件的字体，微信，通讯录
    public static String ID_ADD_FRIEND_TEXT;//申请添加朋友字体
    public static String ID_PHONE_LOADING;//手机号登陆字体
    public static String ID_MORE_LOADING;//用微信号/QQ号/邮箱登录字体
    public static String ID_REGISTERED_TEXT;//注册字体
    public static String ID_PHONE_REGISTERED_TEXT;//注册界面手机号注册字体


//    ************************************************************************************************
//    ************************************************************************************************

    //android class

    public static final String CLASS_NAME_EDITVIEW = "android.widget.EditText";//编辑框
    public static final String CLASS_NAME_LIST_VIEW = "android.widget.ListView";//ListView
    public static final String CLASS_NAME_TOAST = "android.widget.Toast$TN";
    public static final String CLASS_NAME_FRAMELAYOUT = "android.widget.FrameLayout";//添加好友 +号弹出框
    public static final String CLASS_NAME_LINEARLAYOUT = "android.widget.LinearLayout";

    //wechat UI
    @UI_CONDITION(ConditionMain.class)
    public static final String UI_LUANCHER = WECHAT_PACKAGE_NAME + ".ui.LauncherUI";//微信主界面

    @UI_CONDITION(ConditionChatting.class)
    public static final String UI_CHATTING_UI = WECHAT_PACKAGE_NAME + ".ui.chatting.ChattingUI";//群聊界面

    @UI_CONDITION(ConditionFriendContacInfo.class)
    public static final String UI_CONTACT_INFO = WECHAT_PACKAGE_NAME + ".plugin.profile.ui.ContactInfoUI";//客户详情界面

    @UI_TITLE("所有标签")
    public static final String UI_LABLE_PAGE = WECHAT_PACKAGE_NAME + ".plugin.label.ui.ContactLabelManagerUI";//标签页面

    @UI_CONDITION_NONE
    public static final String UI_COLLECT_CONTENT_POPUP_WINDOW = WECHAT_PACKAGE_NAME + ".ui.widget.a.c";//发送收藏内容的弹出窗口

    @UI_CONDITION_NONE
    public static final String UI_COLLECT_CONTENT_POPUP_WINDOW705 = WECHAT_PACKAGE_NAME + ".ui.widget.b.c";//发送收藏内容的弹出窗口

    @UI_CONDITION_NONE
    public static final String UI_COLLECT_CONTENT_POPUP_WINDOW707 = WECHAT_PACKAGE_NAME + ".ui.widget.b.d";//发送收藏内容的弹出窗口

    @UI_CONDITION_NONE
    public static final String UI_COLLECT_CONTENT_POPUP_WINDOW7012 = WECHAT_PACKAGE_NAME + ".ui.widget.a.d";//发送收藏内容的弹出窗口


    @UI_TITLE("添加标签")
    public static final String UI_CONTACT_LABLE = WECHAT_PACKAGE_NAME + ".plugin.label.ui.ContactLabelUI";//添加标签界面

    @UI_CONDITION_NONE
    public static final String UI_WECHAT_NORMAL_LOADING = WECHAT_PACKAGE_NAME + ".ui.base.p";//微信常规黑底左边转圈右边文案loading...


    @UI_TITLE("编辑标签")
    public static final String UI_LABLE_FRIEND_NAME = WECHAT_PACKAGE_NAME + ".plugin.label.ui.ContactLabelEditUI";//标签好友，编辑标签界面

    @UI_CONDITION_NONE
    public static final String UI_WORLD = WECHAT_PACKAGE_NAME + ".app.WeChatSplashActivity";//微信开始地球界面

    @UI_CONDITION_NONE
    public static final String UI_LANDING = WECHAT_PACKAGE_NAME + ".plugin.account.ui.LoginPasswordUI";//登陆界面

    @UI_CONDITION_NONE
    public static final String UI_SMS_LANDING = WECHAT_PACKAGE_NAME + ".plugin.account.ui.LoginSmsUI";//手机号验证码登陆界面

    @UI_TITLE("找回帐号密码")
    public static final String UI_FORGET_PASSWORD = WECHAT_PACKAGE_NAME + ".plugin.webview.ui.tools.WebViewUI";//找回密码界面

    @UI_CONDITION_NONE
    public static final String UI_WECHAT_REST = WECHAT_PACKAGE_NAME + ".plugin.account.ui.WelcomeActivity";//第一次进入微信的界面

    @UI_CONDITION_NONE
    public static final String UI_LOGIN = WECHAT_PACKAGE_NAME + ".plugin.account.ui.LoginUI";//点击使用微信号手机号登陆后的界面


    @UI_CONDITION(ConditionSendAddNews.class)
    public static final String UI_ADD_FRIEND_VERIFY_APPLY = WECHAT_PACKAGE_NAME + ".plugin.profile.ui.SayHiWithSnsPermissionUI";

    @UI_CONDITION(ConditionGroupMore.class)
    public static final String UI_CHATROOM = WECHAT_PACKAGE_NAME + ".chatroom.ui.ChatroomInfoUI";


    @UI_CONDITION(ConditionRoomMember.class)
    public static final String UI_SEE_ALL_GROUP_MEMBER = WECHAT_PACKAGE_NAME + ".chatroom.ui.SeeRoomMemberUI";//查看全部群成员界面

    @UI_CONDITION(ConditionPhoneLogin.class)
    public static final String UI_PHONE_LANDING = WECHAT_PACKAGE_NAME + ".plugin.account.ui.MobileInputUI";//手机号登陆界面

    @UI_CONDITION(ConditionRegistered.class)
    public static final String UI_REGISTERED = WECHAT_PACKAGE_NAME + ".plugin.account.ui.RegByMobileRegAIOUI";//手机号注册界面


//    **********************************************************************************************
//    ************************************************************************************************


    //wechat Text
    public static final String TEXT_GROUP_DETAIL_SCAN_ALL_MEM = "查看全部群成员";
    public static final String TEXT_NEW_PICTURE_REMIND = "你可能要发送的照片：";
    public static final String TEXT_WECHAT_TEAM = "微信团队";
    public static final String TEXT_LABEL = "标签";

}
