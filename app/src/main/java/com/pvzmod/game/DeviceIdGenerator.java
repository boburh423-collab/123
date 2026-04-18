package com.pvzmod.game;

import android.content.Context;
import android.provider.Settings;
import java.util.UUID;

public class DeviceIdGenerator {
    public static String getUniqueId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String serial = Build.SERIAL != null ? Build.SERIAL : "unknown";
        return UUID.nameUUIDFromBytes((androidId + serial).getBytes()).toString().replace("-", "");
    }
}