package com.android.service_getdata.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.service_getdata.Helper.HelperClass;
import com.android.service_getdata.provider.BasicServiceProvider;
import com.android.service_getdata.provider.SyncServiceProvider;

/**
 * Created by inrsharm04 on 3/3/2015.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final int READ_ONLY = 0;
    public static final int READ_WRITE = 1;
    private static final String TAG = DBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "servicedb.db";
    private static final int VERSION = 1;
    private final Context context;
    private static SQLiteDatabase DB_READ_ONLY;
    private static SQLiteDatabase DB_READ_WRITE;
    private static String DB_PATH = null;
    private static DBHelper dbInstance;

    public static DBHelper getInstance(Context context, Object callerObject) throws HelperClass.NotAllowedException{

        if (!BasicServiceProvider.class.isInstance(callerObject) &&
                !SyncServiceProvider.class.isInstance(callerObject))
            throw new HelperClass.NotAllowedException(callerObject.getClass()+" is not allowed to create instance of this class");

        Log.d(TAG,"getInstance");
        if (dbInstance != null)
            return dbInstance;
        dbInstance = new DBHelper(context);
        Log.d(TAG,"getInstance NEW");
        return dbInstance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
        DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
        Log.d(TAG,"DBHelper:Constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate : START");
        try{
            db.beginTransaction();
            Log.d(TAG, "onCreate : Transaction START");
            db.execSQL(DBQuery.DATABASE_CREATE_TABLE_CONTACT);
            db.execSQL(DBQuery.DATABASE_CREATE_TABLE_CallLOGS);
            db.execSQL(DBQuery.DATABASE_CREATE_TABLE_SMS);
            db.execSQL(DBQuery.DATABASE_CREATE_TABLE_SYNC_CALLLOGS);
            db.execSQL(DBQuery.DATABASE_CREATE_TABLE_SYNC_SMS);
            db.execSQL(DBQuery.DATABASE_CREATE_TABLE_SYNC_CONTACT);
            db.setTransactionSuccessful();
            Log.d(TAG,"onCreate : Transaction END");
        }
        catch (SQLiteException e){
            Log.e(TAG,"SQLiteException : "+e.getMessage());
        }
        catch (Exception e){
            Log.e(TAG,"Exception : "+e.getClass()+" : "+e.getMessage());
        }
        finally {
            db.endTransaction();
            Log.d(TAG,"onCreate : END");
        }
    }
/*

 */
    public static SQLiteDatabase openDataBase(int MODE) throws NullPointerException{
        Log.d(TAG,"openDataBase : MODE : "+MODE);
        try {
              switch (MODE){
                  case READ_ONLY :
                      if ( DB_READ_ONLY == null || !DB_READ_ONLY.isOpen() ) {
                          if (DB_READ_ONLY == null)
                              DB_READ_ONLY = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
                          else
                              synchronized (DB_READ_ONLY){
                                  DB_READ_ONLY = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
                              }
                      }
                      Log.d(TAG,"openDataBase : SUCCESS : "+MODE);
                      return DB_READ_ONLY;
                  case READ_WRITE :
                      if ( DB_READ_WRITE == null || !DB_READ_WRITE.isOpen() ){
                          if (DB_READ_WRITE == null){
                              DB_READ_WRITE = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
                          }
                          else
                              synchronized (DB_READ_WRITE){
                                  DB_READ_WRITE = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
                              }
                      }
                      Log.d(TAG,"openDataBase : SUCCESS : "+MODE);
                      return DB_READ_WRITE;
                  default : return MODE == READ_WRITE ? DB_READ_WRITE : DB_READ_ONLY;
              }
        }
        catch (SQLiteException sqliteException){
            Log.e(TAG,"SQLiteException : "+sqliteException.getMessage());
        }
        catch (Exception e){
            Log.e(TAG,"Exception : "+e.getClass()+" : "+e.getMessage());
        }
        Log.d(TAG,"openDataBase : END : NULL");
        return null;
    }
    public static void closeDatabase(){
        try
        {
            Log.d(TAG,"closeDatabase : START");
            if(DB_READ_WRITE != null && DB_READ_WRITE.isOpen()) {
                synchronized (DB_READ_WRITE) {
                    DB_READ_WRITE.close();
                }
            }
            if(DB_READ_ONLY != null && DB_READ_ONLY.isOpen()) {
                synchronized (DB_READ_ONLY) {
                    DB_READ_ONLY.close();
                }
            }
            DB_READ_WRITE = null;
            DB_READ_ONLY = null;
            Log.d(TAG,"closeDatabase : END");
        }
        catch (SQLiteException sqliteException){
            Log.e(TAG,"SQLiteException : "+sqliteException.getMessage());
        }
        catch (Exception e){
            Log.e(TAG,"Exception : "+e.getClass()+" : "+e.getMessage());
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
