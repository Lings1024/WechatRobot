package com.k.wechat.robot;

import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.WindowManager;


public class DpUtil {

    private static Point sPoint;

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    public static int dp2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public static int getScreenWidth(Context context) {
        if(sPoint == null) {
            sPoint = new Point();
            WindowManager wm = (WindowManager)context.getSystemService("window");
            wm.getDefaultDisplay().getSize(sPoint);
        }

        return sPoint.x;
    }

    public static int getScreenHeight(Context context) {
        if(sPoint == null) {
            sPoint = new Point();
            WindowManager wm = (WindowManager)context.getSystemService("window");
            wm.getDefaultDisplay().getSize(sPoint);
        }

        return sPoint.y;
    }
}
