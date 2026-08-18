package com.arena.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "tournament_channel";
    private static final String CHANNEL_NAME = "Tournament Notifications";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();
        String title = data.containsKey("title") ? data.get("title") : "🏆 New Tournament!";
        String body  = data.containsKey("body")  ? data.get("body")  : "";
        String imageUrl = data.containsKey("image") ? data.get("image") : "";
        String roomId   = data.containsKey("roomId") ? data.get("roomId") : "";
        String roomPass = data.containsKey("roomPassword") ? data.get("roomPassword") : "";

        // Room ID aur Password body mein add karo
        if (!roomId.isEmpty())   body += "\n🔑 Room ID: " + roomId;
        if (!roomPass.isEmpty()) body += "\n🔒 Password: " + roomPass;

        // Notification ka notification bhi check karo
        if (remoteMessage.getNotification() != null) {
            if (title.isEmpty()) title = remoteMessage.getNotification().getTitle();
            if (body.isEmpty())  body  = remoteMessage.getNotification().getBody();
        }

        sendNotification(title, body, imageUrl);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Token WebView ke through Firebase DB mein save hoga
    }

    private void sendNotification(String title, String body, String imageUrl) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        // Image download karke notification mein dikhao
        if (!imageUrl.isEmpty()) {
            try {
                Bitmap bitmap = getBitmapFromUrl(imageUrl);
                if (bitmap != null) {
                    builder.setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)
                            .setSummaryText(body));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android 8+ ke liye channel banao
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            return null;
        }
    }
}
