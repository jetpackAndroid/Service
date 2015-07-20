package com.android.service_getdata.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.android.service_getdata.Helper.AppConstant;
import com.android.service_getdata.Threads.PickUpDataThread;

/**
 * Created by inrsharm04 on 7/16/2015.
 */
public class BackupSchedular extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Service");
        wl.acquire();
        pickDataFromDevice(context);
        wl.release();
    }
    private void pickDataFromDevice(Context context)
    {
        //creating sms thread
        ContentResolver mContentResolver = context.getContentResolver();
        PickUpDataThread pickUpDataThreadSms = new PickUpDataThread(mContentResolver,PickUpDataThread.SMS_THREAD);
        //creating Call Log thread
        PickUpDataThread pickUpDataThreadCallLogs = new PickUpDataThread(mContentResolver,PickUpDataThread.CALLLOGS_THREAD);
        Thread threadSms = new Thread(pickUpDataThreadSms);
        Thread threadCallLog = new Thread(pickUpDataThreadCallLogs);
        //Sms Thread start
        threadSms.start();
        //Call Log Thread start
        threadCallLog.start();
    }

    public void SchedulebackUp(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, BackupSchedular.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, AppConstant.BACK_UP_SCHEDULAR_REQUEST_CODE, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AppConstant.ONCE_IN_A_DAY, pi);
    }

    public void unRegisterScheduledBackUp(Context context)
    {
        Intent intent = new Intent(context, BackupSchedular.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, AppConstant.BACK_UP_SCHEDULAR_REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
