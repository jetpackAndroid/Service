package sms.example.inbjavia.server_parse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Device Booted",Toast.LENGTH_SHORT).show();
        Log.d("Service", "Boot Completed");
    }
}
