package com.k.wechat.robot.task.data;

import android.text.TextUtils;

import com.k.wechat.robot.task.TaskId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class CommonListIteratMem {
    public static LinkedBlockingQueue<String> mQueue = new LinkedBlockingQueue<>();
    public static String mCurrent;
    public static String mCurrentLable;
    private static String mMessage;
    public static LinkedBlockingQueue<String> mLablesQueue = new LinkedBlockingQueue<>();
    private static int mSuccessCount = 0;
    private static boolean mAssetFlag;
    private static boolean mNeedSelectLabel;
    private static int mTaskId;
    private static int mStartIndex;
    private static int mEndIndex;
    public static ArrayList<String> mTempList = new ArrayList<>();
    public static boolean mTempFlag;
    public static int mBitFlag;
    private static int mTime;
    private static boolean mNameFlag;//是否添加备注
    private static String mName;//通讯录名字
    private static int mCurrentIndex;
    private static boolean mOverStart;//循环完之后的标示

    public static boolean ismOverStart() {
        return mOverStart;
    }

    public static void setmOverStart(boolean mOverStart) {
        CommonListIteratMem.mOverStart = mOverStart;
    }

    public static boolean getStart() {
        if (mCurrentIndex >= mStartIndex) {
            return true;
        } else {
            mCurrentIndex++;
        }
        return false;
    }

    public static void addCurrentIndex() {
        mCurrentIndex++;
    }

    public static int getmCurrentIndex() {
        return mCurrentIndex;
    }

    public static String getmName() {
        return mName;
    }

    public static void setmName(String mName) {
        CommonListIteratMem.mName = mName;
    }

    public static boolean isNameFlag() {
        return mNameFlag;
    }

    public static int getmTime() {
        return mTime;
    }


    public static boolean contains(String item) {
        return mQueue.contains(item);
    }

    public static String current() {
        mCurrent = mQueue.poll();
        return mCurrent;
    }

    public static LinkedBlockingQueue<String> getQueue() {

        return mQueue;
    }

    public static void setCurrent(String current) {
        mCurrent = current;
    }

    public static void addData2Queue(String data) {
        mQueue.add(data);
    }

    public static String message() {
        return mMessage;
    }

    public static String obainFirstlable() {
        return mCurrentLable = mLablesQueue.poll();
    }

    public static boolean needSelectLabel() {
        return mNeedSelectLabel;
    }

    public static boolean isAvalible() {
        return mCurrent != null;
    }

    public static int startIndex() {
        return mStartIndex;
    }

    public static int endIndex() {
        return mEndIndex;
    }

    public static void currentSuccess() {
        mSuccessCount++;
        mCurrent = null;
    }

    public static int successItemCount() {
        return mSuccessCount;
    }

    public static boolean assetIsAllSuccess() {
        if (TaskId.assertTaskId(mTaskId)) {
            if (mAssetFlag && mQueue.isEmpty()) {
                TaskId.stop(mTaskId);
                mTaskId = -1;
                mAssetFlag = false;
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    public static void reset(int taskId, List<String> cellphone, String message) {
        mQueue = new LinkedBlockingQueue<>();
        mQueue.addAll(cellphone);
        mSuccessCount = 0;
        mCurrent = null;
        mMessage = message;
        mAssetFlag = true;
        mTaskId = taskId;
        mStartIndex = 0;
    }

    public static void reset(int taskId, String message) {
        reset(taskId, mMessage = message, 0);
    }


    public static void reset(int taskId, String message, int startIndex, int t, boolean name) {
        reset(taskId, mMessage = message, startIndex);
        mTime = t;
        mNameFlag = name;
        mName = "";
        mCurrentIndex = 0;
        mOverStart = false;
    }

    public static void reset(int taskId, String message, int startIndex) {
        mQueue = new LinkedBlockingQueue<>();
        mTempList = new ArrayList<>();
        mTempFlag = false;
        mBitFlag = 0;
        mSuccessCount = 0;
        mCurrent = null;
        mCurrentLable = null;
        mMessage = message;
        mAssetFlag = true;
        mTaskId = taskId;
        mStartIndex = startIndex;
    }

    public static void reset(int taskId, String message, String lables, int startIndex) {
        reset(taskId, message, lables, startIndex, -1);
    }

    public static void reset(int taskId, String message, String lables, int startIndex, int endIndex) {
        reset(taskId, message, startIndex);
        mEndIndex = endIndex;
        mLablesQueue.clear();
        if (!TextUtils.isEmpty(lables)) {
            mLablesQueue.addAll(Arrays.asList(lables.split(",")));
            mNeedSelectLabel = mLablesQueue.size() > 0;
        } else {
            mNeedSelectLabel = false;
        }
    }

    public static void needSendPhoto() {
        mBitFlag = 1;
    }

    public static void reset(int taskId, String message, String lables) {
        reset(taskId, message, lables, 0);
    }
}
