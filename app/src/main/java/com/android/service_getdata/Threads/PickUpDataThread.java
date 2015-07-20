package com.android.service_getdata.Threads;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.android.service_getdata.Helper.AppConstant;
import com.android.service_getdata.Helper.BackUpListnerInterface;
import com.android.service_getdata.Helper.HelperClass;
import com.android.service_getdata.application.ServiceApp;
import com.android.service_getdata.database.DBQuery;
import com.android.service_getdata.devicetoapp.CallLogs;
import com.android.service_getdata.devicetoapp.SMSLogs;
import com.android.service_getdata.provider.BasicServiceProvider;

/**
 * Created by inbjavia on 7/2/2015.
 */
public class PickUpDataThread extends Thread{

    private String TAG = PickUpDataThread.class.getSimpleName();
    public static final int SMS_THREAD = 1;
    public static final int CALLLOGS_THREAD = 2;
    public static final int CONTACT_THREAD = 3;
    private int mBackUpStatusSms;
    private int mBackUpStatusCallLogs;
    private int mCurrentThread;
    private SMSLogs smsLogs;
    private CallLogs callLogs;
    LocalBroadcastManager localBroadcastManager;

    public PickUpDataThread(ContentResolver contentResolver, int type) {
        mCurrentThread = type;
        localBroadcastManager = LocalBroadcastManager.getInstance(ServiceApp.getAppContext());
        if (type == SMS_THREAD) {
            smsLogs = new SMSLogs(contentResolver);
            mBackUpStatusSms = AppConstant.BACKUP_STATUS_INITIALIZED;
        }
        if (type == CALLLOGS_THREAD) {
            callLogs = new CallLogs(contentResolver);
            mBackUpStatusCallLogs = AppConstant.BACKUP_STATUS_INITIALIZED;
        }
    }
    @Override
    public void run() {
        try {
            switch (mCurrentThread)
            {
                case SMS_THREAD:
                    smsLogs.getSmsData();
                    mBackUpStatusSms = AppConstant.SMS_STATUS_SAVED;
                    sendBackupBroadcast(AppConstant.BACKUP_STATUS_RESULT_SUCCESS, mBackUpStatusSms);
                    break;
                case CALLLOGS_THREAD:
                    callLogs.getCallData();
                    mBackUpStatusCallLogs = AppConstant.CALLLOGS_STATUS_SAVED;
                    sendBackupBroadcast(AppConstant.BACKUP_STATUS_RESULT_SUCCESS, mBackUpStatusCallLogs);
                    break;
                case CONTACT_THREAD:
                    break;
                default:
                    break;
            }
        }
        catch (Exception exc){
            if (mCurrentThread == SMS_THREAD){
                mBackUpStatusSms = AppConstant.SMS_STATUS_UNSAVED;
                sendBackupBroadcast(AppConstant.BACKUP_STATUS_RESULT_FAILED, mBackUpStatusSms);
            }else if (mCurrentThread == CALLLOGS_THREAD){
                mBackUpStatusCallLogs = AppConstant.CALLLOGS_STATUS_UNSAVED;
                sendBackupBroadcast(AppConstant.BACKUP_STATUS_RESULT_FAILED, mBackUpStatusCallLogs);
            }
            exc.printStackTrace();
        }
    }
    public void sendBackupBroadcast(String result, int resultCode)
    {
        Intent intent = new Intent(AppConstant.BACKUP_INTENT_ACTION);
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.BACKUP_STATUS_RESULT, result);
        bundle.putInt(AppConstant.BACKUP_STATUS_RESULT_CODE, resultCode);
        intent.putExtras(bundle);
        localBroadcastManager.sendBroadcast(intent);
    }
    private int getBackUpStatusSms() {
        return mBackUpStatusSms;
    }
    private int getBackUpStatusCallLogs() {
        return mBackUpStatusCallLogs;
    }
}
