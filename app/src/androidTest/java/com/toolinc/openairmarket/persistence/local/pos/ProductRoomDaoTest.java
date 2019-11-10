package com.toolinc.openairmarket.persistence.local.pos;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.toolinc.openairmarket.common.reactivex.TrampolineSchedulerRule;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomBrandDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomCategoryDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomManufacturerDao;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomMeasureUnitDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoom;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomBrand;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomCategory;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomManufacturer;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomMeasureUnit;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomPurchasePrice;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomSalePrice;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import io.reactivex.Completable;

/** Tests for {@link ProductRoomDao}. */
@RunWith(AndroidJUnit4.class)
public class ProductRoomDaoTest {
  @Rule
  public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  @Rule public final TrampolineSchedulerRule schedulerRule = new TrampolineSchedulerRule();

  private static final String[] PRODUCTS = new String[] {"A", "B", "C", "D", "E"};
  private static final ProductRoomBrand.Builder builderBrand =
      ProductRoomBrand.builder()
          .setId("NEW_BRAND")
          .setReferenceId("REF ID")
          .setName("Name of Brand")
          .setActive(true);
  private static final ProductRoomCategory.Builder builderCategory =
      ProductRoomCategory.builder()
          .setId("NEW_CATEGORY")
          .setReferenceId("REF ID")
          .setName("Name of Category")
          .setActive(true);
  private static final ProductRoomMeasureUnit.Builder builderMeasureUnit =
      ProductRoomMeasureUnit.builder()
          .setId("NEW_MEASURE_UNIT")
          .setReferenceId("REF ID")
          .setName("Name of Measure Unit")
          .setCountable(true)
          .setActive(true);
  private static final ProductRoomManufacturer.Builder builderManufacturer =
      ProductRoomManufacturer.builder()
          .setId("NEW_MANUFACTURER")
          .setReferenceId("REF ID")
          .setName("Name of Manufacturer")
          .setActive(true);
  private static final ProductRoom.Builder builder =
      ProductRoom.builder()
          .setId("NEW")
          .setReferenceId("REF ID")
          .setName("Name of Category")
          .setActive(true)
          .setImage("Image")
          .setProductType(ProductType.ITEM)
          .setProductPurchasePrice(
              ProductRoomPurchasePrice.create(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE))
          .setProductSalePrice(
              ProductRoomSalePrice.create(
                  BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE));

  private PosRoomDatabase db;
  private ProductRoomBrandDao productRoomBrandDao;
  private ProductRoomCategoryDao productRoomCategoryDao;
  private ProductRoomManufacturerDao productRoomManufacturerDao;
  private ProductRoomMeasureUnitDao productRoomMeasureUnitDao;
  private ProductRoomDao productRoomDao;

  @Before
  public void setUp() {
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    db =
        Room.inMemoryDatabaseBuilder(context, PosRoomDatabase.class)
            .allowMainThreadQueries()
            .build();
    productRoomBrandDao = db.productRoomBrandDao();
    productRoomCategoryDao = db.productRoomCategoryDao();
    productRoomManufacturerDao = db.productRoomManufacturerDao();
    productRoomMeasureUnitDao = db.productRoomMeasureUnitDao();
    productRoomDao = db.productRoomDao();
  }

  @Test
  public void insert_record() {
    insert_entities();

    ProductRoom product =
        builder
            .setProductBrand(builderBrand.build().id())
            .setProductCategory(builderCategory.build().id())
            .setProductMeasureUnit(builderMeasureUnit.build().id())
            .build();

    Completable completable = productRoomDao.insert(product);
    completable.test().assertComplete();
  }

  private void insert_entities() {
    Completable completable = productRoomManufacturerDao.insert(builderManufacturer.build());
    completable.test().assertComplete();

    completable =
        productRoomBrandDao.insert(
            builderBrand.setProductManufacturerId(builderManufacturer.build().id()).build());
    completable.test().assertComplete();

    completable = productRoomCategoryDao.insert(builderCategory.build());
    completable.test().assertComplete();

    completable = productRoomMeasureUnitDao.insert(builderMeasureUnit.build());
    completable.test().assertComplete();
  }
}
