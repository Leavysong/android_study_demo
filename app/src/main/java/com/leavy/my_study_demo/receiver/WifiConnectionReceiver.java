package com.leavy.my_study_demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.leavy.my_study_demo.util.BatteryUtil;

public class WifiConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(BatteryUtil.isWifi(context)){
            //wifi链接
        }else{
            //wifi没连接
        }
    }
}
