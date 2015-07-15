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
<<<<<<< HEAD
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        PickUpDataThread pickUpDataThread = new PickUpDataThread(getContentResolver());
        Thread thread = new Thread(pickUpDataThread);
        thread.start();
=======
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Sms Thread start
        PickUpDataThread pickUpDataThreadSms = new PickUpDataThread(getContentResolver(),PickUpDataThread.SMS_THREAD);
        Thread threadSms = new Thread(pickUpDataThreadSms);
        threadSms.start();

        //Call Log Thread start
        PickUpDataThread pickUpDataThreadCallLogs = new PickUpDataThread(getContentResolver(),PickUpDataThread.CALLLOGS_THREAD);
        Thread threadCallLog = new Thread(pickUpDataThreadCallLogs);
        threadCallLog.start();

>>>>>>> ec52dde34d5ca4957bd29e0dcc8021491a4a7cc8
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
