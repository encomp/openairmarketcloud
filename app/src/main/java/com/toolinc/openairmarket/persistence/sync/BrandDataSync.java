package com.toolinc.openairmarket.persistence.sync;

import android.database.SQLException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.toolinc.openairmarket.common.NotificationUtil.ChannelProperties;
import com.toolinc.openairmarket.common.NotificationUtil.NotificationProperties;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomBrandDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomBrand;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductBrand;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

/** Keeps the local information of brand data fresh. */
public class BrandDataSync extends DataSync {

  private static final String TAG = BrandDataSync.class.getSimpleName();

  private final ProductRoomBrandDao productRoomBrandDao;

  @Inject
  public BrandDataSync(
      ProductRoomBrandDao productRoomBrandDao,
      SyncRepository syncRepository,
      ChannelProperties channelProperties,
      NotificationProperties startNotification,
      NotificationProperties successNotification,
      NotificationProperties failureNotification) {
    super(syncRepository, channelProperties, startNotification, successNotification,
        failureNotification);
    this.productRoomBrandDao = productRoomBrandDao;
  }

  @Override
  public void store(List<DocumentSnapshot> documentSnapshots) {
    List<ProductBrand> productBrands = SyncRepository.toProductBrands(documentSnapshots);
    for (ProductBrand productBrand : productBrands) {
      ProductRoomBrand.Builder builder = ProductRoomBrand.builder();
      builder.setProductBrand(productBrand);
      try {
        productRoomBrandDao.insert(builder.build());
      } catch (SQLException exception) {
        Timber.tag(TAG).w(exception);
      }
    }
  }
}
