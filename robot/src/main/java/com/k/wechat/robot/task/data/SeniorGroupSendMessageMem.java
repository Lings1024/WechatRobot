package com.k.wechat.robot.task.data;

import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.accessibility.AccessibilityHelper;
import com.k.wechat.robot.task.TaskId;
import com.k.wechat.robot.util.UiKit;
import com.k.wechat.robot.util.WechatUI;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


public class SeniorGroupSendMessageMem {
    public static LinkedList<String> mQueue = new LinkedList<>();
    private static String mCurrent;
    private static String mMessage;
    private static int mCounter = 0;
    private static int mHandleCount = 0;
    private static boolean mFlag;//备用flag
    private static boolean mSearchOver;
    private static String mNeedExcludedLabels;//需要屏蔽的好友标签
    private static int mStartIndex;
    private static String mNameText;//亲密昵称
    private static boolean mPhotoPath;//照片路径
    private static boolean sendPhoto;//照片路径
    public static boolean mFlag2;//亲密昵称开关
    private static String labelType;//标签选择 0全部 1只选择 2屏蔽
    private static int mTime;
    private static String mNote;//亲密昵称 0发送 1不发 2特殊格式
    private static String mLeaveMessage;//留言
    private static String taskType;//0发送好友 1发送群组
    public static LinkedList<String> mLabelname = new LinkedList<>();//好友姓名集合
    private static int mNumber;
    private static int lastNumber;//上次备份的位置
    private static boolean overCopy;
    private static boolean isDelete;
    private static String mWay;//0朋友圈查死粉1查屏蔽

    private static boolean sendNews;//是否发送消息

    public static boolean isSendNews() {
        return sendNews;
    }

    public static void setSendNews(boolean sendNews) {
        SeniorGroupSendMessageMem.sendNews = sendNews;
    }

    public static boolean isDelete() {
        return isDelete;
    }

    public static String getmWay() {
        return mWay;
    }

    public static boolean isOverCopy() {
        return overCopy;
    }

    public static void setOverCopy(boolean overCopy) {
        SeniorGroupSendMessageMem.overCopy = overCopy;
    }

    public static int getLastNumber() {
        return lastNumber;
    }

    public static void minusLastNumber() {
        lastNumber--;
    }

    public static int getmNumber() {
        return mNumber;
    }

    public static void addNumber() {
        mNumber++;
    }

    public static LinkedList<String> getmLabelname() {
        return mLabelname;
    }

    public static String getFirstName() {
        return mLabelname.removeFirst();
    }

    public static String getTaskType() {
        return taskType;
    }

    public static String getmLeaveMessage() {
        return mLeaveMessage;
    }

    public static String getmNote() {
        return mNote;
    }

    public static int getmTime() {
        return mTime;
    }

    public static String getLabelType() {
        return labelType;
    }

    public static boolean isSendPhoto() {
        return sendPhoto;
    }

    public static void setSendPhoto(boolean sendPhoto) {
        SeniorGroupSendMessageMem.sendPhoto = sendPhoto;
    }


    public static boolean ismPhotoPath() {
        return mPhotoPath;
    }

    public static void setmPhotoPath(boolean mPhotoPath) {
        SeniorGroupSendMessageMem.mPhotoPath = mPhotoPath;
    }

    public static boolean ismFlag2() {
        return mFlag2;
    }

    public static void setmFlag2(boolean mFlag2) {
        SeniorGroupSendMessageMem.mFlag2 = mFlag2;
    }


    public static String getmNameText() {
        return mNameText;
    }

    public static void setmNameText(String mNameText) {
        SeniorGroupSendMessageMem.mNameText = mNameText;
    }

    public static boolean getItemFinishFlag(String item) {
        return !mQueue.contains(item);
    }

    public static String current() {

        return mCurrent;
    }

    public static void setmCurrent(String name) {
        mCurrent = name;

    }

    public static void setMessage(String s) {
        mMessage = s;
    }

    public static void add(String name) {
        mQueue.add(name);
    }

    public static int getHandleCount() {
        return mHandleCount;
    }

    public static void add(int index, String name) {
        mQueue.add(index, name);
    }

    public static String message() {
        return mMessage;
    }

    public static String getNeedExcludedLabels() {
        return mNeedExcludedLabels;
    }

    public static boolean isAvalible() {
        return mCurrent != null;
    }

    public static void currentSuccess() {
        mCurrent = null;
    }

    public static void counterAdd() {
        mCounter++;
    }

    public static int getCounter() {
        return mCounter;
    }

    public static void reset(String message) {
        if (mQueue == null) {

            mQueue = new LinkedList<>();
        }
        mQueue.clear();
        mCounter = 0;
        mHandleCount = 0;
        mCurrent = null;
        mSearchOver = false;
        mFlag = false;
        mMessage = message;
        mNeedExcludedLabels = null;
        mStartIndex = 0;
    }

    public static void reset(String message, int number) {
        if (mQueue == null) {

            mQueue = new LinkedList<>();
        }
        mQueue.clear();
        mCounter = 0;
        mHandleCount = 0;
        mCurrent = null;
        mSearchOver = false;
        mFlag = false;
        mMessage = message;
        mNeedExcludedLabels = null;
        mStartIndex = 0;
        mNumber = number;
        lastNumber = number;
        overCopy = false;
    }

    public static void reset(String message, String needExcludedLabels) {
        reset(message, needExcludedLabels, 0);
    }

    public static void reset(String message, String needExcludedLabels, String laType) {
        reset(message, needExcludedLabels, 0);//needExcludedLabels 选择的标签名字
        labelType = laType;
    }

    public static void reset(String message, String needExcludedLabels, int startIndex) {
        reset(message);
        mNeedExcludedLabels = needExcludedLabels;
        mStartIndex = startIndex;
        mNameText = "";
//        mFlag2 = false;
    }

    public static void reset(String message, String needExcludedLabels, int startIndex, String lType, int t, String leaveMessage, String labelName) {
        reset(message);
        mNeedExcludedLabels = needExcludedLabels;
        mStartIndex = startIndex;
        mNameText = "";
        labelType = lType;
//        mFlag2 = false;
        mTime = t;
        mLeaveMessage = leaveMessage;
        if (mLabelname == null) {
            mLabelname = new LinkedList<>();
        }
        mLabelname.clear();
        if (!TextUtils.isEmpty(labelName)) {
            mLabelname.addAll(getName(labelName));
        }
    }

    public static void reset(String message, String needExcludedLabels, int startIndex, String lType, int t, String leaveMessage, String note, String labelName) {
        reset(message);
        mNeedExcludedLabels = needExcludedLabels;
        mStartIndex = startIndex;
        mNameText = "";
        labelType = lType;
        mTime = t;
        mNote = note;
        mLeaveMessage = leaveMessage;
        if (mLabelname == null) {
            mLabelname = new LinkedList<>();
        }
        mLabelname.clear();
        if (!TextUtils.isEmpty(labelName)) {
            mLabelname.addAll(getName(labelName));
        }
    }

    public static void reset(String way, boolean delete, String message, String needExcludedLabels) {
        if (mQueue == null) {

            mQueue = new LinkedList<>();
        }
        mQueue.clear();
        mWay = way;
        isDelete = delete;
        mMessage = message;
        mNeedExcludedLabels = needExcludedLabels;
        mFlag = false;
        sendNews = false;
        mSearchOver = false;
        mCurrent = null;
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

    public static boolean hasSearchOver() {
        return mSearchOver;
    }

    public static boolean flag() {
        return mFlag;
    }

    public static void setFlag(boolean flag) {
        mFlag = flag;
    }

    public static void clickFriend2SendMessage() {

        AccessibilityNodeInfo nodeInfosById = AccessibilityHelper.findNodeInfosById(WechatUI.ID_ADDRESS_BOOK_LIST_VIEW);

        if (nodeInfosById != null) {
            for (int i = 0; i < nodeInfosById.getChildCount(); i++) {
                AccessibilityNodeInfo child = nodeInfosById.getChild(i);
                AccessibilityNodeInfo nodeInfosById1 = AccessibilityHelper.findNodeInfosById(child, WechatUI.ID_FROM_BOOK_FRIENG);
                if (nodeInfosById1 != null) {
                    String name = nodeInfosById1.getText().toString();
                    if (!mQueue.contains(name) && !WechatUI.TEXT_WECHAT_TEAM.equals(name)) {
                        mQueue.add(name);
                        if (TaskId.assertTaskId(TaskId.TASK_SENIOR_MASS_SEND_FRIENDS)) {
                            if (!TextUtils.isEmpty(mNote) && ("0".equals(mNote) || "2".equals(mNote))) {
                                setmNameText(name);
                            }

                            mCurrent = name;
                            mHandleCount++;
                            AccessibilityHelper.performClick(nodeInfosById1);
                            return;
                        } else {
                            if (mStartIndex <= 0 || mQueue.size() - 1 >= mStartIndex) {
                                if (!TextUtils.isEmpty(mNote) && ("0".equals(mNote) || "2".equals(mNote))) {
                                    setmNameText(name);
                                }

                                mCurrent = name;
                                mHandleCount++;
                                AccessibilityHelper.performClick(nodeInfosById1);
                                return;
                            }
                        }
                    }
                }
            }
            if (TaskId.get().mCurrentId > 0 && AccessibilityHelper.perform_scroll_forward(nodeInfosById)) {
                //如果能滑动 ，添加进去 继续尝试寻找     如果滑动到底了 ,还没找到 放弃， 重新滑倒最上面执行下一个人的寻找
                UiKit.sleep();
                clickFriend2SendMessage();
            } else if (mCurrent == null) {
                TaskId.stop();
            }
        }
    }

    /**
     * 滑倒最顶部
     *
     * @param nodeListView
     */
    public static void scroll2Top(AccessibilityNodeInfo nodeListView) {
        if (TaskId.get().mCurrentId > 0 && AccessibilityHelper.perform_scroll_backward(nodeListView)) {
            UiKit.sleep();
            scroll2Top(nodeListView);
        }
    }

    public static void setRestMessage(String message, int time, String note, String needExcludLabels, String lab, String type) {
        //"", needExcludLabels, 0, labelType, time, message, note
        reset("");
        mNeedExcludedLabels = needExcludLabels;
        mStartIndex = 0;
        mNameText = "";
        labelType = lab;
        mTime = time;
        mNote = note;
        mLeaveMessage = message;
        taskType = type;
    }

    public static void setGroupRest(String message, String type) {
        reset("");
        mNeedExcludedLabels = null;
        mStartIndex = 0;
        mNameText = "";
        labelType = null;
//        mFlag2 = false;
        mTime = 0;
        mLeaveMessage = message;
        taskType = type;
    }

    /**
     * 获取需要发送的人员姓名并且去重
     *
     * @param label
     */
    public static Set<String> getName(String label) {
        return new HashSet<>(Arrays.asList(label.split(",")));
    }

}
