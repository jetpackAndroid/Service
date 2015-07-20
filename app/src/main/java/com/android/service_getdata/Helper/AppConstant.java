package com.android.service_getdata.Helper;

import android.provider.CallLog;
import android.provider.Telephony;

import com.android.service_getdata.database.DBQuery;

/**
 * Created by inrsharm04 on 3/3/2015.
 */
public class AppConstant {

    public static final int BACKUP_STATUS_INITIALIZED = -1;
    public static final int BACKUP_STATUS_COMPLETED = -2;
    public static final int BACKUP_STATUS_ERROR = -3;

    public static final int SMS_STATUS_UNSAVED = 0;
    public static final int SMS_STATUS_INPROGRESS = 1;
    public static final int SMS_STATUS_SAVED = 2;

    public static final int CALLLOGS_STATUS_UNSAVED = 3;
    public static final int CALLLOGS_STATUS_INPROGRESS = 4;
    public static final int CALLLOGS_STATUS_SAVED = 5;

    public static final int CONTACT_STATUS_UNSAVED = 6;
    public static final int CONTACT_STATUS_INPROGRESS = 7;
    public static final int CONTACT_STATUS_SAVED = 8;

    public static final String SMS_UPLOAD_SORTING_ORDER = DBQuery.DbFields.COLUMN_SMS_THREAD_ID;
    public static final int SMS_RECORD_LIMIT = 50;

    public static final long HALF_MINUTE = 30*1000;
    public static final long ONE_MINUTE = 1*60*1000;
    public static final long FIVE_MINUTE = 5*60*1000;
    public static final long TEN_MINUTE = 10*60*1000;
    public static final long TWENTY_MINUTE = 20*60*1000;
    public static final long HALF_HOUR = 30*60*1000;
    public static final long ONE_HOUR = 1*60*60*1000;
    public static final long FIVE_HOUR = 5*60*60*1000;
    public static final long HALF_DAY = 12*60*60*1000;
    public static final long ONCE_IN_A_DAY = 24*60*60*1000;

    public static final int BACK_UP_SCHEDULAR_REQUEST_CODE = 1001;
    public static final String BACKUP_INTENT_ACTION = "com.android.service_getdata.backup_action";
    public static final String BACKUP_STATUS_RESULT = "RESULT";
    public static final String BACKUP_STATUS_RESULT_CODE = "RESULT_CODE";
    public static final String BACKUP_STATUS_RESULT_FAILED = "FAILED";
    public static final String BACKUP_STATUS_RESULT_SUCCESS = "SUCCESS";

    public static final String [] SMS_COLUMNS = {Telephony.TextBasedSmsColumns.THREAD_ID, Telephony.TextBasedSmsColumns.ADDRESS, Telephony.TextBasedSmsColumns.PERSON,
            Telephony.TextBasedSmsColumns.DATE, Telephony.TextBasedSmsColumns.DATE_SENT, Telephony.TextBasedSmsColumns.TYPE,
            Telephony.TextBasedSmsColumns.BODY};

    public static final String [] CALL_LOG_COLUMNS = {CallLog.Calls.NUMBER, CallLog.Calls.NEW, CallLog.Calls.CACHED_FORMATTED_NUMBER,
            CallLog.Calls.CACHED_NUMBER_TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.CACHED_NUMBER_LABEL,
            CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE};
}
