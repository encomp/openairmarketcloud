package com.toolinc.openairmarket.ui.fragment.inject;

import android.app.Application;
import android.app.NotificationManager;

import com.toolinc.openairmarket.ChannelIds;
import com.toolinc.openairmarket.NotificationIds;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.common.NotificationUtil.ChannelProperties;
import com.toolinc.openairmarket.common.NotificationUtil.NotificationProperties;
import com.toolinc.openairmarket.ui.fragment.inject.Annotations.Sale;
import com.toolinc.openairmarket.ui.fragment.inject.Annotations.Sale.Failed;
import com.toolinc.openairmarket.ui.fragment.inject.Annotations.Sale.Succeed;

import dagger.Module;
import dagger.Provides;

@Module
public class SaleNotificationModule {

  @Provides
  @Sale
  ChannelProperties provideChannel(Application application) {
    return channelBuilder()
        .setChannelName(getString(application, R.string.sale_channel_name))
        .setChannelDesc(getString(application, R.string.sale_channel_description))
        .build();
  }

  @Provides
  @Succeed
  NotificationProperties providesSuccessful(Application application) {
    return notiBuilder()
        .setIcon(R.drawable.ic_shopping_cart)
        .setNotificationId(NotificationIds.SALE_COMPLETED)
        .setTitle(getString(application, R.string.sale_notification_success_title))
        .setContent(getString(application, R.string.sale_notification_success_content))
        .build();
  }

  @Provides
  @Failed
  NotificationProperties providesFailure(Application application) {
    return notiBuilder()
        .setIcon(R.drawable.ic_remove_shopping)
        .setNotificationId(NotificationIds.SALE_FAILED)
        .setTitle(getString(application, R.string.sale_notification_failure_title))
        .setContent(getString(application, R.string.sale_notification_failure_content))
        .build();
  }

  private static final ChannelProperties.Builder channelBuilder() {
    return ChannelProperties.builder()
        .setChannelId(ChannelIds.SALE)
        .setImportance(NotificationManager.IMPORTANCE_HIGH);
  }

  private static final NotificationProperties.Builder notiBuilder() {
    return NotificationProperties.builder()
        .setChannelId(ChannelIds.SALE)
        .setPriority(NotificationManager.IMPORTANCE_HIGH);
  }

  private String getString(Application application, int resId) {
    return application.getString(resId);
  }
}