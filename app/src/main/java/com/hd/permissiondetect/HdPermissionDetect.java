package com.hd.permissiondetect;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.location.LocationManager;

import java.net.NetworkInterface;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * @Description:
 * @Author: liaoyuhuan
 * @CreateDate: 2021/4/14
 */
public class HdPermissionDetect implements IXposedHookLoadPackage {
    private static final String TAG = "权限检测";

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam == null) {
            return;
        }
        LOG.F(TAG, "Load app packageName:" + lpparam.packageName);
        // 固定格式
        XposedHelpers.findAndHookMethod(
                // 需要hook的方法所在类的完整类名
                android.telephony.TelephonyManager.class.getName(),
                // 类加载器，固定这么写就行了
                lpparam.classLoader,
                // 需要hook的方法名
                "getDeviceId",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "调用getDeviceId()获取了imei");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
        XposedHelpers.findAndHookMethod(
                android.telephony.TelephonyManager.class.getName(),
                lpparam.classLoader,
                "getDeviceId",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "调用getDeviceId(int)获取了imei");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                android.telephony.TelephonyManager.class.getName(),
                lpparam.classLoader,
                "getSubscriberId",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "调用getSubscriberId获取了imsi");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                android.net.wifi.WifiInfo.class.getName(),
                lpparam.classLoader,
                "getMacAddress",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "调用getMacAddress()获取了mac地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );


        XposedHelpers.findAndHookMethod(
                java.net.NetworkInterface.class.getName(),
                lpparam.classLoader,
                "getHardwareAddress",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "调用getHardwareAddress()获取了mac地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                android.provider.Settings.Secure.class.getName(),
                lpparam.classLoader,
                "getString",
                ContentResolver.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
//                        LOG.F(TAG, "调用Settings.Secure.getstring获取了" + param.args[1]);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                LocationManager.class.getName(),
                lpparam.classLoader,
                "getLastKnownLocation",
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "调用getLastKnownLocation获取了GPS地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        hookIpAddress(lpparam);
        hookInstalledPackages(lpparam);
        hookInetAddress(lpparam);
    }

    private void hookIpAddress(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(
                android.net.wifi.WifiInfo.class.getName(),
                lpparam.classLoader,
                "getIpAddress",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "getIpAddress()获取了IP地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                NetworkInterface.class.getName(),
                lpparam.classLoader,
                "getInetAddresses",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "getInetAddresses 获取了IP地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
    }

    void hookInetAddress(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(
                java.net.InetAddress.class.getName(),
                lpparam.classLoader,
                "getHostAddress",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "java.net.InetAddress.getHostAddress 获取了IP地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
        XposedHelpers.findAndHookMethod(
                java.net.InetAddress.class.getName(),
                lpparam.classLoader,
                "getAddress",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "java.net.InetAddress.getAddress 获取了IP地址");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );
    }


    private void hookInstalledPackages(XC_LoadPackage.LoadPackageParam lpparam) {
        // 私自收集应用软件列表信息 packages
        XposedHelpers.findAndHookMethod(
                "android.app.ApplicationPackageManager",
                lpparam.classLoader,
                "getInstalledPackages",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "调用getInstalledPackages(int)方法收集了应用安装列表");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        // 私自收集应用软件列表信息 applications
        XposedHelpers.findAndHookMethod(
                "android.app.ApplicationPackageManager",
                lpparam.classLoader,
                "getInstalledApplications",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "调用getInstalledApplications(int)方法收集了应用安装列表");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

        // 私自收集应用软件列表信息 applications
        XposedHelpers.findAndHookMethod(
                ActivityManager.class.getName(),
                lpparam.classLoader,
                "getRunningAppProcesses",
                List.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        LOG.F(TAG, "getRunningAppProcesses( ) 获取了当前应用信息");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LOG.F(TAG, getMethodStack());
                        super.afterHookedMethod(param);
                    }
                }
        );

    }

    private String getMethodStack() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement temp : stackTraceElements) {
            stringBuilder.append(temp.toString()).append("\n");
        }
        return stringBuilder.toString();
    }
}
