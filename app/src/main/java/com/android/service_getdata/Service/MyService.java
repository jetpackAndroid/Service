package com.android.service_getdata.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.service_getdata.PickUpDataThread;
import com.android.service_getdata.provider.ServiceProvider;

public class MyService extends Service {


    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Sms Thread start
        PickUpDataThread pickUpDataThreadSms = new PickUpDataThread(getContentResolver(),PickUpDataThread.SMS_THREAD);
        Thread threadSms = new Thread(pickUpDataThreadSms);
        threadSms.start();

        //Call Log Thread start
        PickUpDataThread pickUpDataThreadCallLogs = new PickUpDataThread(getContentResolver(),PickUpDataThread.CALLLOGS_THREAD);
        Thread threadCallLog = new Thread(pickUpDataThreadCallLogs);
        threadCallLog.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
