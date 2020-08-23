package com.toolinc.openairmarket.persistence.sync;

import android.database.SQLException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.toolinc.openairmarket.common.NotificationUtil.ChannelProperties;
import com.toolinc.openairmarket.common.NotificationUtil.NotificationProperties;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomMeasureUnitDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomMeasureUnit;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductMeasureUnit;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

/** Keeps the local information of measure unit data fresh. */
public class MeasureUnitDataSync extends DataSync {

  private static final String TAG = MeasureUnitDataSync.class.getSimpleName();

  private final ProductRoomMeasureUnitDao productRoomMeasureUnitDao;

  @Inject
  public MeasureUnitDataSync(
      ProductRoomMeasureUnitDao productRoomMeasureUnitDao,
      SyncRepository syncRepository,
      ChannelProperties channelProperties,
      NotificationProperties startNotification,
      NotificationProperties successNotification,
      NotificationProperties failureNotification) {
    super(syncRepository, channelProperties, startNotification, successNotification,
        failureNotification);
    this.productRoomMeasureUnitDao = productRoomMeasureUnitDao;
  }

  @Override
  public void store(List<DocumentSnapshot> documentSnapshots) {
    List<ProductMeasureUnit> productMeasureUnits = SyncRepository
        .toProductMeasureUnits(documentSnapshots);
    for (ProductMeasureUnit productMeasureUnit : productMeasureUnits) {
      ProductRoomMeasureUnit.Builder builder = ProductRoomMeasureUnit.builder();
      builder.setProductMeasureUnit(productMeasureUnit);
      try {
        productRoomMeasureUnitDao.insert(builder.build());
      } catch (SQLException exc) {
        Timber.tag(TAG).w(exc);
      }
    }
  }
}
