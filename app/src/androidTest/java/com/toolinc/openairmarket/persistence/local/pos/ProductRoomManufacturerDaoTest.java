package com.toolinc.openairmarket.persistence.local.pos;

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
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomManufacturerDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomManufacturer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import static com.google.common.truth.Truth.assertThat;

/** Tests for {@link ProductRoomManufacturerDao}. */
@RunWith(AndroidJUnit4.class)
public class ProductRoomManufacturerDaoTest {

  @Rule
  public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  @Rule public final TrampolineSchedulerRule schedulerRule = new TrampolineSchedulerRule();

  private static final String[] PRODUCTS = new String[] {"A", "B", "C", "D", "E"};
  private static final ProductRoomManufacturer.Builder builder =
      ProductRoomManufacturer.builder()
          .setId("NEW")
          .setReferenceId("REF ID")
          .setName("Name of Manufacturer")
          .setActive(true);

  private PosRoomDatabase db;
  private ProductRoomManufacturerDao productRoomManufacturerDao;

  @Before
  public void setUp() {
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    db =
        Room.inMemoryDatabaseBuilder(context, PosRoomDatabase.class)
            .allowMainThreadQueries()
            .build();
    productRoomManufacturerDao = db.productRoomManufacturerDao();
  }

  @Test
  public void insert_record() {
    ProductRoomManufacturer productRoomManufacturer = builder.build();

    Completable completable = productRoomManufacturerDao.insert(productRoomManufacturer);
    completable.test().assertComplete();
  }

  @Test
  public void insert_records() {
    ImmutableList<ProductRoomManufacturer> products = create();
    for (ProductRoomManufacturer productRoomManufacturer : products) {
      Completable completable = productRoomManufacturerDao.insert(productRoomManufacturer);
      completable.test().assertComplete();
    }
  }

  @Test
  public void delete_record() {
    ProductRoomManufacturer productRoomManufacturer = builder.build();

    Completable completable = productRoomManufacturerDao.insert(productRoomManufacturer);
    completable.test().assertComplete();

    completable = productRoomManufacturerDao.delete(productRoomManufacturer);
    completable.test().assertComplete();
  }

  @Test
  public void update_record() {
    ProductRoomManufacturer productRoomManufacturer = builder.build();

    Completable completable = productRoomManufacturerDao.insert(productRoomManufacturer);
    completable.test().assertComplete();

    completable =
        productRoomManufacturerDao.update(
            productRoomManufacturer.toBuilder().setName("Other").build());
    completable.test().assertComplete();

    Maybe<ProductRoomManufacturer> maybe =
        productRoomManufacturerDao.findById(productRoomManufacturer.id());
    maybe.test().assertValue(productRoomManufacturer.toBuilder().setName("Other").build());
  }

  @Test
  public void all_records() {
    ImmutableList<ProductRoomManufacturer> products = create();
    for (ProductRoomManufacturer productRoomManufacturer : products) {
      Completable completable = productRoomManufacturerDao.insert(productRoomManufacturer);
      completable.test().assertComplete();
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    Observable<PagedList<ProductRoomManufacturer>> result =
        new RxPagedListBuilder(productRoomManufacturerDao.all(), config).buildObservable();
    PagedList<ProductRoomManufacturer> productRoomManufacturers = result.blockingFirst();
    assertThat(Lists.newArrayList(productRoomManufacturers.iterator()))
        .containsAtLeastElementsIn(products);
  }

  @Test
  public void all_records_withLiveData() throws InterruptedException {
    ImmutableList<ProductRoomManufacturer> products = create();
    for (ProductRoomManufacturer productRoomManufacturer : products) {
      Completable completable = productRoomManufacturerDao.insert(productRoomManufacturer);
      completable.test().assertComplete();
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    LiveData<PagedList<ProductRoomManufacturer>> listLiveData =
        new LivePagedListBuilder<>(productRoomManufacturerDao.all(), config).build();
    PagedList<ProductRoomManufacturer> pagedList = LiveDataTestUtil.blocking(listLiveData);
    assertThat(Lists.newArrayList(pagedList.iterator())).containsAtLeastElementsIn(products);
  }

  @Test
  public void findById_record() {
    ProductRoomManufacturer productRoomManufacturer = builder.build();

    Completable completable = productRoomManufacturerDao.insert(productRoomManufacturer);
    completable.test().assertComplete();

    Maybe<ProductRoomManufacturer> maybe =
        productRoomManufacturerDao.findById(productRoomManufacturer.id());
    maybe.test().assertValue(productRoomManufacturer);
  }

  @Test
  public void findById_referenceId() {
    ProductRoomManufacturer productRoomManufacturer = builder.build();

    Completable completable = productRoomManufacturerDao.insert(productRoomManufacturer);
    completable.test().assertComplete();

    Maybe<ProductRoomManufacturer> maybe =
        productRoomManufacturerDao.findByReferenceId(productRoomManufacturer.referenceId());
    maybe.test().assertValueCount(1);
  }

  @Test
  public void findAllLikeName_records() {
    ImmutableList<ProductRoomManufacturer> products = create();
    for (ProductRoomManufacturer productRoomManufacturer : products) {
      Completable completable = productRoomManufacturerDao.insert(productRoomManufacturer);
      completable.test().assertComplete();
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    Observable<PagedList<ProductRoomManufacturer>> result =
        new RxPagedListBuilder(productRoomManufacturerDao.findAllLikeName("NAME -"), config)
            .buildObservable();
    PagedList<ProductRoomManufacturer> productRoomManufacturers = result.blockingFirst();
    assertThat(Lists.newArrayList(productRoomManufacturers.iterator()))
        .containsAtLeastElementsIn(products);
  }

  @After
  public void teardown() {
    db.close();
  }

  private static final ImmutableList<ProductRoomManufacturer> create() {
    ImmutableList.Builder<ProductRoomManufacturer> builder = ImmutableList.builder();
    for (String product : PRODUCTS) {
      ProductRoomManufacturer.Builder builderProduct =
          ProductRoomManufacturer.builder()
              .setId("NEW - " + product)
              .setReferenceId("REF ID - " + product)
              .setName("NAME - " + product)
              .setActive(true);
      builder.add(builderProduct.build());
    }
    return builder.build();
  }
}
