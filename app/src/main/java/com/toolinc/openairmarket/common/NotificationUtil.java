package com.toolinc.openairmarket.common;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.auto.value.AutoValue;

/** Notification utility that simplifies the render of notifications and channel definitions. */
public final class NotificationUtil {

  private NotificationUtil() {}

  @AutoValue
  public abstract static class ChannelProperties {

    public abstract String channelId();

    public abstract String channelName();

    public abstract String channelDesc();

    public abstract int importance();

    public abstract Builder toBuilder();

    public static Builder builder() {
      return new AutoValue_NotificationUtil_ChannelProperties.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

      public abstract Builder setChannelId(String channelId);

      public abstract Builder setChannelName(String channelName);

      public abstract Builder setChannelDesc(String channelDesc);

      public abstract Builder setImportance(int importance);

      public abstract ChannelProperties build();
    }
  }

  @AutoValue
  public abstract static class NotificationProperties {

    public abstract int notificationId();

    public abstract String channelId();

    public abstract int icon();

    public abstract String title();

    public abstract String content();

    public abstract int priority();

    public abstract Builder toBuilder();

    public static Builder builder() {
      return new AutoValue_NotificationUtil_NotificationProperties.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

      public abstract Builder setNotificationId(int notificationId);

      public abstract Builder setChannelId(String channelId);

      public abstract Builder setIcon(int icon);

      public abstract Builder setTitle(String title);

      public abstract Builder setContent(String content);

      public abstract Builder setPriority(int priority);

      public abstract NotificationProperties build();
    }
  }

  /** Create a channel using {@link NotificationManager}. */
  public static final boolean createChannel(Context context, ChannelProperties properties) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel =
          new NotificationChannel(
              properties.channelId(), properties.channelName(), properties.importance());
      channel.setDescription(properties.channelDesc());
      NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
      return true;
    }
    return false;
  }

  /** Renders a notification with a small icon, title, content and priority. */
  public static final void notify(Context context, NotificationProperties properties) {
    NotificationCompat.Builder builder =
        new NotificationCompat.Builder(context, properties.channelId())
            .setSmallIcon(properties.icon())
            .setContentTitle(properties.title())
            .setContentText(properties.content())
            .setPriority(properties.priority());
    NotificationManagerCompat.from(context).notify(properties.notificationId(), builder.build());
  }
}
