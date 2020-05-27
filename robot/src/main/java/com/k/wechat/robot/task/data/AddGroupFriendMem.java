package com.k.wechat.robot.task.data;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;


import com.k.wechat.robot.LibInstance;
import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.util.L;
import com.k.wechat.robot.util.UiKit;
import com.k.wechat.robot.util.WeChatHelper;
import com.k.wechat.robot.util.WechatUI;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.k.wechat.robot.util.WechatUI.ID_CONVERSATION_DETAIL_CONTACT_LIST_ITEM_NAME;
import static com.k.wechat.robot.util.WechatUI.ID_CONVERSATION_DETAIL_PP_LIST;


public class AddGroupFriendMem {
    public static LinkedList<String> mQueue = new LinkedList<>();
    public static Set<String> mSuccessNames = new HashSet<>();
    public static Set<String> mSuccessVerifyNames = new HashSet<>();
    private static String mCurrent;
    private static String mMessage;
    private static String mGenderCondition;
    private static int mStartIndex;
    private static boolean mFlag;
    private static boolean mSearchOver;
    private static boolean mUseChattingUi;
    private static String mPrefixStr;
    private static String mLabel;
    private static int mTime;
    private static String mNote;

    public static String getmNote() {
        return mNote;
    }

    public static int getmTime() {
        return mTime;
    }

    public static String getFirstItem() {
        return mCurrent = mQueue.poll();
    }

    public static String current() {
        return mCurrent;
    }

    public static void add(String name) {
        mQueue.add(name);
    }

    public static void add(int index, String name) {
        mQueue.add(index, name);
    }

    public static String message() {
        return mMessage;
    }

    public static String remarkPrefix() {
        return mPrefixStr;
    }

    public static String label() {
        return mLabel;
    }

    public static String genderCondition() {
        return mGenderCondition;
    }

    public static boolean isAvalible() {
        return mCurrent != null;
    }


    public static boolean flag() {
        return mFlag;
    }

    public static void setFlag(boolean flag) {
        mFlag = flag;
    }

    public static int startIndex() {
        return mStartIndex;
    }

    public static void currentSuccess() {
        if (!TextUtils.isEmpty(mCurrent)) {
            mSuccessNames.add(mCurrent);
        }
        mCurrent = null;
    }

    public static void currentSendVerifySuccess() {
        if (!TextUtils.isEmpty(mCurrent)) {
            mSuccessVerifyNames.add(mCurrent);
        }
        currentSuccess();
    }

    public static void reset(String message, String note, String genderSelectResult, int startIndex, int t) {//boolean b
        if (mQueue == null) {

            mQueue = new LinkedList<>();
        }
        mQueue.clear();
        mSuccessNames.clear();
        mSuccessVerifyNames.clear();
        mCurrent = null;
        mSearchOver = false;
        mMessage = message;
        mStartIndex = startIndex;
        mGenderCondition = genderSelectResult;
        mFlag = false;
        mUseChattingUi = true;
//        mPrefixStr = null;
        mLabel = null;
//        if (b) {
//            mPrefixStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
//        }
        mTime = t;
        mNote = note;
    }

    public static void reset(String remarkPrefix, String label) {
        if (mQueue == null) {

            mQueue = new LinkedList<>();
        }
        mQueue.clear();
        mSuccessNames.clear();
        mSuccessVerifyNames.clear();
        mCurrent = null;
        mSearchOver = false;
        mMessage = null;
        mStartIndex = 0;
        mGenderCondition = null;
        mFlag = false;
        mUseChattingUi = true;
        mPrefixStr = remarkPrefix;
        mLabel = label;
    }

    /**
     * 有查看全部群成员的text 不使用这个页面去发送 改用查看全部群成员的页面
     */
    public static void dontUseChattingUi() {
        mQueue.clear();
        mUseChattingUi = false;
    }

    public static boolean useChattingUi() {
        return mUseChattingUi;
    }


    public static boolean isQueueEmpty() {
        return mQueue.isEmpty();
    }

    public static boolean existElement(String name) {
        return mQueue.contains(name);
    }

    public static void searchOver() {
        mSearchOver = true;
    }

    public static void searchNotOver() {
        mSearchOver = false;
    }

    public static boolean hasSearchOver() {
        return mSearchOver;
    }


    public static String getTaskCompleteMessage() {
        return "成功操作" + mSuccessNames.size() + "个联系人" + ",其中发送好友验证消息" + mSuccessVerifyNames.size() + "个。";
    }

    public static void taskComplete() {
        TaskId.stop(TaskId.TASK_ADD_GROUP_FRIEND, getTaskCompleteMessage());
    }

    public static void clickFriend2ContactInfo() {
        if (useChattingUi()) {
            if (AddGroupFriendMem.hasSearchOver()) {
                AccessibilityNodeInfo nodeListView = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ANDROID_LIST_VIEW);

                if (nodeListView != null) {
                    List<AccessibilityNodeInfo> contactRow = nodeListView.findAccessibilityNodeInfosByViewId(ID_CONVERSATION_DETAIL_PP_LIST);

                    for (int i = 0; i < contactRow.size(); i++) {
                        //每一行 5个
                        AccessibilityNodeInfo child = contactRow.get(i);
                        for (int i1 = 0; i1 < child.getChildCount(); i1++) {
                            UiKit.sleep();
                            //每一个人
                            AccessibilityNodeInfo child1 = child.getChild(i1);
                            AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(child1, ID_CONVERSATION_DETAIL_CONTACT_LIST_ITEM_NAME);
                            if (nodeInfosById != null && !TextUtils.isEmpty(nodeInfosById.getText())) {
                                String name = nodeInfosById.getText().toString();
                                L.e("name：" + name);
                                if (!mQueue.contains(name)) {
                                    L.e("不存在的name: " + name);
                                    mQueue.add(name);
                                    //709修改备注后，群里姓名就会变化，会导致获取到修改的姓名
                                    if (WeChatHelper.above709(LibInstance.getInstance().getTaskListener().provideContext())) {
                                        mQueue.add(getmNote() + name);
                                    }
                                    mCurrent = name;
                                    UiKit.sleep(mTime * 1000);
                                    AccessibilityHelper.performClick(nodeInfosById);
                                    return;
                                }
                            }
                        }
                    }
                    if (TaskId.get().mCurrentId > 0 && AccessibilityHelper.perform_scroll_forward(nodeListView)) {
                        UiKit.sleep(1000);
                        clickFriend2ContactInfo();
                    } else if (mCurrent == null) {
                        taskComplete();
                    }
                }
            }
        }
    }

    public static void clickFriend2ContactInfoInAllMember() {
        if (AddGroupFriendMem.hasSearchOver()) {
            AccessibilityNodeInfo nodeGridView = AccessibilityHelper.findNodeInfosById(WechatUI.ID_GROUP_SEE_ALL_MEMBER_GRIDVIEW);

            if (nodeGridView != null) {
                List<AccessibilityNodeInfo> nodeInfoItem = AccessibilityHelper.findNodeListInfosById(WechatUI.ID_SEE_ALL_GROUP_MEMBER_ITEM_NAME);

                for (int i1 = 0; i1 < nodeInfoItem.size(); i1++) {
                    //每一个人
                    AccessibilityNodeInfo nodeInfosById = nodeInfoItem.get(i1);
                    if (nodeInfosById != null && !TextUtils.isEmpty(nodeInfosById.getText())) {
                        String name = nodeInfosById.getText().toString();
                        if (!mQueue.contains(name)) {
                            mQueue.add(name);
                            mCurrent = name;
                            UiKit.sleep(mTime * 1000);
                            AccessibilityHelper.performClick(nodeInfosById);
                            return;
                        }
                    }
                }

                if (TaskId.get().mCurrentId > 0 && AccessibilityHelper.perform_scroll_forward(nodeGridView)) {
                    UiKit.sleep(1000);
                    clickFriend2ContactInfoInAllMember();
                } else if (mCurrent == null) {
                    taskComplete();
                }
            }
        }
    }

    /**
     * 单纯的滑倒最顶部
     *
     * @param nodeListView
     */
    public static void scroll2Top(AccessibilityNodeInfo nodeListView) {
        if (AccessibilityHelper.perform_scroll_backward(nodeListView)) {
            UiKit.sleep();
            scroll2Top(nodeListView);
        }
    }
}
