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
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomMeasureUnitDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomMeasureUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import static com.google.common.truth.Truth.assertThat;

/** Tests for {@link ProductRoomMeasureUnitDao}. */
@RunWith(AndroidJUnit4.class)
public class ProductRoomMeasureUnitDaoTest {

  @Rule
  public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  @Rule public final TrampolineSchedulerRule schedulerRule = new TrampolineSchedulerRule();

  private static final String[] PRODUCTS = new String[] {"A", "B", "C", "D", "E"};
  private static final ProductRoomMeasureUnit.Builder builder =
      ProductRoomMeasureUnit.builder()
          .setId("NEW")
          .setReferenceId("REF ID")
          .setName("Name of Category")
          .setCountable(true)
          .setActive(true);

  private PosRoomDatabase db;
  private ProductRoomMeasureUnitDao productRoomMeasureUnitDao;

  @Before
  public void setUp() {
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    db =
        Room.inMemoryDatabaseBuilder(context, PosRoomDatabase.class)
            .allowMainThreadQueries()
            .build();
    productRoomMeasureUnitDao = db.productRoomMeasureUnitDao();
  }

  @Test
  public void insert_record() {
    ProductRoomMeasureUnit productRoomMeasureUnit = builder.build();

    Completable completable = productRoomMeasureUnitDao.insert(productRoomMeasureUnit);
    completable.test().assertComplete();
  }

  @Test
  public void insert_records() {
    ImmutableList<ProductRoomMeasureUnit> products = create();
    for (ProductRoomMeasureUnit productRoomMeasureUnit : products) {
      Completable completable = productRoomMeasureUnitDao.insert(productRoomMeasureUnit);
      completable.test().assertComplete();
    }
  }

  @Test
  public void delete_record() {
    ProductRoomMeasureUnit productRoomMeasureUnit = builder.build();

    Completable completable = productRoomMeasureUnitDao.insert(productRoomMeasureUnit);
    completable.test().assertComplete();

    completable = productRoomMeasureUnitDao.delete(productRoomMeasureUnit);
    completable.test().assertComplete();
  }

  @Test
  public void update_record() {
    ProductRoomMeasureUnit productRoomMeasureUnit = builder.build();

    Completable completable = productRoomMeasureUnitDao.insert(productRoomMeasureUnit);
    completable.test().assertComplete();

    completable =
        productRoomMeasureUnitDao.update(
            productRoomMeasureUnit.toBuilder().setName("Other").build());
    completable.test().assertComplete();

    Maybe<ProductRoomMeasureUnit> maybe =
        productRoomMeasureUnitDao.findById(productRoomMeasureUnit.id());
    maybe.test().assertValue(productRoomMeasureUnit.toBuilder().setName("Other").build());
  }

  @Test
  public void all_records() {
    ImmutableList<ProductRoomMeasureUnit> products = create();
    for (ProductRoomMeasureUnit productRoomMeasureUnit : products) {
      Completable completable = productRoomMeasureUnitDao.insert(productRoomMeasureUnit);
      completable.test().assertComplete();
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    Observable<PagedList<ProductRoomMeasureUnit>> result =
        new RxPagedListBuilder(productRoomMeasureUnitDao.all(), config).buildObservable();
    PagedList<ProductRoomMeasureUnit> productRoomMeasureUnits = result.blockingFirst();
    assertThat(Lists.newArrayList(productRoomMeasureUnits.iterator()))
        .containsAtLeastElementsIn(products);
  }

  @Test
  public void all_records_withLiveData() throws InterruptedException {
    ImmutableList<ProductRoomMeasureUnit> products = create();
    for (ProductRoomMeasureUnit productRoomMeasureUnit : products) {
      Completable completable = productRoomMeasureUnitDao.insert(productRoomMeasureUnit);
      completable.test().assertComplete();
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    LiveData<PagedList<ProductRoomMeasureUnit>> listLiveData =
        new LivePagedListBuilder<>(productRoomMeasureUnitDao.all(), config).build();
    PagedList<ProductRoomMeasureUnit> pagedList = LiveDataTestUtil.blocking(listLiveData);
    assertThat(Lists.newArrayList(pagedList.iterator())).containsAtLeastElementsIn(products);
  }

  @Test
  public void findById_record() {
    ProductRoomMeasureUnit productRoomMeasureUnit = builder.build();

    Completable completable = productRoomMeasureUnitDao.insert(productRoomMeasureUnit);
    completable.test().assertComplete();

    Maybe<ProductRoomMeasureUnit> maybe =
        productRoomMeasureUnitDao.findById(productRoomMeasureUnit.id());
    maybe.test().assertValue(productRoomMeasureUnit);
  }

  @Test
  public void findById_referenceId() {
    ProductRoomMeasureUnit productRoomMeasureUnit = builder.build();

    Completable completable = productRoomMeasureUnitDao.insert(productRoomMeasureUnit);
    completable.test().assertComplete();

    Maybe<ProductRoomMeasureUnit> maybe =
        productRoomMeasureUnitDao.findByReferenceId(productRoomMeasureUnit.referenceId());
    maybe.test().assertValueCount(1);
  }

  @Test
  public void findAllLikeName_records() {
    ImmutableList<ProductRoomMeasureUnit> products = create();
    for (ProductRoomMeasureUnit productRoomMeasureUnit : products) {
      Completable completable = productRoomMeasureUnitDao.insert(productRoomMeasureUnit);
      completable.test().assertComplete();
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    Observable<PagedList<ProductRoomMeasureUnit>> result =
        new RxPagedListBuilder(productRoomMeasureUnitDao.findAllLikeName("NAME -"), config)
            .buildObservable();
    PagedList<ProductRoomMeasureUnit> productRoomMeasureUnits = result.blockingFirst();
    assertThat(Lists.newArrayList(productRoomMeasureUnits.iterator()))
        .containsAtLeastElementsIn(products);
  }

  @After
  public void teardown() {
    db.close();
  }

  private static final ImmutableList<ProductRoomMeasureUnit> create() {
    ImmutableList.Builder<ProductRoomMeasureUnit> builder = ImmutableList.builder();
    for (String product : PRODUCTS) {
      ProductRoomMeasureUnit.Builder builderProduct =
          ProductRoomMeasureUnit.builder()
              .setId("NEW - " + product)
              .setReferenceId("REF ID - " + product)
              .setName("NAME - " + product)
              .setCountable(true)
              .setActive(true);
      builder.add(builderProduct.build());
    }
    return builder.build();
  }
}
