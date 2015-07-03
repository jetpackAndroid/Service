package sms.example.inbjavia.server_parse;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {


    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PickUpDataThread pickUpDataThread = new PickUpDataThread(getContentResolver(), PickUpDataThread.UNLIMITED_THREAD);
        Thread thread = new Thread(pickUpDataThread);
        thread.start();

        return START_STICKY;
    }

//    @Override
//    public void onCreate() {
//        Log.d("My_Service", "FirstService started");
//        super.onCreate();
//        try {
//
//            ContentResolver contentResolver = getContentResolver();
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("m_id", "bhavik");
//            contentResolver.insert(MySmsProvider.CONTENT_URI, contentValues);
//            ContentValues contentValues1 = new ContentValues();
//            contentValues1.put("number","9811585125");
//            contentResolver.insert(MySmsProvider.CONTENT_URI_CALL_LOGS,contentValues1);
//        }
//        catch(Exception e){
//            Log.d("My_Service","Exception is "+ e.getMessage());
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
