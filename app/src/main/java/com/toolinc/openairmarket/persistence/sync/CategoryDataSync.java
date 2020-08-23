package com.toolinc.openairmarket.persistence.sync;

import android.database.SQLException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.toolinc.openairmarket.common.NotificationUtil.ChannelProperties;
import com.toolinc.openairmarket.common.NotificationUtil.NotificationProperties;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomCategoryDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomCategory;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductCategory;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

/** Keeps the local information of categories data fresh. */
public class CategoryDataSync extends DataSync {

  private static final String TAG = CategoryDataSync.class.getSimpleName();

  private final ProductRoomCategoryDao productRoomCategoryDao;

  @Inject
  public CategoryDataSync(
      ProductRoomCategoryDao productRoomCategoryDao,
      SyncRepository syncRepository,
      ChannelProperties channelProperties,
      NotificationProperties startNotification,
      NotificationProperties successNotification,
      NotificationProperties failureNotification) {
    super(syncRepository, channelProperties, startNotification, successNotification,
        failureNotification);
    this.productRoomCategoryDao = productRoomCategoryDao;
  }

  @Override
  public void store(List<DocumentSnapshot> documentSnapshots) {
    List<ProductCategory> productCategories = SyncRepository
        .toProductCategories(documentSnapshots);
    for (ProductCategory productCategory : productCategories) {
      ProductRoomCategory.Builder builder = ProductRoomCategory.builder();
      builder.setProductCategory(productCategory);
      try {
        productRoomCategoryDao.insert(builder.build());
      } catch (SQLException exc) {
        Timber.tag(TAG).w(exc);
      }
    }
  }
}
