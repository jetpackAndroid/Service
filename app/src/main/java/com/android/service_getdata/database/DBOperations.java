package com.android.service_getdata.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.service_getdata.Helper.HelperClass;
import com.android.service_getdata.provider.ServiceProvider;

/**
 * Created by inrsharm04 on 3/3/2015.
 */
public class DBOperations {
    private static final String TAG = DBOperations.class.getSimpleName();
    private static SQLiteDatabase DB_READ_ONLY;
    private static  SQLiteDatabase DB_READ_WRITE;
    static DBOperations dbOperations;

    public static DBOperations getInstance(Object caller) throws HelperClass.NotAllowedException {
        if (!ServiceProvider.class.isInstance(caller))
            throw new HelperClass.NotAllowedException(caller.getClass()+" is not allowed to create instance of this class");

        if (dbOperations == null){
            dbOperations = new DBOperations();
        }
        return dbOperations;
    }
    private DBOperations(){

    }
    public long Insert_SMS(ContentValues contentValues){
        Log.d(TAG,"Insert_SMS() START");
        DB_READ_WRITE = DBHelper.openDataBase(DBHelper.READ_WRITE);
        long result = (DB_READ_WRITE.insert(DBQuery.DbTables.TABLE_SMS,null,contentValues));
        Log.d(TAG,"Insert_SMS() result: "+result);
        Log.d(TAG,"Insert_SMS() END");
        return result;
    }
    public Cursor Query_Sms(String[] projection, String selection, String[] selectionArgs, String sortOrder){
        DB_READ_ONLY = DBHelper.openDataBase(DBHelper.READ_ONLY);
        Cursor cursor = DB_READ_ONLY.query(DBQuery.DbTables.TABLE_SMS,projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }
}