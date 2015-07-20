package com.android.service_getdata.application;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;

/**
 * Created by inrsharm04 on 7/15/2015.
 */
public class ServiceApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = this;
        Parse.initialize(this, "2EgAoVkYmL4pTmbhLJP0dMQ7vPoZ9GR4ycXVbNNp", "O1vE9V9xSR2m7OONmVaxkjBx7tiBW9a82vRlNyTR");
    }
    public static Context getAppContext(){
        return mContext;
    }
}
