package sms.example.inbjavia.server_parse;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
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
           ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put("m_id", "bhavik");
            contentResolver.insert(MySmsProvider.CONTENT_URI, contentValues);
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
