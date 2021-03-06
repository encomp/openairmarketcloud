package com.toolinc.openairmarket.persistence.sync;

import android.database.SQLException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.toolinc.openairmarket.common.NotificationUtil.ChannelProperties;
import com.toolinc.openairmarket.common.NotificationUtil.NotificationProperties;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomManufacturerDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomManufacturer;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductManufacturer;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

/** Keeps the local information of manufacturers data fresh. */
public class ManufacturerDataSync extends DataSync {

  private static final String TAG = ManufacturerDataSync.class.getSimpleName();

  private final ProductRoomManufacturerDao productRoomManufacturerDao;

  @Inject
  public ManufacturerDataSync(
      ProductRoomManufacturerDao productRoomManufacturerDao,
      SyncRepository syncRepository,
      ChannelProperties channelProperties,
      NotificationProperties startNotification,
      NotificationProperties successNotification,
      NotificationProperties failureNotification) {
    super(syncRepository, channelProperties, startNotification, successNotification,
        failureNotification);
    this.productRoomManufacturerDao = productRoomManufacturerDao;
  }

  @Override
  public void store(List<DocumentSnapshot> documentSnapshots) {
    List<ProductManufacturer> productManufacturers = SyncRepository
        .toProductManufacturers(documentSnapshots);
    for (ProductManufacturer productManufacturer : productManufacturers) {
      ProductRoomManufacturer.Builder builder = ProductRoomManufacturer.builder();
      builder.setProductManufacturer(productManufacturer);
      try {
        productRoomManufacturerDao.insert(builder.build());
      } catch (SQLException exc) {
        Timber.tag(TAG).w(exc);
      }
    }
  }
}
