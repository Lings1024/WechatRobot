package com.k.wechat.robot.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;

import com.k.wechat.robot.action.WechatCurrentActivity;
import com.k.wechat.robot.util.WechatUI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



public class AccessibilityHelper {
    private static final String TAG = AccessibilityHelper.class.getSimpleName();

    public static AccessibilityService mService;

    /**
     * 判断辅助服务是否正在运行
     */
    public static boolean isServiceRunning(AccessibilityService service) {
        if (service == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();
        if (info == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if (i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if (!isConnect) {
            return false;
        }
        return true;
    }

    /**
     * 打开辅助服务的设置
     */
    public static void openAccessibilityServiceSettings(Activity context) {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 自动点击按钮
     *
     * @param event
     * @param nodeText 按钮文本
     */
    public static void handleEvent(AccessibilityEvent event, String nodeText) {
        List<AccessibilityNodeInfo> unintall_nodes = event.getSource().findAccessibilityNodeInfosByText(nodeText);
        if (unintall_nodes != null && !unintall_nodes.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < unintall_nodes.size(); i++) {
                node = unintall_nodes.get(i);
                if (node.getClassName().equals("android.widget.Button") && node.isEnabled()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }


    /**
     * 获取text
     */
    public static String getNodeText(String id) {
        List<AccessibilityNodeInfo> unintall_nodes = mService.getRootInActiveWindow().findAccessibilityNodeInfosByViewId(id);
        if (unintall_nodes != null && !unintall_nodes.isEmpty()) {
            return unintall_nodes.get(0).getText().toString().trim();
        }
        return null;
    }

    /**
     * 获取text
     */
    public static String getNodeText(AccessibilityNodeInfo nodeInfo, String id) {
        List<AccessibilityNodeInfo> unintall_nodes = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (unintall_nodes != null && !unintall_nodes.isEmpty()) {
            return unintall_nodes.get(0).getText().toString().trim();
        }
        return null;
    }


    private AccessibilityHelper() {
    }


    //通过id查找
    public static AccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo, String resId) {
        if (nodeInfo == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    //通过id查找
    public static AccessibilityNodeInfo findNodeInfosById(String resId) {
        AccessibilityNodeInfo rootInActiveWindow = mService.getRootInActiveWindow();
        if (rootInActiveWindow != null) {
            List<AccessibilityNodeInfo> list = rootInActiveWindow.findAccessibilityNodeInfosByViewId(resId);
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    public static void recycle(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            nodeInfo.recycle();
        }
    }

    //通过id查找
    public static List<AccessibilityNodeInfo> findNodeListInfosById(String resId) {
        List<AccessibilityNodeInfo> result = new ArrayList<>();
        AccessibilityNodeInfo rootInActiveWindow = mService.getRootInActiveWindow();
        if (rootInActiveWindow != null) {
            List<AccessibilityNodeInfo> list = rootInActiveWindow.findAccessibilityNodeInfosByViewId(resId);
            if (list != null) {
                result.addAll(list);
            }
        }
        return result;
    }

    //通过id查找 ,第i个组件
    public static AccessibilityNodeInfo findNodeInfosById(String resId, int index) {
        AccessibilityNodeInfo rootInActiveWindow = mService.getRootInActiveWindow();
        if (rootInActiveWindow != null) {
            List<AccessibilityNodeInfo> list = rootInActiveWindow.findAccessibilityNodeInfosByViewId(resId);
            if (list != null && list.size() > index) {
                return list.get(index);
            }
        }
        return null;
    }

    //返回指定位置的node
    public static AccessibilityNodeInfo findNodeInfosByIdAndPosition(AccessibilityNodeInfo nodeInfo, String resId, int position) {
        if (nodeInfo == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            for (int i = 0; i < list.size(); i++) {
                if (i == position) {
                    return list.get(i);
                }
            }
        }
        return null;
    }

    //通过某个文本查找
    public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo == null) return null;
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    //通过某个文本查找
    public static AccessibilityNodeInfo findNodeInfosByText(String text) {
        AccessibilityNodeInfo rootInActiveWindow = mService.getRootInActiveWindow();
        if (rootInActiveWindow != null) {
            List<AccessibilityNodeInfo> list = rootInActiveWindow.findAccessibilityNodeInfosByText(text);
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list.get(0);
        }
        return null;
    }
    //通过某个文本查找
    public static AccessibilityNodeInfo findEditTextByHint(String hint) {
        List<AccessibilityNodeInfo> nodeEditTextList = findNodeInfoListDeeplyByClassName(mService.getRootInActiveWindow(), WechatUI.CLASS_NAME_EDITVIEW);
        for (AccessibilityNodeInfo nodeEditText : nodeEditTextList) {
            if (TextUtils.equals(nodeEditText.getText() ,hint)) {
                return nodeEditText;
            }
        }
        return null;
    }

    //通过某个文本查找
    public static List<AccessibilityNodeInfo> findNodeInfosListByText(String text) {
        List<AccessibilityNodeInfo> list = new ArrayList<>();
        AccessibilityNodeInfo rootInActiveWindow = mService.getRootInActiveWindow();
        if (rootInActiveWindow != null) {
            list = rootInActiveWindow.findAccessibilityNodeInfosByText(text);
            if (list == null) {
                return new ArrayList<>();
            }
        }
        return list;
    }

    //通过ClassName查找
    public static AccessibilityNodeInfo findNodeInfosByClassName(AccessibilityNodeInfo nodeInfo, String className) {
        if (TextUtils.isEmpty(className)) {
            return null;
        }
        for (int i = 0; nodeInfo != null && i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo node = nodeInfo.getChild(i);
            if (node != null) {
                if (className.equals(node.getClassName())) {
                    return node;
                } else if (node.getChildCount() > 0) {
                    AccessibilityNodeInfo nodeInfosByClassName = findNodeInfosByClassName(node, className);
                    if (nodeInfosByClassName != null) {
                        return nodeInfosByClassName;
                    }
                }
            }
        }
        return null;
    }

    //通过ClassName查找 在nodeInfo的下方
    public static AccessibilityNodeInfo findNodeInfoByClassNameBelowNode(AccessibilityNodeInfo nodeInfo, String className) {
        List<AccessibilityNodeInfo> nodeList = findNodeInfoListDeeplyByClassName(mService.getRootInActiveWindow(), className);
        Rect nodeRect = new Rect();
        Rect itemRect = new Rect();
        nodeInfo.getBoundsInScreen(nodeRect);
        int minY = -1;
        AccessibilityNodeInfo finalNode = null;
        for (AccessibilityNodeInfo node : nodeList) {
            node.getBoundsInScreen(itemRect);
            if (nodeRect.bottom <= itemRect.top) {
                //满足在node的下方的条件
                if (minY < 0 || itemRect.top < minY) {
                    minY = itemRect.top;
                    finalNode = node;
                }
            }
        }
        return finalNode;
    }

    //通过ClassName查找 在nodeInfo的上方
    public static AccessibilityNodeInfo findNodeInfoByClassNameAboveNode(AccessibilityNodeInfo nodeInfo, String className) {
        List<AccessibilityNodeInfo> nodeList = findNodeInfoListDeeplyByClassName(mService.getRootInActiveWindow(), className);
        Rect nodeRect = new Rect();
        Rect itemRect = new Rect();
        nodeInfo.getBoundsInScreen(nodeRect);
        int maxY = -1;
        AccessibilityNodeInfo finalNode = null;
        for (AccessibilityNodeInfo node : nodeList) {
            node.getBoundsInScreen(itemRect);
            if (nodeRect.top >= itemRect.bottom) {
                //满足在node的下方的条件
                if (maxY < 0 || itemRect.bottom > maxY) {
                    maxY = itemRect.bottom;
                    finalNode = node;
                }
            }
        }
        return finalNode;
    }

    //通过ClassName查找
    public static List<AccessibilityNodeInfo> findNodeInfoListByClassName(AccessibilityNodeInfo nodeInfo, String className) {
        if (TextUtils.isEmpty(className)) {
            return Collections.EMPTY_LIST;
        }
        List<AccessibilityNodeInfo> result = new ArrayList<>();
        for (int i = 0; nodeInfo != null && i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo node = nodeInfo.getChild(i);
            if (node != null && className.equals(node.getClassName())) {
                result.add(node);
            }
        }
        return result;
    }

    //通过ClassName查找
    public static List<AccessibilityNodeInfo> findNodeInfoListDeeplyByClassName(AccessibilityNodeInfo nodeInfo, String className) {
        if (TextUtils.isEmpty(className)) {
            return Collections.EMPTY_LIST;
        }
        List<AccessibilityNodeInfo> result = new ArrayList<>();
        for (int i = 0; nodeInfo != null && i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo node = nodeInfo.getChild(i);
            if (node != null) {
                if (className.equals(node.getClassName())) {
                    result.add(node);
                }
                if (node.getChildCount() > 0) {
                    result.addAll(findNodeInfoListDeeplyByClassName(node, className));
                }
            }
        }
        return result;
    }

    //通过node寻找 ListView node
    public static AccessibilityNodeInfo findListViewNodeByChild(AccessibilityNodeInfo nodeChild) {
        if (nodeChild == null) {
            return null;
        }
        AccessibilityNodeInfo parent = nodeChild.getParent();
        if (parent != null) {
            if (parent.getClassName().equals(WechatUI.CLASS_NAME_LIST_VIEW)) {
                return parent;
            } else {
                return findListViewNodeByChild(parent);
            }
        } else {
            return null;
        }
    }

    //通过ClassName查找
    public static AccessibilityNodeInfo findNodeInfosByClassName(String className) {
        return findNodeInfosByClassName(mService.getRootInActiveWindow(), className);
    }

    /**
     * 找父组件
     */
    public static AccessibilityNodeInfo findParentNodeInfosByClassName(AccessibilityNodeInfo nodeInfo, String className) {
        if (nodeInfo == null) {
            return null;
        }
        if (TextUtils.isEmpty(className)) {
            return null;
        }
        if (className.equals(nodeInfo.getClassName())) {
            return nodeInfo;
        }
        return findParentNodeInfosByClassName(nodeInfo.getParent(), className);
    }

    private static final Field sSourceNodeField;

    static {
        Field field = null;
        try {
            field = AccessibilityNodeInfo.class.getDeclaredField("mSourceNodeId");
            field.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sSourceNodeField = field;
    }

    public static long getSourceNodeId(AccessibilityNodeInfo nodeInfo) {
        if (sSourceNodeField == null) {
            return -1;
        }
        try {
            return sSourceNodeField.getLong(nodeInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getViewIdResourceName(AccessibilityNodeInfo nodeInfo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return nodeInfo.getViewIdResourceName();
        }
        return null;
    }

    //返回HOME界面
    public static void performHome(AccessibilityService service) {
        if (service == null) {
            return;
        }
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    //返回
    public static void performBack(AccessibilityService service) {
        if (service == null) {
            return;
        }
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    //返回
    public static boolean performBack() {
        //默认使用MainService
        if (AccessibilityHelper.mService == null) {
            return false;
        }
        return AccessibilityHelper.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    //返回
    public static boolean performBack(String uiClassName) {
        if (WechatCurrentActivity.isCurrentActivity(uiClassName)) {
            return performBack();
        }
        return false;
    }

    /**
     * 点击事件
     */
    public static void performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
    }

    /**
     * 点击事件
     */
    public static void performGestureClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null || mService == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Rect outBounds = new Rect();
            nodeInfo.getBoundsInScreen(outBounds);

            GestureDescription.Builder builder = new GestureDescription.Builder();
            Path p = new Path();
            p.moveTo(outBounds.left + outBounds.width() / 2f, outBounds.top + outBounds.height() / 2f);
            builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 100L));
            GestureDescription gesture = builder.build();
            mService.dispatchGesture(gesture, null, null);
        }
    }

    /**
     * 通过node的坐标长按
     */
    public static void performGestureLongClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null || mService == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Rect outBounds = new Rect();
            nodeInfo.getBoundsInScreen(outBounds);

            GestureDescription.Builder builder = new GestureDescription.Builder();
            Path p = new Path();
            p.moveTo(outBounds.left + outBounds.width() / 2f, outBounds.top + outBounds.height() / 2f);
            builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 1000L));
            GestureDescription gesture = builder.build();
            mService.dispatchGesture(gesture, null, null);
        }
    }

    /**
     * 通过xy坐标滑动屏幕
     */
    public static boolean performGestureScrollForward(AccessibilityNodeInfo nodeInfo) {
        if (mService == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            GestureDescription.Builder builder = new GestureDescription.Builder();
            Path p = new Path();
            p.moveTo(200, 1500);
            p.lineTo(200, 500);
//            p.moveTo(outBounds.left + outBounds.width() / 2f, outBounds.top + outBounds.height() / 2f);
//            p.lineTo(outBounds1.left + outBounds.width() / 2f, outBounds1.top + outBounds.height() / 2f);
            builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 100L));
            GestureDescription gesture = builder.build();
            return mService.dispatchGesture(gesture, null, null);
        }else {
            return perform_scroll_forward(nodeInfo);
        }

    }

    /**
     * 点击事件
     */
    public static void performGestureClick(float x, float y) {
        if (mService == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            GestureDescription.Builder builder = new GestureDescription.Builder();
            Path p = new Path();
            p.moveTo(x, y);
            builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 100L));
            GestureDescription gesture = builder.build();
            mService.dispatchGesture(gesture, null, null);
        }
    }

    /**
     * 点击事件
     */
    public static boolean performClickResultBoolean(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }
        if (nodeInfo.isClickable()) {
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            return performClickResultBoolean(nodeInfo.getParent());
        }
    }

    /**
     * 点击事件
     */
    public static void performClick(String id) {
        performClick(findNodeInfosById(id));
    }

    //长按事件
    public static void performLongClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
    }

    //move 事件
    public static void performMoveDown(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        nodeInfo.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN.getId());
    }


    //ACTION_SCROLL_FORWARD 事件
    public static boolean perform_scroll_forward(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }
        return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
    }

    //ACTION_SCROLL_BACKWARD 后退事件
    public static boolean perform_scroll_backward(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }
        return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
    }

    //粘贴
    @TargetApi(18)
    public static void performPaste(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
    }

    //设置editview text
    @TargetApi(21)
    public static void performSetText(AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo == null) {
            return;
        }
        CharSequence className = nodeInfo.getClassName();
        if ("android.widget.EditText".equals(className)) {//||"android.widget.TextView".equals(className)
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo
                    .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }
    }

    //设置editview text
    @TargetApi(21)
    public static boolean performSetTextReturnBoolean(AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo == null) {
            return false;
        }
        CharSequence className = nodeInfo.getClassName();
        if ("android.widget.EditText".equals(className)) {//||"android.widget.TextView".equals(className)
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo
                    .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }
        return false;
    }


}
