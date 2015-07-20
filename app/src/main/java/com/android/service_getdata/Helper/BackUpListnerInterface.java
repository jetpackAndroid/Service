package com.android.service_getdata.Helper;

/**
 * Created by inrsharm04 on 7/17/2015.
 */
public interface BackUpListnerInterface {
    public void OnSmsBackUpResult(String result, int resultCode);
    public void OnCallLogBackUpResult(String result, int resultCode);
}
