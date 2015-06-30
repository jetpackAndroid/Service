package sms.example.inbjavia.server_parse;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
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
            MySmsProvider mySmsProvider = new MySmsProvider();
            ContentValues contentValues = new ContentValues();
            contentValues.put("m_id", "bhavik");
            Uri uri = Uri.parse("content://mysms");
            mySmsProvider.insert(uri, contentValues);
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
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
