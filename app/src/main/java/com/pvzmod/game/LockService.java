package com.pvzmod.game;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LockService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent lockIntent = new Intent(this, LockScreenActivity.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(lockIntent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}