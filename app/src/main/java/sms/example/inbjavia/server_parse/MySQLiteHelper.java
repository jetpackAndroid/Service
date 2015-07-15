package sms.example.inbjavia.server_parse;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by inbjavia on 6/30/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_COMMENT = "comment";

    private static final String DATABASE_NAME = "mysms.db";
    private static final int DATABASE_VERSION = 11;

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
        createCallLogsTables(db);
        createSmsTables(db);
    }
    private void createCallLogsTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE mCallLogs (" +
        "call_id INTEGER PRIMARY KEY," +
        "number TEXT," +
        "date INTEGER," +
        "duration INTEGER," +
        "type INTEGER," +
        "new INTEGER," +
        "name TEXT," +
        "numbertype INTEGER," +
        "numberlabel TEXT," +
        "formatted_number TEXT" +
        ");");
    }

    private void createSmsTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE mysms (" +
                "_id INTEGER ," +
                "m_id INTEGER PRIMARY KEY," +
                "thread_id INTEGER," +
                "address TEXT," +
                "person INTEGER," +
                "date INTEGER," +
                "date_sent INTEGER DEFAULT 0," +
                "type INTEGER," +
                "body TEXT" +
                ");");
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
