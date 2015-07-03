package sms.example.inbjavia.server_parse;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class MySmsProvider extends ContentProvider {

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/mysms";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/mysm";
    // used for the UriMacher
    private static final int SMS_ID = 10;
    private static final int LOGS_ID = 30;
    private static final int TODO_ID = 20;
    private static final String AUTHORITY = "sms.example.inbjavia.server_parse.MySmsProvider";
    private static final String BASE_PATH_SMS = "mysms";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_SMS);
    private static final String BASE_PATH_CALL_LOGS = "mCallLogs";
    public static final Uri CONTENT_URI_CALL_LOGS = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_CALL_LOGS);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_SMS, SMS_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_SMS + "/#", SMS_ID);

        sURIMatcher.addURI(AUTHORITY, BASE_PATH_CALL_LOGS, LOGS_ID);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_CALL_LOGS + "/#", TODO_ID);
    }

    private MySQLiteHelper database;

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
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();

        int rowsDeleted = 0;
        long id = 0;
//        id = sqlDB.insert(uri.toString(), null, values);

        switch (uriType) {
            case SMS_ID:
                Log.d("insert", "uri: " + uri.toString());
                id = sqlDB.insert("mysms", null, values);
                Log.d("insert", "id: " + id);
                break;
            case LOGS_ID:
                id = sqlDB.insert("mCallLogs", null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH_SMS + "/" + id);
    }

    @Override
    public boolean onCreate() {
        System.out.println("");
        database = new MySQLiteHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int match = sURIMatcher.match(uri);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = null;

        switch (match) {
            case SMS_ID:
                qb.setTables("mysms");
//                cursor = db.query("mysms",projection,selection,selectionArgs,null,null,null);
                cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;

        }
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
