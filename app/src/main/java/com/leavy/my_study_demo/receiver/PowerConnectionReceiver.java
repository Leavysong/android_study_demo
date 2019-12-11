package com.leavy.my_study_demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class PowerConnectionReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //获取action信息
        String action = intent.getAction();
        //判断action信息手否是充电链接
        if(TextUtils.equals(action,Intent.ACTION_POWER_CONNECTED)){
            //充电
        }else{
            //没充电
        }
    }


}
