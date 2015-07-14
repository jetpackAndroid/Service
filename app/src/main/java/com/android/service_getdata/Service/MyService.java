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

        PickUpDataThread pickUpDataThread = new PickUpDataThread(getContentResolver());
        Thread thread = new Thread(pickUpDataThread);
        thread.start();
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
