package com.android.service_getdata.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

/**
 * Created by inrsharm04 on 7/13/2015.
 */
public class IncomingSms extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        SmsMessage smsMessage = null;

        smsMessage.getOriginatingAddress();
        smsMessage.getTimestampMillis();
        smsMessage.getDisplayMessageBody();
    }
}
