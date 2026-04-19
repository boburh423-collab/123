package com.pvzmod.vip;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import java.security.MessageDigest;

public class DeviceIdGenerator {
    public static String generateUniqueId(Context context) {
        String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        id += Build.SERIAL + Build.MODEL + Build.MANUFACTURER;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(id.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));
            return hex.toString().substring(0, 16);
        } catch (Exception e) {
            return id;
        }
    }
}
