package com.android.service_getdata.database;

/**
 * Created by inrsharm04 on 3/3/2015.
 */
public class DBQuery {


    public interface DbTables{
        //Database tables
        String TABLE_CALL_LOGS = "CallLogs";
        String TABLE_SMS = "Sms";
        String TABLE_CONTACT = "Contact";
    }

    public interface DbFields{

        //TABLE_CALL_LOGS Fields
        String COLUMN_CALL_LOGS_ID = "_id";
        String COLUMN_CALL_LOGS_CALL_ID = "call_id";
        String COLUMN_CALL_LOGS_NUMBER = "number";
        String COLUMN_CALL_LOGS_DATE = "date";
        String COLUMN_CALL_LOGS_DURATION = "duration";
        String COLUMN_CALL_LOGS_TYPE = "type";
        String COLUMN_CALL_LOGS_NEW = "new";
        String COLUMN_CALL_LOGS_NAME = "name";
        String COLUMN_CALL_LOGS_NUMBER_TYPE = "numbertype";
        String COLUMN_CALL_LOGS_NUMBER_LABEL = "numberlabel";
        String COLUMN_CALL_LOGS_NUMBER_FORMATED_NUMBER = "formatted_number";

        //TABLE_SMS Fields
        String COLUMN_SMS_ID = "_id";
        String COLUMN_SMS_MESSAGE_ID = "m_id";
        String COLUMN_SMS_THREAD_ID = "thread_id";
        String COLUMN_SMS_ADDRESS = "address";
        String COLUMN_SMS_PERSON = "person";
        String COLUMN_SMS_DATE = "date";
        String COLUMN_SMS_DATE_SENT = "date_sent";
        String COLUMN_SMS_TYPE = "type";
        String COLUMN_SMS_BODY = "body";

        //TABLE_CONTACT Fields
        String COLUMN_CONTACT_ID = "CONTACT_ID";
        String COLUMN_CONTACT_NAME = "CONTACT_NAME";
        String COLUMN_CONTACT_MOBILE = "CONTACT_MOBILE";
        String COLUMN_CONTACT_ADDRESS = "CONTACT_ADDRESS";
        String COLUMN_CONTACT_EMAIL = "CONTACT_EMAIL";
        String COLUMN_CONTACT_COMPANY = "CONTACT_COMPANY";
    }


    //create table CONTACT
    protected static final String DATABASE_CREATE_TABLE_CONTACT = "CREATE TABLE "
            + DbTables.TABLE_CONTACT + "(" + DbFields.COLUMN_CONTACT_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DbFields.COLUMN_CONTACT_NAME
            + " TEXT NOT NULL, " + DbFields.COLUMN_CONTACT_MOBILE
            + " TEXT NOT NULL, " + DbFields.COLUMN_CONTACT_EMAIL
            + " TEXT, " + DbFields.COLUMN_CONTACT_ADDRESS
            + " TEXT, " + DbFields.COLUMN_CONTACT_COMPANY
            + " TEXT);";

    //create table CallLogs
    protected static final String DATABASE_CREATE_TABLE_CallLOGS = "CREATE TABLE "
            + DbTables.TABLE_CALL_LOGS + "(" + DbFields.COLUMN_CALL_LOGS_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DbFields.COLUMN_CALL_LOGS_CALL_ID
            + " INTEGER NOT NULL UNIQUE, " + DbFields.COLUMN_CALL_LOGS_NAME
            + " TEXT, " + DbFields.COLUMN_CALL_LOGS_DATE
            + " INTEGER, " + DbFields.COLUMN_CALL_LOGS_DURATION
            + " INTEGER, " + DbFields.COLUMN_CALL_LOGS_NUMBER
            + " TEXT, " + DbFields.COLUMN_CALL_LOGS_NUMBER_LABEL
            + " TEXT, " + DbFields.COLUMN_CALL_LOGS_NUMBER_TYPE
            + " INTEGER, " + DbFields.COLUMN_CALL_LOGS_NUMBER_FORMATED_NUMBER
            + " TEXT, " + DbFields.COLUMN_CALL_LOGS_NEW
            + " INTEGER, " + DbFields.COLUMN_CALL_LOGS_TYPE
            + " INTEGER);";

    //create table Sms
    protected static final String DATABASE_CREATE_TABLE_SMS = "CREATE TABLE "
            + DbTables.TABLE_SMS + "(" + DbFields.COLUMN_SMS_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DbFields.COLUMN_SMS_MESSAGE_ID
            + " INTEGER NOT NULL UNIQUE, " + DbFields.COLUMN_SMS_ADDRESS
            + " TEXT, " + DbFields.COLUMN_SMS_BODY
            + " TEXT, " + DbFields.COLUMN_SMS_DATE
            + " INTEGER, " + DbFields.COLUMN_SMS_DATE_SENT
            + " INTEGER, " + DbFields.COLUMN_SMS_PERSON
            + " INTEGER, " + DbFields.COLUMN_SMS_TYPE
            + " INTEGER, " + DbFields.COLUMN_SMS_THREAD_ID
            + " INTEGER);";
}
