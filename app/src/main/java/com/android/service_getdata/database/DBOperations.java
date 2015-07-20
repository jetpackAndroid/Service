package com.android.service_getdata.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.service_getdata.Helper.HelperClass;
import com.android.service_getdata.provider.BasicServiceProvider;
import com.android.service_getdata.provider.SyncServiceProvider;

/**
 * Created by inrsharm04 on 3/3/2015.
 */
public class DBOperations {
    private static final String TAG = DBOperations.class.getSimpleName();
    private static SQLiteDatabase DB_READ_ONLY;
    private static  SQLiteDatabase DB_READ_WRITE;
    static DBOperations dbOperations;

    public static DBOperations getInstance(Object caller) throws HelperClass.NotAllowedException {
        if (!BasicServiceProvider.class.isInstance(caller) &&
                !SyncServiceProvider.class.isInstance(caller))
            throw new HelperClass.NotAllowedException(caller.getClass()+" is not allowed to create instance of this class");

        if (dbOperations == null){
            dbOperations = new DBOperations();
        }
        return dbOperations;
    }
    private DBOperations(){

    }
    public long Insert_SMS(ContentValues contentValues)
    {
//        Log.d(TAG,"Insert_SMS() START");
        DB_READ_WRITE = DBHelper.openDataBase(DBHelper.READ_WRITE);
        long result = (DB_READ_WRITE.insert(DBQuery.DbTables.TABLE_SMS,null,contentValues));
//        Log.d(TAG,"Insert_SMS() result: "+result);
//        Log.d(TAG,"Insert_SMS() END");
        return result;
    }
    public long Insert_CALLLOGS(ContentValues contentValues)
    {
//        Log.d(TAG,"Insert_CALLLOGS() START");
        DB_READ_WRITE = DBHelper.openDataBase(DBHelper.READ_WRITE);
        long result = (DB_READ_WRITE.insert(DBQuery.DbTables.TABLE_CALL_LOGS,null,contentValues));
//        Log.d(TAG,"Insert_CALLLOGS() result: "+result);
//        Log.d(TAG,"Insert_CALLLOGS() END");
        return result;
    }
    public Cursor Query_Sms_CALLLogs(String table, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        DB_READ_ONLY = DBHelper.openDataBase(DBHelper.READ_ONLY);
        Cursor cursor = DB_READ_ONLY.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    public long Insert_SYNCSMS(ContentValues contentValues) {
        //        Log.d(TAG,"Insert_SMS() START");
        DB_READ_WRITE = DBHelper.openDataBase(DBHelper.READ_WRITE);
        long result = (DB_READ_WRITE.insert(DBQuery.DbTables.TABLE_SYNC_SMS, null, contentValues));
//        Log.d(TAG,"Insert_SMS() result: "+result);
//        Log.d(TAG,"Insert_SMS() END");
        return result;
    }

    public long Insert_SYNCCALLLOGS(ContentValues contentValues) {
//        Log.d(TAG, "Insert_CALLLOGS() START");
        DB_READ_WRITE = DBHelper.openDataBase(DBHelper.READ_WRITE);
        long result = (DB_READ_WRITE.insert(DBQuery.DbTables.TABLE_SYNC_CALLLOGS, null, contentValues));
//        Log.d(TAG,"Insert_CALLLOGS() result: "+result);
//        Log.d(TAG,"Insert_CALLLOGS() END");
        return result;
    }
}
