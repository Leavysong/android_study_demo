package com.leavy.my_study_demo.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadTask extends AsyncTask<JobParameters,Void,Void> {

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

    }
}
