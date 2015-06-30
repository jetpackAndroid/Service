package sms.example.inbjavia.server_parse;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.CallLog;
import android.provider.VoicemailContract;
import android.util.Log;

/**
 * Created by inbjavia on 6/30/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_COMMENT = "comment";

    private static final String DATABASE_NAME = "mysms.db";
    private static final int DATABASE_VERSION = 1;

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createSmsTables(db);
        createCallLogsTables(db);
    }
    private void createCallLogsTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + " mCallLogs (" +
        CallLog.Calls._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        CallLog.Calls.NUMBER + " TEXT," +
        CallLog.Calls.NUMBER_PRESENTATION + " INTEGER NOT NULL DEFAULT " +
        CallLog.Calls.PRESENTATION_ALLOWED + "," +
        CallLog.Calls.DATE + " INTEGER," +
        CallLog.Calls.DURATION + " INTEGER," +
        CallLog.Calls.DATA_USAGE + " INTEGER," +
        CallLog.Calls.TYPE + " INTEGER," +
        CallLog.Calls.FEATURES + " INTEGER NOT NULL DEFAULT 0," +
        CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME + " TEXT," +
        CallLog.Calls.PHONE_ACCOUNT_ID + " TEXT," +
//        CallLog.Calls.SUB_ID + " INTEGER DEFAULT -1," +
        CallLog.Calls.NEW + " INTEGER," +
        CallLog.Calls.CACHED_NAME + " TEXT," +
        CallLog.Calls.CACHED_NUMBER_TYPE + " INTEGER," +
        CallLog.Calls.CACHED_NUMBER_LABEL + " TEXT," +
        CallLog.Calls.COUNTRY_ISO + " TEXT," +
        CallLog.Calls.VOICEMAIL_URI + " TEXT," +
        CallLog.Calls.IS_READ + " INTEGER," +
        CallLog.Calls.GEOCODED_LOCATION + " TEXT," +
        CallLog.Calls.CACHED_LOOKUP_URI + " TEXT," +
        CallLog.Calls.CACHED_MATCHED_NUMBER + " TEXT," +
        CallLog.Calls.CACHED_NORMALIZED_NUMBER + " TEXT," +
        CallLog.Calls.CACHED_PHOTO_ID + " INTEGER NOT NULL DEFAULT 0," +
        CallLog.Calls.CACHED_FORMATTED_NUMBER + " TEXT," +
//        VoicemailContract.Voicemails._DATA + " TEXT," +
        VoicemailContract.Voicemails.HAS_CONTENT + " INTEGER," +
        VoicemailContract.Voicemails.MIME_TYPE + " TEXT," +
        VoicemailContract.Voicemails.SOURCE_DATA + " TEXT," +
        VoicemailContract.Voicemails.SOURCE_PACKAGE + " TEXT," +
        VoicemailContract.Voicemails.TRANSCRIPTION + " TEXT," +
//        VoicemailContract.Voicemails.STATE + " INTEGER" +
        ");");
    }

    private void createSmsTables(SQLiteDatabase db) {
        // N.B.: Whenever the columns here are changed, the columns in
        // {@ref MmsSmsProvider} must be changed to match.
        db.execSQL("CREATE TABLE mysms (" +
                "_id INTEGER PRIMARY KEY," +
                "m_id TEXT," +   // STC FIELD ADDED
                "thread_id INTEGER," +
                "address TEXT," +
                "person INTEGER," +
                "date INTEGER," +
                "date_sent INTEGER DEFAULT 0," +
                "protocol INTEGER," +
                "read INTEGER DEFAULT 0," +
                "status INTEGER DEFAULT -1," + // a TP-Status value
                // or -1 if it
                // status hasn't
                // been received
                "type INTEGER," +
                "reply_path_present INTEGER," +
                "subject TEXT," +
                "body TEXT," +
                "service_center TEXT," +
                "locked INTEGER DEFAULT 0," +
                "sub_id INTEGER DEFAULT -1" + ", " +
                "error_code INTEGER DEFAULT 0," +
                "creator TEXT," +
                "seen INTEGER DEFAULT 0" +
                ");");

        /**
         * This table is used by the SMS dispatcher to hold
         * incomplete partial messages until all the parts arrive.
         */
        db.execSQL("CREATE TABLE raw (" +
                "_id INTEGER PRIMARY KEY," +
                "date INTEGER," +
                "reference_number INTEGER," + // one per full message
                "count INTEGER," + // the number of parts
                "sequence INTEGER," + // the part number of this message
                "destination_port INTEGER," +
                "address TEXT," +
                "sub_id INTEGER DEFAULT -1" +  ", " +
                "pdu TEXT);"); // the raw PDU for this part

        db.execSQL("CREATE TABLE attachments (" +
                "sms_id INTEGER," +
                "content_url TEXT," +
                "offset INTEGER);");

        /**
         * This table is used by the SMS dispatcher to hold pending
         * delivery status report intents.
         */
        db.execSQL("CREATE TABLE sr_pending (" +
                "reference_number INTEGER," +
                "action TEXT," +
                "data TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("My_Service_MySQLiteHelper",
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }
}
