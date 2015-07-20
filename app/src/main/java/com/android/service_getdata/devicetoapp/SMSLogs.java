package com.android.service_getdata.devicetoapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

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
public class SMSLogs {

    List<ContentValues> receivedData;
    ContentResolver mContentResolver;
    private final String TAG = "SMSLogs";

    public SMSLogs(ContentResolver contentResolver){
        mContentResolver = contentResolver;
        receivedData = new ArrayList<ContentValues>();
    }

    public void getSmsData(){

        getDataFromDevice();
        if (receivedData == null || receivedData.isEmpty()){
            return;
        }
        for (int iContentValues = 0; iContentValues <receivedData.size(); iContentValues++){

            ContentValues contentValues = receivedData.get(iContentValues);
            String address = contentValues.getAsString(DBQuery.DbFields.COLUMN_SMS_ADDRESS);
            int date = contentValues.getAsInteger(DBQuery.DbFields.COLUMN_SMS_DATE);
            String body = contentValues.getAsString(DBQuery.DbFields.COLUMN_SMS_BODY);
            String m_id = HelperClass.getMessageID(address, "" + date, body);

            if (!HelperClass.isMessageCallIDExist(SyncServiceProvider.CONTENT_URI_SYNC_SMS, m_id, DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID, mContentResolver) &&
                    !HelperClass.isMessageCallIDExist(BasicServiceProvider.CONTENT_URI_SMS, m_id, DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID, mContentResolver)) {

                contentValues.put(DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID, m_id);
                contentValues.put(DBQuery.DbFields.COLUMN_SMS_STATUS, AppConstant.SMS_STATUS_UNSAVED);

                Uri uri = mContentResolver.insert(BasicServiceProvider.CONTENT_URI_SMS, contentValues);
                Log.d(TAG, "getSmsData() Uri : " + uri);
                Log.d(TAG, "getSmsData() MessageID : " + contentValues.getAsString(DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID));
            }
        }
    }

    private void getDataFromDevice(){

        Cursor cursor = null;
        String [] Columns = {Telephony.TextBasedSmsColumns.THREAD_ID, Telephony.TextBasedSmsColumns.ADDRESS, Telephony.TextBasedSmsColumns.PERSON,
                Telephony.TextBasedSmsColumns.DATE, Telephony.TextBasedSmsColumns.DATE_SENT, Telephony.TextBasedSmsColumns.TYPE,
                Telephony.TextBasedSmsColumns.BODY};
        cursor = mContentResolver.query(Uri.parse("content://sms"), Columns, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            if (cursor != null)
                cursor.close();
            return;
        }
        cursor.moveToFirst(); // must check the result to prevent exception
        do {

            ContentValues contentValues = new ContentValues();

            //retrieving from cursor
            int thread_id = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_THREAD_ID));
            String address = cursor.getString(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_ADDRESS));
            int person = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_PERSON));
            int date = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_DATE));
            int date_sent = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_DATE_SENT));
            int type = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_TYPE));
            String body = cursor.getString(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_BODY));

            //content value creation
            contentValues.put(DBQuery.DbFields.COLUMN_SMS_THREAD_ID, thread_id);
            contentValues.put(DBQuery.DbFields.COLUMN_SMS_ADDRESS, address);
            contentValues.put(DBQuery.DbFields.COLUMN_SMS_PERSON, person);
            contentValues.put(DBQuery.DbFields.COLUMN_SMS_DATE, date);
            contentValues.put(DBQuery.DbFields.COLUMN_SMS_DATE_SENT, date_sent);
            contentValues.put(DBQuery.DbFields.COLUMN_SMS_TYPE, type);
            contentValues.put(DBQuery.DbFields.COLUMN_SMS_BODY, body);

            receivedData.add(contentValues);
        } while (cursor.moveToNext());
        cursor.close();
    }

}
