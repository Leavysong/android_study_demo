package com.leavy.my_study_demo.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.Nullable;

public class UploadService extends IntentService {


    public UploadService() {
        super("upload_service");
    }

    /**
     * 上传信息
     * @param location
     */
    public static void UploadLocation(Context context , String location) {
        //创建意图
        Intent intent = new Intent(context,UploadService.class);
        //把信息以键值对的形式放进意图中
        intent.putExtra("location_data",location);
        //开启服务
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //接收到intent消息后取出intent中的值
        String location = intent.getStringExtra("location_data");
        OutputStream os  = null ;
        HttpURLConnection connection = null ;
        try {
            //这里随便写的网址无所谓的
            connection = (HttpURLConnection) new URL("https://www.xxxxxx.com/").openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            os = connection.getOutputStream();
            os.write(location.getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (os!=null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                connection.disconnect();
            }

        }


    }


}
