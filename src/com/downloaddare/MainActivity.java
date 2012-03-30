package com.downloaddare;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends Activity {
    public static final String TAG = "DOWNLOADDARE";
    public static final int MAX_SMS_MESSAGE_LENGTH = 160;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /*
        delete all contacts
        http://stackoverflow.com/questions/527216/how-to-remove-a-contact-programmatically-in-android
     */
    public void btnClearContacts(View view) {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        while (cur.moveToNext()) {
            try {
                String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                System.out.println("The uri is " + uri.toString());
                cr.delete(uri, null, null);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
    }

    /*
        read all contacts
        http://stackoverflow.com/questions/2356084/read-all-contacts-phone-numbers-in-android
     */
    public void btnReadAllContactNumbers(View view) {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.i(TAG, "Phone number: " + phoneNumber);
        }
        phones.close();
    }

    /*
        send text to all contacts
        http://stackoverflow.com/questions/7620150/can-i-automatically-send-sms-without-the-user-need-to-approve
     */
    public void btnTextAllContacts(View view) {
        SmsManager manager = SmsManager.getDefault();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.i(TAG, "Sending SMS to: " + phoneNumber);

            String smsText = "Hey " + name + ", you need to try DownloadDare!";
            manager.sendTextMessage(phoneNumber, null, smsText, null, null);
        }
        phones.close();
    }
}
