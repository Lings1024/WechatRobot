package com.k.wechat.robot.util;

import com.k.wechat.robot.action.H;


public class UiKit {

    public static void sleep(long time) {
        try {
            H.getInstance().updateOpreateTimeForward(time);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 基础睡眠周期
     */
    private static int mBaseSleepDuration = 200;

    /**
     * 睡眠一个周期
     */
    public static void sleep() {
        try {
            H.getInstance().updateOpreateTimeForward(mBaseSleepDuration);
            Thread.sleep(mBaseSleepDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 睡眠2个周期
     */
    public static void sleepL() {
        try {
            H.getInstance().updateOpreateTimeForward(2 * mBaseSleepDuration);
            Thread.sleep(2 * mBaseSleepDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 睡眠5个周期
     */
    public static void sleepLL() {
        try {
            H.getInstance().updateOpreateTimeForward(5 * mBaseSleepDuration);
            Thread.sleep(5 * mBaseSleepDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
