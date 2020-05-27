package com.k.wechat.robot.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.k.wechat.robot.LibInstance;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WeChatHelper {

    public static boolean hasInt;

    public static Map<String, String> wechatAssetsName = new HashMap<>();

    private WeChatHelper() {

    }

    private static final WeChatHelper instance = new WeChatHelper();

    public static WeChatHelper init() {
        return instance;
    }

    public static String getFromXml(String xmlmsg, String node) throws XmlPullParserException, IOException {
        String xl = xmlmsg.substring(xmlmsg.indexOf("<msg>"));
        //nativeurl
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser pz = factory.newPullParser();
        pz.setInput(new StringReader(xl));
        int eventType = pz.getEventType();
        String result = "";
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (pz.getName().equals(node)) {
                    pz.nextToken();
                    result = pz.getText();
                    break;
                }
            }
            eventType = pz.next();
        }
        return result;
    }

    public static int getRandom(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }

    public static void openWechat(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName(WechatUI.WECHAT_PACKAGE_NAME, WechatUI.UI_LUANCHER);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasInstallWechat() {
        List<PackageInfo> installedPackages = LibInstance.getInstance().getTaskListener().provideContext().getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);
        for (PackageInfo info : installedPackages) {
            if (WechatUI.WECHAT_PACKAGE_NAME.equals(info.packageName)) {

                return true;
            }
        }
        return false;

    }

    public static boolean isWechat667(Context context) {
        return "6.6.7".equals(getWechatVersionName(context));
    }

    public static boolean isWechat673(Context context) {
        return "6.7.3".equals(getWechatVersionName(context));
    }

    public static boolean above700(Context context) {
        return getWechatVersion(context) >= 1380;
    }

    public static boolean above705(Context context) {
        return getWechatVersion(context) >= 1440;
    }

    public static boolean small705(Context context) {
        return getWechatVersion(context) < 1440;
    }

    public static boolean above706(Context context) {
        return getWechatVersion(context) >= 1460;
    }

    public static boolean above707(Context context) {
        return getWechatVersion(context) >= 1500;
    }

    public static boolean above708(Context context) {
        return getWechatVersion(context) >= 1521;
    }

    public static boolean above709(Context context) {
        return getWechatVersion(context) >= 1560;
    }

    public static boolean above7010(Context context) {
        return getWechatVersion(context) >= 1580;
    }

    public static boolean above7011(Context context) {
        return getWechatVersion(context) >= 1600;
    }

    public static boolean above7012(Context context) {
        return getWechatVersion(context) >= 1620;
    }

    public static boolean above7013(Context context) {
        return getWechatVersion(context) >= 1640;
    }

    public static boolean above7014(Context context) {
        return getWechatVersion(context) >= 1660;
    }

    /**
     * 获取微信的版本
     */
    public static int getWechatVersion(Context context) {
        PackageInfo mWechatPackageInfo = getPackageInfo(context);
        if (mWechatPackageInfo == null) {
            return 0;
        }
        return mWechatPackageInfo.versionCode;
    }

    /**
     * 获取微信的版本名称
     */
    public static String getWechatVersionName(Context context) {
        PackageInfo mWechatPackageInfo = getPackageInfo(context);
        if (mWechatPackageInfo == null) {
            return "";
        }
        return mWechatPackageInfo.versionName;
    }


    public static boolean above700() {
        return getWechatVersion(LibInstance.getInstance().getTaskListener().provideContext()) >= 1380;
    }

    public static boolean above673() {
        return getWechatVersion(LibInstance.getInstance().getTaskListener().provideContext()) >= 1360;
    }

    /**
     * 更新微信包信息
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo mWechatPackageInfo = null;
        try {
            mWechatPackageInfo = context.getPackageManager().getPackageInfo(WechatUI.WECHAT_PACKAGE_NAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mWechatPackageInfo;
    }


    public static boolean initIdConfig(Context context, String jsonFileName) {
        L.e("使用微信版本 ： " + jsonFileName);
        try {
            InputStream is = context.getResources().getAssets().open(jsonFileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int data;
            while ((data = is.read()) != -1) {
                bos.write(data);
            }
            is.close();
            String json = bos.toString();
            JSONObject jsonObject = new JSONObject(json);
            JSONObject ids = (JSONObject) jsonObject.get("ids");
            Iterator<String> keys = ids.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String id = WechatUI.BASE_ID + ids.get(key);
                try {
                    WechatUI.class.getDeclaredField(key).set(WechatUI.class, id);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            return hasInt = true;
        } catch (IOException | JSONException | IllegalAccessException e) {
            L.e(e.toString());
            throw new IllegalArgumentException("ids json解析失败", e);
        }
    }

    /**
     * 获取assets目录下满足微信版本的 viewPagerId-assetsName 的Map
     *
     * @param context
     */
    public static void initByWechatVersionInApp(Context context) {
        resetWechatIdConfig(context);
        if (WeChatHelper.wechatAssetsName.size() == 1) {
            //如果asset下只有一个相同版本的json文件 那么默认用这个
            Iterator<String> iterator = WeChatHelper.wechatAssetsName.values().iterator();
            if (iterator.hasNext()) {
                String wechatConfig = iterator.next();
                WeChatHelper.initIdConfig(context, wechatConfig);
            }
        }

    }

    public static void resetWechatIdConfig(Context context) {
        hasInt = false;
        wechatAssetsName.clear();
        String wechatVersionName = getWechatVersionName(context);
        String versonNumStr = wechatVersionName.replace(".", "");

        try {
            String[] list = context.getResources().getAssets().list("");

            for (String assetsName : list) {
                if (assetsName.contains(versonNumStr)) {

                    InputStream is = context.getResources().getAssets().open(assetsName);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int data;
                    while ((data = is.read()) != -1) {
                        bos.write(data);
                    }
                    is.close();
                    String json = bos.toString();
                    JSONObject jsonObject = new JSONObject(json);
                    String id = WechatUI.BASE_ID + jsonObject.get("WECHAT_MAIN_VIEWPAGER_ID");
                    wechatAssetsName.put(id, assetsName);
                    L.e(assetsName);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}
