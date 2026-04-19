package com.pvzmod.vip;

import okhttp3.*;
import java.io.IOException;

public class TelegramSender {
    private static final String BOT_TOKEN = "8180924483:AAHt7ySle_GRAywhYP6KZJnCMzwIDegjQoA";
    private static final String CHAT_ID = "5970230338";
    private static final String TELEGRAM_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
    
    public static void sendDeviceInfo(String deviceId) {
        OkHttpClient client = new OkHttpClient();
        String message = "🆕 YANGI QURILMA!\nID: " + deviceId + "\nEmail: admin@pentest.uz";
        
        RequestBody body = RequestBody.create(
            MediaType.parse("application/x-www-form-urlencoded"),
            "chat_id=" + CHAT_ID + "&text=" + message
        );
        
        Request request = new Request.Builder()
            .url(TELEGRAM_URL)
            .post(body)
            .build();
            
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {}
            @Override public void onResponse(Call call, Response response) throws IOException {
                response.close();
            }
        });
    }
}
