package com.pvzmod.vip;

import android.content.SharedPreferences;
import android.content.Context;

public class PinManager {
    public static void saveAttempts(int attempts) {
        SharedPreferences prefs = PvZApplication.prefs;
        prefs.edit().putInt("failed_attempts", attempts).apply();
    }
    
    public static int loadAttempts() {
        return PvZApplication.prefs.getInt("failed_attempts", 0);
    }
    
    public static void blockUntil(long timestamp) {
        PvZApplication.prefs.edit().putLong("block_until", timestamp).apply();
    }
    
    public static boolean isBlocked() {
        long blockUntil = PvZApplication.prefs.getLong("block_until", 0);
        return System.currentTimeMillis() < blockUntil;
    }
}
