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
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomManufacturerDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomBrand;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomManufacturer;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link ProductRoomBrandDao}.
 */
@RunWith(AndroidJUnit4.class)
public class ProductRoomBrandDaoTest {

  private static final String[] PRODUCTS = new String[]{"A", "B", "C", "D", "E"};
  private static final ProductRoomManufacturer.Builder builderManufacturer =
      ProductRoomManufacturer.builder()
          .setId("NEW")
          .setReferenceId("REF ID")
          .setName("Name of Manufacturer")
          .setActive(true);
  private static final ProductRoomBrand.Builder builder =
      ProductRoomBrand.builder()
          .setId("NEW")
          .setReferenceId("REF ID")
          .setName("Name of Category")
          .setActive(true);
  @Rule
  public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  @Rule
  public final TrampolineSchedulerRule schedulerRule = new TrampolineSchedulerRule();
  private PosRoomDatabase db;
  private ProductRoomManufacturerDao productRoomManufacturerDao;
  private ProductRoomBrandDao productRoomBrandDao;

  private static final ImmutableList<ProductRoomBrand> create() {
    ImmutableList.Builder<ProductRoomBrand> builder = ImmutableList.builder();
    for (String product : PRODUCTS) {
      ProductRoomBrand.Builder builderProduct =
          ProductRoomBrand.builder()
              .setId("NEW - " + product)
              .setReferenceId("REF ID - " + product)
              .setName("NAME - " + product)
              .setActive(true);
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
    productRoomManufacturerDao = db.productRoomManufacturerDao();
    productRoomBrandDao = db.productRoomBrandDao();
  }

  @Test
  public void insert_record() {
    ProductRoomBrand productBrand = builder.setProductManufacturerId(null).build();

    productRoomBrandDao.insert(productBrand);
  }

  @Test
  public void insert_withManufacturer_record() {
    productRoomManufacturerDao.insert(builderManufacturer.build());

    ProductRoomBrand productBrand =
        builder.setProductManufacturer(builderManufacturer.build()).build();

    productRoomBrandDao.insert(productBrand);
  }

  @Test
  public void delete_record() {
    ProductRoomBrand productRoomBrand = builder.setProductManufacturerId(null).build();

    productRoomBrandDao.insert(productRoomBrand);

    productRoomBrandDao.delete(productRoomBrand);
  }

  @Test
  public void update_record() {
    ProductRoomBrand productRoomBrand = builder.setProductManufacturerId(null).build();

    productRoomBrandDao.insert(productRoomBrand);

    productRoomBrandDao.update(productRoomBrand.toBuilder().setName("Other").build());

    Maybe<ProductRoomBrand> maybe = productRoomBrandDao.findById(productRoomBrand.id());
    maybe.test().assertValue(productRoomBrand.toBuilder().setName("Other").build());
  }

  @Test
  public void all_records() {
    ImmutableList<ProductRoomBrand> products = create();
    for (ProductRoomBrand productRoomBrand : products) {
      productRoomBrandDao.insert(productRoomBrand);
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    Observable<PagedList<ProductRoomBrand>> result =
        new RxPagedListBuilder(productRoomBrandDao.all(), config).buildObservable();
    PagedList<ProductRoomBrand> productRoomBrands = result.blockingFirst();
    assertThat(Lists.newArrayList(productRoomBrands.iterator()))
        .containsAtLeastElementsIn(products);
  }

  @Test
  public void all_records_withLiveData() throws InterruptedException {
    ImmutableList<ProductRoomBrand> products = create();
    for (ProductRoomBrand productRoomBrand : products) {
      productRoomBrandDao.insert(productRoomBrand);
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    LiveData<PagedList<ProductRoomBrand>> listLiveData =
        new LivePagedListBuilder<>(productRoomBrandDao.all(), config).build();
    PagedList<ProductRoomBrand> pagedList = LiveDataTestUtil.blocking(listLiveData);
    assertThat(Lists.newArrayList(pagedList.iterator())).containsAtLeastElementsIn(products);
  }

  @Test
  public void findById_record() {
    ProductRoomBrand productRoomBrand = builder.build();

    productRoomBrandDao.insert(productRoomBrand);

    Maybe<ProductRoomBrand> maybe = productRoomBrandDao.findById(productRoomBrand.id());
    maybe.test().assertValue(productRoomBrand);
  }

  @Test
  public void findById_referenceId() {
    ProductRoomBrand productRoomBrand = builder.build();

    productRoomBrandDao.insert(productRoomBrand);

    Maybe<ProductRoomBrand> maybe =
        productRoomBrandDao.findByReferenceId(productRoomBrand.referenceId());
    maybe.test().assertValueCount(1);
  }

  @Test
  public void findAllLikeName_records() {
    ImmutableList<ProductRoomBrand> products = create();
    for (ProductRoomBrand productRoomBrand : products) {
      productRoomBrandDao.insert(productRoomBrand);
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    Observable<PagedList<ProductRoomBrand>> result =
        new RxPagedListBuilder(productRoomBrandDao.findAllLikeName("NAME -"), config)
            .buildObservable();
    PagedList<ProductRoomBrand> productRoomBrands = result.blockingFirst();
    assertThat(Lists.newArrayList(productRoomBrands.iterator()))
        .containsAtLeastElementsIn(products);
  }

  @After
  public void teardown() {
    db.close();
  }
}
