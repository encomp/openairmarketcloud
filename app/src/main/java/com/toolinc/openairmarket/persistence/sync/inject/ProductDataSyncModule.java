package com.toolinc.openairmarket.persistence.sync.inject;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.toolinc.openairmarket.ChannelIds;
import com.toolinc.openairmarket.NotificationIds;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.common.NotificationUtil.ChannelProperties;
import com.toolinc.openairmarket.common.NotificationUtil.NotificationProperties;
import com.toolinc.openairmarket.common.inject.Global;
import com.toolinc.openairmarket.persistence.cloud.CollectionsNames;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import com.toolinc.openairmarket.persistence.cloud.inject.SyncRepositoryModule;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Categories;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Products;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.sync.DataSync;

import java.util.concurrent.Executor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@InstallIn(FragmentComponent.class)
@Module(includes = SyncRepositoryModule.class)
public class ProductDataSyncModule {
  private static final NotificationProperties.Builder START_BUILDER =
      notiBuilder().setIcon(R.drawable.ic_cloud_download);
  private static final NotificationProperties.Builder SUCCESS_BUILDER =
      notiBuilder().setIcon(R.drawable.ic_cloud_done);
  private static final NotificationProperties.Builder FAILURE_BUILDER =
      notiBuilder().setIcon(R.drawable.ic_cloud_off);

  @Singleton
  @Provides
  @Named("DataSync")
  ChannelProperties provideChannel(@ApplicationContext Context context) {
    return channelBuilder()
        .setChannelName(getString(context, R.string.data_sync_channel_name))
        .setChannelDesc(getString(context, R.string.data_sync_channel_description))
        .build();
  }

  @Provides
  @Categories
  DataSync providesProductCategoriesSync(
      @ApplicationContext Context context,
      @Global.NetworkIO Executor executor,
      @Categories SyncRepository syncRepository,
      CollectionSyncStateRepository collectionSyncStateRepository,
      @Named("DataSync") ChannelProperties channelProperties) {
    NotificationProperties brandStart =
        START_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_START)
            .setTitle(
                getString(
                    context, R.string.product_category_data_sync_notification_inprogress_title))
            .setContent(
                getString(
                    context,
                    R.string.product_category_data_sync_notification_inprogress_content))
            .build();

    NotificationProperties brandSuccess =
        SUCCESS_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_SUCCESS)
            .setTitle(
                getString(
                    context, R.string.product_category_data_sync_notification_success_title))
            .setContent(
                getString(
                    context, R.string.product_category_data_sync_notification_success_content))
            .build();

    NotificationProperties brandFailure =
        FAILURE_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_FAILED)
            .setTitle(
                getString(
                    context, R.string.product_category_data_sync_notification_failure_title))
            .setContent(
                getString(
                    context, R.string.product_category_data_sync_notification_failure_content))
            .build();

    return new DataSync(
        executor,
        CollectionsNames.PRODUCT_CATEGORIES,
        syncRepository,
        collectionSyncStateRepository,
        channelProperties,
        brandStart,
        brandSuccess,
        brandFailure);
  }

  @Provides
  @Products
  DataSync providesProductsSync(
      @ApplicationContext Context context,
      @Global.NetworkIO Executor executor,
      @Products SyncRepository syncRepository,
      CollectionSyncStateRepository collectionSyncStateRepository,
      @Named("DataSync") ChannelProperties channelProperties) {
    NotificationProperties brandStart =
        START_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_PRODUCTS_START)
            .setTitle(
                getString(context, R.string.products_data_sync_notification_inprogress_title))
            .setContent(
                getString(context, R.string.products_data_sync_notification_inprogress_content))
            .build();

    NotificationProperties brandSuccess =
        SUCCESS_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_PRODUCTSY_SUCCESS)
            .setTitle(
                getString(context, R.string.products_data_sync_notification_success_title))
            .setContent(
                getString(context, R.string.products_data_sync_notification_success_content))
            .build();

    NotificationProperties brandFailure =
        FAILURE_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_PRODUCTS_FAILED)
            .setTitle(
                getString(context, R.string.products_data_sync_notification_failure_title))
            .setContent(
                getString(context, R.string.products_data_sync_notification_failure_content))
            .build();

    return new DataSync(
        executor,
        CollectionsNames.PRODUCTS,
        syncRepository,
        collectionSyncStateRepository,
        channelProperties,
        brandStart,
        brandSuccess,
        brandFailure);
  }

  private static final ChannelProperties.Builder channelBuilder() {
    ChannelProperties.Builder builder =
        ChannelProperties.builder().setChannelId(ChannelIds.DATA_SYNC);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      builder.setImportance(NotificationManager.IMPORTANCE_HIGH);
    }
    return builder;
  }

  private static final NotificationProperties.Builder notiBuilder() {
    NotificationProperties.Builder builder =
        NotificationProperties.builder().setChannelId(ChannelIds.DATA_SYNC);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
    }
    return builder;
  }

  private String getString(Context context, int resId) {
    return context.getString(resId);
  }
}
