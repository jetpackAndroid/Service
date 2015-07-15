package com.android.service_getdata.receivers;

<<<<<<< HEAD
import android.app.ActivityManager;
=======
>>>>>>> ec52dde34d5ca4957bd29e0dcc8021491a4a7cc8
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
<<<<<<< HEAD
import android.widget.Toast;

import com.android.service_getdata.Service.MyService;
=======
>>>>>>> ec52dde34d5ca4957bd29e0dcc8021491a4a7cc8

/**
 * Created by inrsharm04 on 7/13/2015.
 */
public class IncomingSms extends BroadcastReceiver
{
<<<<<<< HEAD
    //private String SERVICE="com.android.service_getdata.Service";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context,"Incoming Sms Detected",Toast.LENGTH_LONG).show();
       // boolean isServiceRunning=false;
        if(context==null && intent==null)
        {
            return;
        }
        //Checking for Service is started or not
//        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (SERVICE.equals(service.getClass().getSimpleName()))
//            {
//                isServiceRunning=true;
//            }
//            else
//            {
               // isServiceRunning=false;
                Intent in=new Intent(context,MyService.class);
                context.startService(in);
          //  }

        //}
=======
    @Override
    public void onReceive(Context context, Intent intent)
    {
        SmsMessage smsMessage = null;

        smsMessage.getOriginatingAddress();
        smsMessage.getTimestampMillis();
        smsMessage.getDisplayMessageBody();
>>>>>>> ec52dde34d5ca4957bd29e0dcc8021491a4a7cc8
    }
}
