package com.leavy.my_study_demo.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;

import com.leavy.my_study_demo.manager.JobManager;
import com.leavy.my_study_demo.manager.LocationManager;
import com.leavy.my_study_demo.receiver.AlarmReceiver;

import androidx.annotation.Nullable;

public class LocationService extends Service {

    //不推荐使用，在耗电一块不太出色
    //如果需要屏幕常亮可以在theme中配置 <item name="android:keepScreenOn">true</item>
    //也可以代码中使用 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    //但是要知道有这么一个东西可以使用来保持cpu一直运行阻止cpu睡眠，因此使用WakeLock会导致耗电量增加
    //这里如果非及时性的任务我们可以使用闹钟AlarmManager处理
    PowerManager.WakeLock wakeLock;

    //闹钟管理者
    AlarmManager alarmManager;

    AlarmReceiver alarmReceiver = new AlarmReceiver();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //wakeLock
        //lockKeep();
        JobManager.getInstance().init(this);
        LocationManager.getInstance().startLocation(this);

        //alarmManager
        alarmKeep();
    }

    /**
     * 闹钟形式的
     */
    private void alarmKeep() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent();
        alarmIntent.setAction("my_location");
        //创建一个等待意图 getBroadcast方法时检索我们的意图（intent），然后去执行一个广播
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        //创建一个拦截器拦截我们的意图my_location
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("my_location");
        //注册一个广播去拦截我们的意图
        registerReceiver(alarmReceiver, intentFilter);
        //设置重复 这里就是设置5秒闹钟他会唤醒一次cpu
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 5000,
                pendingIntent
        );
    }

    /**
     * 通过wakeLock保证cpu补睡眠一直运行
     */
    @SuppressLint("InvalidWakeLockTag")
    private void lockKeep() {
        //获取PowerManager对象
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        //判断是否支持wakeLock PARTIAL_WAKE_LOCK级别
        pm.isWakeLockLevelSupported(PowerManager.PARTIAL_WAKE_LOCK);

        //通过PowerManager对象获取wakeLock 这里PARTIAL_WAKE_LOCK是只确保cpu运行
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "my_lock");
        //拿到wakelock 保证cpu不睡眠休息
        //这里记住wakeLock 获取与释放是需要成对出现的 所以我们要在销毁时释放wakeLock
        wakeLock.acquire();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wakeLock != null) {
            //释放wakelock
            wakeLock.release();
        }

        if(alarmReceiver!=null){
            unregisterReceiver(alarmReceiver);
        }
    }
}
