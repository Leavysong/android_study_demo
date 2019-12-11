package com.leavy.my_study_demo.util;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.view.LayoutInflater;

import java.util.logging.Filter;
import java.util.zip.Inflater;

public class BatteryUtil {

    /**
     * 是否在充电
     */
    public static boolean isPlugged(Context context) {

        //创建过滤器拦截电量改变广播
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //通过过滤器来获取电量改变intent 电量改变是系统广播所以我们无需去设置所以receiver传null即可
        Intent intent = context.registerReceiver(null, intentFilter);
        //获取电量信息
        int isPlugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        //电源充电
        boolean acPlugged = BatteryManager.BATTERY_PLUGGED_AC == isPlugged;
        //usb充电
        boolean usbPlugged = BatteryManager.BATTERY_PLUGGED_USB == isPlugged;
        //无线充电
        boolean wirePlugged = BatteryManager.BATTERY_PLUGGED_WIRELESS == isPlugged;

        //满足充电即返回true
        return acPlugged || usbPlugged || wirePlugged;

    }

    /**
     * 是否为wifi链接状态
     */
    public static boolean isWifi(Context context) {

        //获取网络状态管理
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //根据网络状态管理来获取当前网络信息
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        //根据网络信息获取当前是否wifi链接
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected() &&
                activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


}
