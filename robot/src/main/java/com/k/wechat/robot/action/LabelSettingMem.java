package com.k.wechat.robot.action;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class LabelSettingMem {
    private static String mLabelName;//选择的标签
    private static String mLabelSelected;//0s所有 1只选择标签 2 屏蔽标签
    private static boolean mNeedLabel;//
    public static LinkedBlockingQueue<String> mLablesQueue = new LinkedBlockingQueue<>();
    private static boolean mNeedChoose;//要不要点击默认true
    private static List<String> labelLIst;
    private static List<String> sendName;//发送过的人，去重用的
    private static List<String> sendLabel;//发送或者过的标签

    public static boolean getLabelListSize() {

       return labelLIst.isEmpty();
    }

    public static boolean isSendLabel(String name) {
        return sendLabel.contains(name);
    }

    public static void labelListRemove(String s) {
        labelLIst.remove(s);
    }

    public static void addSendLabelName(String name) {
        sendLabel.add(name);
    }


    public static boolean isSendName(String name) {
        return sendName.contains(name);
    }

    public static int getSendNameListSize() {
        return sendName.size();
    }

    public static void addSendName(String name) {
        sendName.add(name);
    }

    public static String getmLabelName() {
        return mLabelName;
    }

    public static String getmLabelSelected() {
        return mLabelSelected;
    }

    public static void setmLabelSelected(String type) {
        mLabelSelected = type;
    }

    public static boolean ismNeedChoose() {
        return mNeedChoose;
    }

    public static void setmNeedChoose(boolean mNeedChoose) {
        LabelSettingMem.mNeedChoose = mNeedChoose;
    }

    public static boolean ismNeedLabel() {
        return mNeedLabel;
    }

    public static void setmNeedLabel(boolean mNeedLabel) {
        LabelSettingMem.mNeedLabel = mNeedLabel;
    }

    public static String fristLabel() {
        return mLablesQueue.poll();
    }

    public static void rest(String label, String labelSelected) {
        mNeedChoose = true;
        mLabelName = label;
        mLabelSelected = labelSelected;
        mLablesQueue.clear();
        if (!TextUtils.isEmpty(mLabelName)) {
            mLablesQueue.addAll(Arrays.asList(mLabelName.split(",")));
        }

    }

    public static void rest() {
        mNeedChoose = false;
        mLabelSelected = "0";//1图片 2视频
        mNeedLabel = false;
    }

    public static void rest(String label) {
        sendName = new ArrayList<>();
        sendLabel = new ArrayList<>();
        labelLIst = new ArrayList<>();
        String[] split = label.split(",");
        labelLIst.addAll(Arrays.asList(split));
    }

    public static void clearAlreadySendedNamesAndLables() {
        sendName.clear();
        labelLIst.clear();
        labelLIst.addAll(sendLabel);
        sendLabel.clear();
    }

    public static boolean containsLabelName(String name) {
        for (String s : labelLIst) {
            if (s.equals(name)) {
                return true;
            }
        }
        return false;
    }

}
