package com.android.service_getdata.parsehandling;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.android.service_getdata.Helper.AppConstant;
import com.android.service_getdata.database.DBQuery;
import com.android.service_getdata.provider.BasicServiceProvider;
import com.android.service_getdata.provider.SyncServiceProvider;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by inrsharm04 on 7/15/2015.
 */
public class SaveSMSToParse implements SaveCallback{

    private final String TAG = "SaveSMSToParse";
    List<ParseObject> parseObjectList;
    ContentResolver mContentResolver;
    List<ContentValues> DbValues;
    public boolean isContinue;

    @Override
    public void done(ParseException e) {
        if (e == null){
            changeStatus_InProgress_To_Status(AppConstant.SMS_STATUS_SAVED);
            saveDataTOSyncSms();
            deleteDataFromBackUpTable();
        }else{
            changeStatus_InProgress_To_Status(AppConstant.SMS_STATUS_UNSAVED);
        }
        parseObjectList.clear();
        DbValues.clear();
    }
    public SaveSMSToParse(ContentResolver contentResolver){
        parseObjectList = new ArrayList<ParseObject>();
        DbValues = new ArrayList<>();
        mContentResolver = contentResolver;
        isContinue = true;
    }

    public void fetchSMSDataFromDB()
    {
        parseObjectList.clear();
        DbValues.clear();

        String [] columns = {DBQuery.DbFields.COLUMN_SMS_BODY, DBQuery.DbFields.COLUMN_SMS_THREAD_ID, DBQuery.DbFields.COLUMN_SMS_TYPE,
                DBQuery.DbFields.COLUMN_SMS_PERSON, DBQuery.DbFields.COLUMN_SMS_ADDRESS, DBQuery.DbFields.COLUMN_SMS_DATE_SENT, DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID,
                DBQuery.DbFields.COLUMN_SMS_DATE};
        String where = DBQuery.DbFields.COLUMN_SMS_STATUS+"=?";
        String [] whereArgs = {Integer.toString(AppConstant.SMS_STATUS_UNSAVED)};
        Cursor cursor = mContentResolver.query(BasicServiceProvider.CONTENT_URI_SMS, columns, where, whereArgs, AppConstant.SMS_UPLOAD_SORTING_ORDER + " LIMIT " + AppConstant.SMS_RECORD_LIMIT);

        if (cursor == null || cursor.getCount() == 0){
            if (cursor != null)
                cursor.close();
            isContinue = false;
            return;
        }
        cursor.moveToFirst();
        do{
            ContentValues values = new ContentValues();

            int m_id = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID));
            String m_body = cursor.getString(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_BODY));
            int m_tid = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_THREAD_ID));
            int m_type = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_TYPE));
            int m_p = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_PERSON));
            String m_address = cursor.getString(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_ADDRESS));
            int m_dsent = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_DATE_SENT));
            int m_d = cursor.getInt(cursor.getColumnIndex(DBQuery.DbFields.COLUMN_SMS_DATE));

            values.put(DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID, Integer.toString(m_id));
            values.put(DBQuery.DbFields.COLUMN_SMS_BODY, m_body);
            values.put(DBQuery.DbFields.COLUMN_SMS_THREAD_ID, Integer.toString(m_tid));
            values.put(DBQuery.DbFields.COLUMN_SMS_TYPE, Integer.toString(m_type));
            values.put(DBQuery.DbFields.COLUMN_SMS_PERSON, Integer.toString(m_p));
            values.put(DBQuery.DbFields.COLUMN_SMS_ADDRESS, m_address);
            values.put(DBQuery.DbFields.COLUMN_SMS_DATE_SENT, Integer.toString(m_dsent));
            values.put(DBQuery.DbFields.COLUMN_SMS_DATE, Integer.toString(m_d));

            DbValues.add(values);
        }
        while (cursor.moveToNext());
        cursor.close();

        if (DbValues.isEmpty()){
            isContinue = false;
            return;
        }
        createParseObjects(DbValues);
    }
    private void createParseObjects(List<ContentValues> DbValues)
    {
        for (int iParseObjectCount = 0; iParseObjectCount <DbValues.size(); iParseObjectCount++)
        {
            ParseObject smsLogs = new ParseObject("SMSLogs");
            smsLogs.put("message_Id", DbValues.get(iParseObjectCount).getAsInteger(DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID));
            smsLogs.put("thread_Id", DbValues.get(iParseObjectCount).getAsInteger(DBQuery.DbFields.COLUMN_SMS_THREAD_ID));
            smsLogs.put("mobile_Number", DbValues.get(iParseObjectCount).getAsString(DBQuery.DbFields.COLUMN_SMS_ADDRESS));
            smsLogs.put("person_Name", DbValues.get(iParseObjectCount).getAsString(DBQuery.DbFields.COLUMN_SMS_PERSON));
            smsLogs.put("receive_Date", DbValues.get(iParseObjectCount).getAsInteger(DBQuery.DbFields.COLUMN_SMS_DATE));
            smsLogs.put("sent_Date", DbValues.get(iParseObjectCount).getAsInteger(DBQuery.DbFields.COLUMN_SMS_DATE_SENT));
            smsLogs.put("message_Body", DbValues.get(iParseObjectCount).getAsString(DBQuery.DbFields.COLUMN_SMS_BODY));

            parseObjectList.add(smsLogs);
        }

    }
    public void sendDataToParse(){
//        fetchSMSDataFromDB();
        if (parseObjectList != null && !parseObjectList.isEmpty()) {
            changeDataStatus_MID(AppConstant.SMS_STATUS_INPROGRESS);
            ParseObject.saveAllInBackground(parseObjectList, this);
        }
    }
    private void saveDataTOSyncSms(){
        if ((parseObjectList == null || parseObjectList.isEmpty())
                && (DbValues == null || DbValues.isEmpty()) ){
            return;
        }
        for (ContentValues data : DbValues)
        {
            ContentValues values = new ContentValues();
            values.put(DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID, data.getAsString(DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID));
            values.put(DBQuery.DbFields.COLUMN_SMS_STATUS, AppConstant.SMS_STATUS_SAVED);
            mContentResolver.insert(SyncServiceProvider.CONTENT_URI_SYNC_SMS, data);
        }
    }
    private void deleteDataFromBackUpTable()
    {
        if ((parseObjectList == null || parseObjectList.isEmpty())
                && (DbValues == null || DbValues.isEmpty()) ){
            return;
        }
        String where = DBQuery.DbFields.COLUMN_SMS_STATUS + "=?";
        String [] whereArgs = {"" + AppConstant.SMS_STATUS_SAVED};
        mContentResolver.delete(BasicServiceProvider.CONTENT_URI_SMS, where, whereArgs);
    }
    private void changeDataStatus_MID(int status){
        if ((parseObjectList == null || parseObjectList.isEmpty())
        && (DbValues == null || DbValues.isEmpty()) ){
            return;
        }
        for (ContentValues data : DbValues){

            String m_id = data.getAsString(DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID);
            String where = DBQuery.DbFields.COLUMN_SMS_MESSAGE_ID + "=?";
            String [] whereArgs = {m_id};
            data.put(DBQuery.DbFields.COLUMN_SMS_STATUS, status);
            mContentResolver.update(BasicServiceProvider.CONTENT_URI_SMS, data, where, whereArgs);

        }
    }
    private void changeStatus_InProgress_To_Status(int status){
        if ((parseObjectList == null || parseObjectList.isEmpty())
                && (DbValues == null || DbValues.isEmpty()) ){
            return;
        }
            String where = DBQuery.DbFields.COLUMN_SMS_STATUS  + "=?";
            String [] whereArgs = {"" + AppConstant.SMS_STATUS_INPROGRESS};
            ContentValues values = new ContentValues();
            values.put(DBQuery.DbFields.COLUMN_SMS_STATUS, status);
            mContentResolver.update(BasicServiceProvider.CONTENT_URI_SMS, values, where, whereArgs);

    }
}
