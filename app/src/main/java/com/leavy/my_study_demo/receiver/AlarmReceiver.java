package com.leavy.my_study_demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.leavy.my_study_demo.location.LocationManager;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (TextUtils.equals(intent.getAction(), "my_location")) {
            //是我们的自己的意图 拦截到后我们去开启定位
            LocationManager.getInstance().startLocation(context);
        }
    }
}
