package com.android.service_getdata.Helper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
<<<<<<< HEAD
import android.text.TextUtils;

=======
import android.net.Uri;
import android.text.TextUtils;

import com.android.service_getdata.database.DBQuery;
>>>>>>> ec52dde34d5ca4957bd29e0dcc8021491a4a7cc8
import com.android.service_getdata.provider.ServiceProvider;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by inrsharm04 on 7/13/2015.
 */
public class HelperClass {

    public static String getMessageID(String address, String timeStamp, String msgBody){
        String messageData = address + timeStamp + msgBody;
        byte[] MessageData = messageData.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if(md == null){
            return "";
        }
        md.update(MessageData, 0, MessageData.length);
        byte[] hash = md.digest();
        BigInteger bigInteger = new BigInteger(bytesToHexString(hash),16);
        BigInteger result = bigInteger.mod(new BigInteger("10000", 16));
        return ""+result;
    }
    private static String bytesToHexString(byte[] bytes) {
        if (bytes == null) return null;
        StringBuilder ret = new StringBuilder(2*bytes.length);
        for (int i = 0 ; i < bytes.length ; i++) {
            int b;
            b = 0x0f & (bytes[i] >> 4);
            ret.append("0123456789abcdef".charAt(b));
            b = 0x0f & bytes[i];
            ret.append("0123456789abcdef".charAt(b));
        }

        return ret.toString();
    }
    public static String getPackageNamesByUid(Context context, int uid) {
        final PackageManager pm = context.getPackageManager();
        final String[] packageNames = pm.getPackagesForUid(uid);
        if (packageNames != null) {
            final StringBuilder sb = new StringBuilder();
            for (String name : packageNames) {
                if (!TextUtils.isEmpty(name)) {
                    if (sb.length() > 0) {
                        sb.append(' ');
                    }
                    sb.append(name);
                }
            }
            return sb.toString();
        }
        return null;
    }
<<<<<<< HEAD
    public static boolean isMessageIDExist(String messageId, ContentResolver mContentResolver){
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(ServiceProvider.CONTENT_URI_SMS, new String[]{"*"}, "m_id=?", new String[]{messageId}, null);
            if (cursor == null) {
                return false;
            }
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("m_id");
                String m_Id = cursor.getString(columnIndex);
                if (m_Id == null) {
                    cursor.close();
                    return false;
                }
                cursor.close();
                return true;
            }
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
=======
    public static boolean isMessageCallIDExist(Uri tableUri, String messageId, String columnName, ContentResolver mContentResolver){
        Cursor cursor = null;
        cursor = mContentResolver.query(tableUri, new String[]{"*"}, columnName+"=?", new String[]{messageId}, null);
        if (cursor == null) {
            return false;
        }
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            String m_Id = cursor.getString(columnIndex);
            if (m_Id == null) {
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        }
        cursor.close();
>>>>>>> ec52dde34d5ca4957bd29e0dcc8021491a4a7cc8
        return false;
    }
    public static class NotAllowedException extends Exception{
        private String message = null;

        public NotAllowedException() {
            super();
        }

        public NotAllowedException(String message) {
            super(message);
            this.message = message;
        }

        public NotAllowedException(Throwable cause) {
            super(cause);
        }

        @Override
        public String toString() {
            return message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
