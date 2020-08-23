package com.toolinc.openairmarket.persistence.sync.inject;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import com.toolinc.openairmarket.ChannelIds;
import com.toolinc.openairmarket.NotificationIds;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.common.NotificationUtil.ChannelProperties;
import com.toolinc.openairmarket.common.NotificationUtil.NotificationProperties;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import com.toolinc.openairmarket.persistence.cloud.inject.SyncRepositoryModule;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Brands;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Categories;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Manufacturers;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.MeasureUnit;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Products;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncStateRepository;
import com.toolinc.openairmarket.persistence.local.pos.PosDatabaseModule;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomBrandDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomCategoryDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomManufacturerDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomMeasureUnitDao;
import com.toolinc.openairmarket.persistence.sync.BrandDataSync;
import com.toolinc.openairmarket.persistence.sync.CategoryDataSync;
import com.toolinc.openairmarket.persistence.sync.DataSync;
import com.toolinc.openairmarket.persistence.sync.ManufacturerDataSync;
import com.toolinc.openairmarket.persistence.sync.MeasureUnitDataSync;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Named;
import javax.inject.Singleton;

@InstallIn(ApplicationComponent.class)
@Module(includes = {SyncRepositoryModule.class, PosDatabaseModule.class})
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
  BrandDataSync providesProductBrandsSync(
      ProductRoomBrandDao productRoomBrandDao,
      @ApplicationContext Context context,
      @Brands SyncRepository syncRepository,
      @Named("DataSync") ChannelProperties channelProperties) {
    NotificationProperties brandStart =
        START_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_START)
            .setTitle(
                getString(
                    context, R.string.product_brand_data_sync_notification_inprogress_title))
            .setContent(
                getString(
                    context,
                    R.string.product_brand_data_sync_notification_inprogress_content))
            .build();

    NotificationProperties brandSuccess =
        SUCCESS_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_SUCCESS)
            .setTitle(
                getString(
                    context, R.string.product_brand_data_sync_notification_success_title))
            .setContent(
                getString(
                    context, R.string.product_brand_data_sync_notification_success_content))
            .build();

    NotificationProperties brandFailure =
        FAILURE_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_FAILED)
            .setTitle(
                getString(
                    context, R.string.product_brand_data_sync_notification_failure_title))
            .setContent(
                getString(
                    context, R.string.product_brand_data_sync_notification_failure_content))
            .build();

    return new BrandDataSync(
        productRoomBrandDao,
        syncRepository,
        channelProperties,
        brandStart,
        brandSuccess,
        brandFailure);
  }

  @Provides
  CategoryDataSync providesProductCategoriesSync(
      ProductRoomCategoryDao productRoomCategoryDao,
      @ApplicationContext Context context,
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

    return new CategoryDataSync(
        productRoomCategoryDao,
        syncRepository,
        channelProperties,
        brandStart,
        brandSuccess,
        brandFailure);
  }

  @Provides
  ManufacturerDataSync providesProductManufacturerDataSync(
      ProductRoomManufacturerDao productRoomManufacturerDao,
      @ApplicationContext Context context,
      @Manufacturers SyncRepository syncRepository,
      @Named("DataSync") ChannelProperties channelProperties) {
    NotificationProperties manufacturerStart =
        START_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_START)
            .setTitle(
                getString(
                    context, R.string.product_manufacturer_data_sync_notification_inprogress_title))
            .setContent(
                getString(
                    context,
                    R.string.product_manufacturer_data_sync_notification_inprogress_content))
            .build();

    NotificationProperties manufacturerSuccess =
        SUCCESS_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_SUCCESS)
            .setTitle(
                getString(
                    context, R.string.product_manufacturer_data_sync_notification_success_title))
            .setContent(
                getString(
                    context, R.string.product_manufacturer_data_sync_notification_success_content))
            .build();

    NotificationProperties manufacturerFailure =
        FAILURE_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_FAILED)
            .setTitle(
                getString(
                    context, R.string.product_manufacturer_data_sync_notification_failure_title))
            .setContent(
                getString(
                    context, R.string.product_manufacturer_data_sync_notification_failure_content))
            .build();

    return new ManufacturerDataSync(
        productRoomManufacturerDao,
        syncRepository,
        channelProperties,
        manufacturerStart,
        manufacturerSuccess,
        manufacturerFailure);
  }

  @Provides
  MeasureUnitDataSync providesProductMeasureUnitDataSync(
      ProductRoomMeasureUnitDao productRoomMeasureUnitDao,
      @ApplicationContext Context context,
      @MeasureUnit SyncRepository syncRepository,
      @Named("DataSync") ChannelProperties channelProperties) {
    NotificationProperties measureUnitStart =
        START_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_START)
            .setTitle(
                getString(
                    context, R.string.product_measure_unit_data_sync_notification_inprogress_title))
            .setContent(
                getString(
                    context,
                    R.string.product_measure_unit_data_sync_notification_inprogress_content))
            .build();

    NotificationProperties measureUnitSuccess =
        SUCCESS_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_SUCCESS)
            .setTitle(
                getString(
                    context, R.string.product_measure_unit_data_sync_notification_success_title))
            .setContent(
                getString(
                    context, R.string.product_measure_unit_data_sync_notification_success_content))
            .build();

    NotificationProperties measureUnitFailure =
        FAILURE_BUILDER
            .setNotificationId(NotificationIds.DATA_SYNC_CATEGORIES_FAILED)
            .setTitle(
                getString(
                    context, R.string.product_measure_unit_data_sync_notification_failure_title))
            .setContent(
                getString(
                    context, R.string.product_measure_unit_data_sync_notification_failure_content))
            .build();

    return new MeasureUnitDataSync(
        productRoomMeasureUnitDao,
        syncRepository,
        channelProperties,
        measureUnitStart,
        measureUnitSuccess,
        measureUnitFailure);
  }

  @Provides
  @Products
  DataSync providesProductsSync(
      @ApplicationContext Context context,
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
        syncRepository,
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
