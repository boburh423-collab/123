package com.pvzmod.game;

import android.content.Context;
import android.content.SharedPreferences;
import java.security.MessageDigest;

public class PinManager {
    private static final String PREFS_NAME = "pvzmod_prefs";
    private static final String PIN_KEY = "device_pin";

    public static String generateUniquePin(String deviceId) {
        try {
            String raw = deviceId + System.currentTimeMillis();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes());
            int pinInt = Math.abs((hash[0] & 0xff) * 1000 + (hash[1] & 0xff) * 10 + (hash[2] & 0xff));
            String pin = String.format("%06d", pinInt % 1000000);
            
            SharedPreferences prefs = PvZApplication.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putString(PIN_KEY, pin).apply();
            
            return pin;
        } catch (Exception e) {
            return "123456";
        }
    }

    public static String getStoredPin(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PIN_KEY, "123456");
    }
}