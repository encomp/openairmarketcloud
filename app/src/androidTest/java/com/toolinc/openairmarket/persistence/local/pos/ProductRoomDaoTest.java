package com.toolinc.openairmarket.persistence.local.pos;

import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;
import androidx.room.Room;
import androidx.test.espresso.web.internal.deps.guava.collect.ImmutableList;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.google.common.collect.Lists;
import com.toolinc.openairmarket.common.lifecycle.LiveDataTestUtil;
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
import io.reactivex.Maybe;
import io.reactivex.Observable;
import java.math.BigDecimal;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link ProductRoomDao}.
 */
@RunWith(AndroidJUnit4.class)
public class ProductRoomDaoTest {

  private static final String[] PRODUCTS = new String[]{"A", "B", "C", "D", "E"};
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
  @Rule
  public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  @Rule
  public final TrampolineSchedulerRule schedulerRule = new TrampolineSchedulerRule();
  private ProductRoom.Builder builder;

  private PosRoomDatabase db;
  private ProductRoomBrandDao productRoomBrandDao;
  private ProductRoomCategoryDao productRoomCategoryDao;
  private ProductRoomManufacturerDao productRoomManufacturerDao;
  private ProductRoomMeasureUnitDao productRoomMeasureUnitDao;
  private ProductRoomDao productRoomDao;

  private static final ImmutableList<ProductRoom> create() {
    BigDecimal uno = new BigDecimal("1.0");
    ImmutableList.Builder<ProductRoom> builder = ImmutableList.builder();
    for (String product : PRODUCTS) {
      ProductRoom.Builder builderProduct =
          ProductRoom.builder()
              .setId("NEW - " + product)
              .setReferenceId("REF ID - " + product)
              .setName("NAME - " + product)
              .setActive(true)
              .setImage("Image")
              .setProductType(ProductType.ITEM)
              .setProductPurchasePrice(ProductRoomPurchasePrice.create(uno, uno, uno))
              .setProductSalePrice(ProductRoomSalePrice.create(uno, uno, uno, uno))
              .setProductBrand(builderBrand.build().id())
              .setProductCategory(builderCategory.build().id())
              .setProductMeasureUnit(builderMeasureUnit.build().id());
      builder.add(builderProduct.build());
    }
    return builder.build();
  }

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

    BigDecimal uno = new BigDecimal("1.0");
    builder =
        ProductRoom.builder()
            .setId("NEW")
            .setReferenceId("REF ID")
            .setName("Name of Product")
            .setActive(true)
            .setImage("Image")
            .setProductType(ProductType.ITEM)
            .setProductPurchasePrice(ProductRoomPurchasePrice.create(uno, uno, uno))
            .setProductSalePrice(ProductRoomSalePrice.create(uno, uno, uno, uno));
  }

  @After
  public void teardown() {
    db.close();
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

    productRoomDao.insert(product);
  }

  @Test
  public void delete_record() {
    insert_entities();

    ProductRoom product =
        builder
            .setProductBrand(builderBrand.build().id())
            .setProductCategory(builderCategory.build().id())
            .setProductMeasureUnit(builderMeasureUnit.build().id())
            .build();

    productRoomDao.insert(product);

    productRoomDao.delete(product);
  }

  @Test
  public void update_record() {
    insert_entities();

    ProductRoom product =
        builder
            .setProductBrand(builderBrand.build().id())
            .setProductCategory(builderCategory.build().id())
            .setProductMeasureUnit(builderMeasureUnit.build().id())
            .build();

    productRoomDao.insert(product);

    productRoomDao.update(product.toBuilder().setName("Other").build());

    Maybe<ProductRoom> maybe = productRoomDao.findById(product.id());
    maybe.test().assertValue(product.toBuilder().setName("Other").build());
  }

  @Test
  public void all_records() {
    insert_entities();

    ImmutableList<ProductRoom> products = create();
    for (ProductRoom productRoom : products) {
      productRoomDao.insert(productRoom);
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    Observable<PagedList<ProductRoom>> result =
        new RxPagedListBuilder(productRoomDao.all(), config).buildObservable();
    PagedList<ProductRoom> productsRoom = result.blockingFirst();
    assertThat(Lists.newArrayList(productsRoom.iterator())).containsAtLeastElementsIn(products);
  }

  @Test
  public void all_records_withLiveData() throws InterruptedException {
    insert_entities();

    ImmutableList<ProductRoom> products = create();
    for (ProductRoom productRoom : products) {
      productRoomDao.insert(productRoom);
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    LiveData<PagedList<ProductRoom>> listLiveData =
        new LivePagedListBuilder<>(productRoomDao.all(), config).build();
    PagedList<ProductRoom> pagedList = LiveDataTestUtil.blocking(listLiveData);
    assertThat(Lists.newArrayList(pagedList.iterator())).containsAtLeastElementsIn(products);
  }

  private void insert_entities() {
    productRoomManufacturerDao.insert(builderManufacturer.build());

    productRoomBrandDao.insert(
        builderBrand.setProductManufacturerId(builderManufacturer.build().id()).build());

    productRoomCategoryDao.insert(builderCategory.build());

    productRoomMeasureUnitDao.insert(builderMeasureUnit.build());
  }

  @Test
  public void findById_record() {
    insert_entities();

    ProductRoom product =
        builder
            .setProductBrand(builderBrand.build().id())
            .setProductCategory(builderCategory.build().id())
            .setProductMeasureUnit(builderMeasureUnit.build().id())
            .build();

    productRoomDao.insert(product);

    Maybe<ProductRoom> maybe = productRoomDao.findById(product.id());
    maybe.test().assertValue(product);
  }

  @Test
  public void findById_referenceId() {
    insert_entities();

    ProductRoom product =
        builder
            .setProductBrand(builderBrand.build().id())
            .setProductCategory(builderCategory.build().id())
            .setProductMeasureUnit(builderMeasureUnit.build().id())
            .build();

    productRoomDao.insert(product);

    Maybe<ProductRoom> maybe = productRoomDao.findByReferenceId(product.referenceId());
    maybe.test().assertValue(product);
  }

  @Test
  public void findAllLikeName_records() {
    insert_entities();

    ImmutableList<ProductRoom> products = create();
    for (ProductRoom productRoom : products) {
      productRoomDao.insert(productRoom);
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    Observable<PagedList<ProductRoom>> result =
        new RxPagedListBuilder(productRoomDao.findAllLikeName("NAME -"), config).buildObservable();
    PagedList<ProductRoom> productsRoom = result.blockingFirst();
    assertThat(Lists.newArrayList(productsRoom.iterator())).containsAtLeastElementsIn(products);
  }
}
