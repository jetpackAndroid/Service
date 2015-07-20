package com.android.service_getdata.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.android.service_getdata.Helper.HelperClass;
import com.android.service_getdata.database.DBHelper;
import com.android.service_getdata.database.DBOperations;
import com.android.service_getdata.database.DBQuery;

public class ServiceProvider extends ContentProvider {

    String TAG = ServiceProvider.class.getSimpleName();
    private DBOperations dbOperations;
    // used for the UriMacher
    private static final int SMS_ID = 1;
    private static final int CALLLOGS_ID = 2;
    private static final int CONTACT_ID = 3;

    private static final String AUTHORITY = "com.android.service_getdata.provider.ServiceProvider";

    private static final String BASE_PATH_SMS = DBQuery.DbTables.TABLE_SMS;
    public static final Uri CONTENT_URI_SMS = Uri.parse("content://" + AUTHORITY+ "/" + BASE_PATH_SMS);
    private static final String BASE_PATH_CALL_LOGS = DBQuery.DbTables.TABLE_CALL_LOGS;
    public static final Uri CONTENT_URI_CALL_LOGS = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_CALL_LOGS);
    private static final String BASE_PATH_CONTACT = DBQuery.DbTables.TABLE_CONTACT;
    public static final Uri CONTENT_URI_CONTACT = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_CONTACT);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_SMS, SMS_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_SMS + "/#", SMS_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_CALL_LOGS, CALLLOGS_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_CALL_LOGS + "/#", CALLLOGS_ID);
<<<<<<< HEAD
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_CALL_LOGS, CONTACT_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_CALL_LOGS + "/#", CONTACT_ID);
=======
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_CONTACT, CONTACT_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_CONTACT + "/#", CONTACT_ID);
>>>>>>> ec52dde34d5ca4957bd29e0dcc8021491a4a7cc8
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        //throw new UnsupportedOperationException("Not yet implemented");
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Log.d(TAG,"insert() START");
        int uriType = sURIMatcher.match(uri);
        Log.d(TAG,"insert() uriType : "+uriType);
        int rowsDeleted = 0;
        long id = 0;
        Uri returnUri = null;
        try {
            switch (uriType) {
                case SMS_ID:
                    id = dbOperations.Insert_SMS(values);
                    returnUri = Uri.parse(BASE_PATH_SMS + "/" + id);
                    break;
                case CALLLOGS_ID:
<<<<<<< HEAD
//                id = sqlDB.insert("mCallLogs", null, values);
=======
                    id = dbOperations.Insert_CALLLOGS(values);
>>>>>>> ec52dde34d5ca4957bd29e0dcc8021491a4a7cc8
                    returnUri = Uri.parse(BASE_PATH_CALL_LOGS + "/" + id);
                    break;
                case CONTACT_ID:
                    returnUri = Uri.parse(BASE_PATH_CONTACT + "/" + id);
                    break;
                default:
                    returnUri = Uri.parse(uri.toString() + "/" + -1);
            }
        }
        catch (Exception e){
            if (uri != null)
                returnUri = Uri.parse(uri.toString() + "/" + -1);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        Log.d(TAG, "insert() Returned Uri is : " + returnUri);
        Log.d(TAG, "insert() END");
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG,"onCreate() START");
        DBHelper db = null;
        try {
            db = DBHelper.getInstance(getContext(),this);
            dbOperations = DBOperations.getInstance(this);
        }
        catch (HelperClass.NotAllowedException exc){
            exc.printStackTrace();
        }

        db.getWritableDatabase();
        db.close();
        Log.d(TAG, "onCreate() END");
        return db == null ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Log.d(TAG,"query() START");
        int match = sURIMatcher.match(uri);
        Log.d(TAG,"query() Match : "+match);
        Cursor cursor = null;

        switch (match) {
            case SMS_ID:
<<<<<<< HEAD
                cursor = dbOperations.Query_Sms(projection, selection, selectionArgs, sortOrder);
                break;
            case CALLLOGS_ID:
=======
                cursor = dbOperations.Query_Sms_CALLLogs(DBQuery.DbTables.TABLE_SMS, projection, selection, selectionArgs, sortOrder);
                break;
            case CALLLOGS_ID:
                cursor = dbOperations.Query_Sms_CALLLogs(DBQuery.DbTables.TABLE_CALL_LOGS, projection, selection, selectionArgs, sortOrder);
>>>>>>> ec52dde34d5ca4957bd29e0dcc8021491a4a7cc8
                break;
            case CONTACT_ID:
                break;
            default:
                break;
        }
        Log.d(TAG,"query() END");
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
//        throw new UnsupportedOperationException("Not yet implemented");
        return 0;
    }
}
