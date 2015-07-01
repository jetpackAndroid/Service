package sms.example.inbjavia.server_parse;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.d("My_Service", "FirstService started");
        super.onCreate();
        try {
            Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
            if (cursor.moveToFirst()) { // must check the result to prevent exception
                Log.d("My_Service","We have message");
                do {
                    String msgData = "";
                    for(int i=0;i<cursor.getColumnCount();i++)
                    {
                        msgData += " " + cursor.getColumnName(i) + ":" + cursor.getString(i);
                    }
                    Log.d("My_Service","mesage Data = " + msgData);
                    // use msgData
                } while (cursor.moveToNext());
            } else {
                Log.d("My_Service","No message");
            }

            ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put("m_id", "bhavik");
            contentResolver.insert(MySmsProvider.CONTENT_URI, contentValues);
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("number","9811585125");
            contentResolver.insert(MySmsProvider.CONTENT_URI_CALL_LOGS,contentValues1);
        }
        catch(Exception e){
            Log.d("My_Service","Exception is "+ e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
