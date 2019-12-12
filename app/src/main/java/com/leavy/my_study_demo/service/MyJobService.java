package com.leavy.my_study_demo.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyJobService extends JobService {


    @Override
    public boolean onStartJob(JobParameters params) {
        //如果返回值是false,这个方法返回时任务已经执行完毕。
        //如果返回值是true,那么这个任务正要被执行，我们就需要开始执行任务。
        //当任务执行完毕时你需要调用jobFinished(JobParameters params, boolean needsRescheduled)来通知系统
        new UploadTask().execute();
        return true;
    }
    //当系统接收到一个取消请求时
    @Override
    public boolean onStopJob(JobParameters params) {
        //如果onStartJob返回false,那么onStopJob不会被调用
        // 返回 true 则会重新计划这个job
        return false;
    }

    class UploadTask extends AsyncTask<JobParameters,Void,Void> {

        JobParameters jobParameters ;

        @Override
        protected Void doInBackground(JobParameters[] jobInfos) {
            jobParameters = jobInfos[0];
            String location = jobParameters.getExtras().getString("location_data");
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            jobFinished(jobParameters,false);
        }
    }

}
