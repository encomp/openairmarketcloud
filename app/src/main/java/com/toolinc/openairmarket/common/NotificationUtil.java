package com.toolinc.openairmarket.common;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/** Notification utility that simplifies the render of notifications and channel definitions. */
public final class NotificationUtil {

  private NotificationUtil() {}

  /** Create a channel using {@link NotificationManager}. */
  public static final boolean createChannel(
      Context context, String channelId, String channelName, String channelDesc, int importance) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
      channel.setDescription(channelDesc);
      NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
      return true;
    }
    return false;
  }

  /** Renders a notification with a small icon, title, content and priority. */
  public static final void notify(
      Context context,
      int notificationId,
      String channelId,
      int icon,
      String title,
      String content,
      int priority) {
    NotificationCompat.Builder builder =
        new NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(priority);
    NotificationManagerCompat.from(context).notify(notificationId, builder.build());
  }
}
