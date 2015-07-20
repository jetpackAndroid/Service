package com.android.service_getdata.contentobserver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Telephony;
import com.android.service_getdata.Helper.HelperClass;
import com.android.service_getdata.database.DBQuery;
import com.android.service_getdata.provider.BasicServiceProvider;
import com.android.service_getdata.provider.SyncServiceProvider;

import java.util.Date;

/**
 * Created by inrsharm04 on 7/16/2015.
 */
public class SentSmsHandler extends ContentObserver {

    Handler mSmsServiceHandler;
    ContentResolver mContentResolver;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SentSmsHandler(Handler handler, ContentResolver contentResolver) {
        super(handler);
        mSmsServiceHandler = handler;
        mContentResolver = contentResolver;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Cursor cursor = mContentResolver.query(Uri.parse("content://sms"), null, null, null, null);

        ContentValues values = null;
        String address = "", body = "";
        int date = 0;
        if (cursor.moveToNext()) {
            values = new ContentValues();
            String protocol = cursor.getString(cursor.getColumnIndex("protocol"));
            int type = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_TYPE));
            if (protocol != null || type != Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT) {
                mSmsServiceHandler.sendEmptyMessage(2);
                return;
            }
            int thread_id = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_THREAD_ID));
            address = cursor.getString(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_ADDRESS));
            int person = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_PERSON));
            date = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_DATE));
            int date_sent = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_DATE_SENT));
            int sms_type = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_TYPE));
            body = cursor.getString(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_BODY));

            values.put(DBQuery.DbFields.COLUMN_SMS_THREAD_ID, thread_id);
            values.put(DBQuery.DbFields.COLUMN_SMS_ADDRESS, address);
            values.put(DBQuery.DbFields.COLUMN_SMS_PERSON, person);
            values.put(DBQuery.DbFields.COLUMN_SMS_DATE, date);
            values.put(DBQuery.DbFields.COLUMN_SMS_DATE_SENT, date_sent);
            values.put(DBQuery.DbFields.COLUMN_SMS_TYPE, sms_type);
            values.put(DBQuery.DbFields.COLUMN_SMS_BODY, body);
        }
        if (cursor ==null || values == null){
            mSmsServiceHandler.sendEmptyMessage(1);
            return;
        }
        cursor.close();

        String m_id = HelperClass.getMessageID(address, "" + date, body);
        if (!HelperClass.isMessageCallIDExist(SyncServiceProvider.CONTENT_URI_SYNC_SMS, m_id, DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID, mContentResolver) &&
                !HelperClass.isMessageCallIDExist(BasicServiceProvider.CONTENT_URI_SMS, m_id, DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID, mContentResolver)) {
            values.put(DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID, m_id);
            Uri uri = mContentResolver.insert(BasicServiceProvider.CONTENT_URI_SMS, values);
            mSmsServiceHandler.sendEmptyMessage(0);
        }
    }
}
