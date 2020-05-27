package com.k.wechat.robot.action;

import android.text.TextUtils;


import com.k.wechat.robot.util.L;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 打招呼和标签下的名字
 */
public class SayHelloMem {
    private static Set<String> mAddNames;//包含判断
    private static Map<String, Set<String>> map;//存放标签和人
    private static boolean mBack;//是不是需要返回和
    private static String lableName;//标签名字
    private static Set<String> mName;//标签下好友名字
    private static String mMassage;
    private static String chooseGender;//性别
    private static boolean mGender;//设置性别标签
    private static int mIndex;//开始
    private static String mNote;//备注内容
    private static boolean noteFlag;//是否备注过，true备注过，false没有备注
    private static String mLabels;//标签名称
    private static boolean labelFlag;//需不需要设置标签
    private static int mCount;//微信步数限制||标签好友总数
    private static Set<String> mTop;//步数排行
    private static int mTime;

    public static int getmTime() {
        return mTime;
    }

    public static boolean topContainsId(String top) {
        return mTop.contains(top);
    }

    public static void setTop(String top) {
        mTop.add(top);
    }

    public static boolean flag() {
        return labelFlag;
    }

    public static int getmCount() {
        return mCount;
    }

    public static void addmCount(int i) {
        mCount = i;
    }

    public static void setLabelFlag(boolean b) {
        labelFlag = b;
    }

    public static String labelsMassage() {
        return mLabels;
    }

    public static boolean isNoteFlag() {
        return noteFlag;
    }

    public static void putNoteText() {
        noteFlag = true;
    }

    public static void notNote() {
        noteFlag = false;
    }

    public static String note() {
        return mNote;
    }

    public static int index() {
        return mIndex;
    }

    public static void indexFalling() {
        mIndex--;
    }

    public static void indexAdd() {
        mIndex++;
    }

    public static boolean getGebderFlag() {
        return mGender;
    }

    public static void readyGender() {
        mGender = true;
    }

    public static String gender() {
        return chooseGender;
    }

    public static String massage() {
        return mMassage;
    }

    //key赋值
    public static void addLablesName(String name) {
        if (!TextUtils.isEmpty(name)) {
            lableName = name;
        }
    }

    public static void lableNameClear() {
        lableName = "";
        mName = new HashSet<>();
    }

    //value的list赋值
    public static void addListName(String name) {
        mName.add(name);
    }

    public static String getLable() {
        return lableName;
    }

    public static Map<String, Set<String>> map() {
        return map;
    }

    public static void addKeyValue() {
        if (!TextUtils.isEmpty(lableName)) {
            map.put(lableName, mName);
        }
    }

    public static Set<String> getListname() {
        return mName;
    }

    public static boolean keyContains(String name) {
        return map.containsKey(name);
    }

    public static void add(String name) {
        mAddNames.add(name);
    }

    public static boolean isContainsId(String name) {
        return mAddNames.contains(name);
    }

    public static boolean ismBack() {
        return mBack;
    }

    public static void reayBack() {
        mBack = true;
    }

    public static void noyBack() {
        mBack = false;
    }

    public static void rest(String massage, String gender, int index, String noteName, String labels) {
        mAddNames = new HashSet<>();
        mBack = false;
        mMassage = massage;
        chooseGender = gender;
        mGender = false;
        mIndex = index;
        mNote = noteName;
//        noteFlag = false;
        mLabels = labels;
        labelFlag = false;
    }

    public static void rest(String massage, String gender, int index, String noteName, String labels, int t) {
        mAddNames = new HashSet<>();
        mBack = false;
        mMassage = massage;
        chooseGender = gender;
        mGender = false;
        mIndex = index;
        mNote = noteName;
//        noteFlag = false;
        mLabels = labels;
        labelFlag = false;
        mTime = t;
    }

    public static void rest() {
        mName = new HashSet<>();
        lableName = "";
        map = new HashMap<>();
    }

    public static void rest(int count) {
        mCount = count;
        mAddNames = new HashSet<>();
        mTop = new HashSet<>();
    }

    public static void indexAndName(int time) {//初始化
        mAddNames = new HashSet<>();
        mIndex = 0;
        noteFlag = false;
        mCount = 0;
        mBack = false;
        mGender = false;//多选单选的判断
        mTime = time;
    }

//    public static int timeDelay(int time) {
//
//    }
}
