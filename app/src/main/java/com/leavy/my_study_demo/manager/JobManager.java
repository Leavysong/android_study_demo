package com.leavy.my_study_demo.manager;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.PersistableBundle;

import com.leavy.my_study_demo.service.MyJobService;

import java.util.List;

public class JobManager {

    static JobScheduler jobScheduler;

    static Context appContext;

    //任务id
    static final int jobID = 0 ;

    private JobManager() {
    }

    static JobManager jobManager;

    public static JobManager getInstance() {
        if (jobManager != null) {

            jobManager = new JobManager();

        }
        return jobManager;
    }

    public static void init(Context context){
        jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        appContext = context.getApplicationContext();

    }

    /**
     * 添加任务
     *
     * @param location 任务信息
     */
    public static void addJob(String location) {

        if (null == jobScheduler) {
            throw  new NullPointerException("jobScheduler must be NoNull,please init before use");
        }

        JobInfo jobInfo = null ;

        //24之后可以直接寻找jobid合并工作  24之前需要遍历
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.N) {
            jobInfo = jobScheduler.getPendingJob(jobID);
        }else{
            List<JobInfo> allPendingJobs = jobScheduler.getAllPendingJobs();
            for (JobInfo info : allPendingJobs) {
                if (info.getId()==jobID) {
                    jobInfo = info ;
                    break;
                }
            }
        }
        //判断jobInfo是否为空
        if (jobInfo!=null) {

            PersistableBundle extras = jobInfo.getExtras();
            String my_location = extras.getString("location_data");
            //这里拼接的&可以随便写 要和后台沟通达成自定义协议
            //比如 传给后台的是“北京&上海&深圳&海口这”这一类的字符串后台自己解析 这里是统计数据后提交
            location += "&"+location;
            //关闭当前任务后面重新提交合并后的任务
            jobScheduler.cancel(jobID);
        }
        //PersistableBundle和Bundle用法差不多都是BaseBundle的子类详情看代码这里用法就是和bundle差不多的
        PersistableBundle bundle = new PersistableBundle();
        //注意与上面我们取值时对应的key要相同
        bundle.putString("location_data",location);


        JobInfo newJobInfo = new JobInfo.Builder(jobID,new ComponentName(appContext, MyJobService.class))
                //在这里可以选择配置很多得属性
                //设置不在低电量时工作
                .setRequiresCharging(true)
                //设置没有在无计量时
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setExtras(bundle)
                .build();
        //提交任务
        jobScheduler.schedule(newJobInfo);
    }
}
