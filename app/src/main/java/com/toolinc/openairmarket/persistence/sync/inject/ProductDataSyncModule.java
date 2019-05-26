package com.toolinc.openairmarket.persistence.sync.inject;

import android.app.Application;
import android.app.NotificationManager;

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

import dagger.Module;
import dagger.Provides;

@Module(includes = SyncRepositoryModule.class)
public class ProductDataSyncModule {
  private static final NotificationProperties.Builder START_BUILDER =
      notiBuilder().setIcon(R.drawable.ic_cloud_download);
  private static final NotificationProperties.Builder SUCCESS_BUILDER =
      notiBuilder().setIcon(R.drawable.ic_cloud_done);
  private static final NotificationProperties.Builder FAILURE_BUILDER =
      notiBuilder().setIcon(R.drawable.ic_cloud_off);

  @Provides
  @Named("DataSync")
  ChannelProperties provideChannel(Application application) {
    return channelBuilder()
        .setChannelName(getString(application, R.string.data_sync_channel_name))
        .setChannelDesc(getString(application, R.string.data_sync_channel_description))
        .build();
  }

  @Provides
  @Categories
  DataSync providesProductCategoriesSync(
      Application application,
      @Global.NetworkIO Executor executor,
      @Categories SyncRepository syncRepository,
      CollectionSyncStateRepository collectionSyncStateRepository,
      @Named("DataSync") ChannelProperties channelProperties) {
    NotificationProperties brandStart =
        START_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_START)
            .setTitle(
                getString(
                    application, R.string.product_category_data_sync_notification_inprogress_title))
            .setContent(
                getString(
                    application,
                    R.string.product_category_data_sync_notification_inprogress_content))
            .build();

    NotificationProperties brandSuccess =
        SUCCESS_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_SUCCESS)
            .setTitle(
                getString(
                    application, R.string.product_category_data_sync_notification_success_title))
            .setContent(
                getString(
                    application, R.string.product_category_data_sync_notification_success_content))
            .build();

    NotificationProperties brandFailure =
        FAILURE_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_FAILED)
            .setTitle(
                getString(
                    application, R.string.product_category_data_sync_notification_failure_title))
            .setContent(
                getString(
                    application, R.string.product_category_data_sync_notification_failure_content))
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
      Application application,
      @Global.NetworkIO Executor executor,
      @Products SyncRepository syncRepository,
      CollectionSyncStateRepository collectionSyncStateRepository,
      @Named("DataSync") ChannelProperties channelProperties) {
    NotificationProperties brandStart =
        START_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_PRODUCTS_START)
            .setTitle(
                getString(application, R.string.products_data_sync_notification_inprogress_title))
            .setContent(
                getString(application, R.string.products_data_sync_notification_inprogress_content))
            .build();

    NotificationProperties brandSuccess =
        SUCCESS_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_PRODUCTSY_SUCCESS)
            .setTitle(
                getString(application, R.string.products_data_sync_notification_success_title))
            .setContent(
                getString(application, R.string.products_data_sync_notification_success_content))
            .build();

    NotificationProperties brandFailure =
        FAILURE_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_PRODUCTS_FAILED)
            .setTitle(
                getString(application, R.string.products_data_sync_notification_failure_title))
            .setContent(
                getString(application, R.string.products_data_sync_notification_failure_content))
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
    return ChannelProperties.builder()
        .setChannelId(ChannelIds.DATA_SYNC)
        .setImportance(NotificationManager.IMPORTANCE_HIGH);
  }

  private static final NotificationProperties.Builder notiBuilder() {
    return NotificationProperties.builder()
        .setChannelId(ChannelIds.DATA_SYNC)
        .setPriority(NotificationManager.IMPORTANCE_HIGH);
  }

  private String getString(Application application, int resId) {
    return application.getString(resId);
  }
}
