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
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomCategoryDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomCategory;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link ProductRoomCategoryDao}.
 */
@RunWith(AndroidJUnit4.class)
public class ProductRoomCategoryDaoTest {

  private static final String[] PRODUCTS = new String[]{"A", "B", "C", "D", "E"};
  private static final ProductRoomCategory.Builder builder =
      ProductRoomCategory.builder()
          .setId("NEW")
          .setReferenceId("REF ID")
          .setName("Name of Category")
          .setActive(true);
  @Rule
  public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  @Rule
  public final TrampolineSchedulerRule schedulerRule = new TrampolineSchedulerRule();
  private PosRoomDatabase db;
  private ProductRoomCategoryDao productRoomCategoryDao;

  private static final ImmutableList<ProductRoomCategory> create() {
    ImmutableList.Builder<ProductRoomCategory> builder = ImmutableList.builder();
    for (String product : PRODUCTS) {
      ProductRoomCategory.Builder builderProduct =
          ProductRoomCategory.builder()
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
    productRoomCategoryDao = db.productRoomCategoryDao();
  }

  @Test
  public void insert_record() {
    ProductRoomCategory productCategory = builder.build();

    productRoomCategoryDao.insert(productCategory);
  }

  @Test
  public void insert_records() {
    ImmutableList<ProductRoomCategory> products = create();
    for (ProductRoomCategory productCategory : products) {
      productRoomCategoryDao.insert(productCategory);
    }
  }

  @Test
  public void delete_record() {
    ProductRoomCategory productCategory = builder.build();

    productRoomCategoryDao.insert(productCategory);

    productRoomCategoryDao.delete(productCategory);
  }

  @Test
  public void update_record() {
    ProductRoomCategory productCategory = builder.build();

    productRoomCategoryDao.insert(productCategory);

    productRoomCategoryDao.update(productCategory.toBuilder().setName("Other").build());

    Maybe<ProductRoomCategory> maybe = productRoomCategoryDao.findById(productCategory.id());
    maybe.test().assertValue(productCategory.toBuilder().setName("Other").build());
  }

  @Test
  public void all_records() {
    ImmutableList<ProductRoomCategory> products = create();
    for (ProductRoomCategory productCategory : products) {
      productRoomCategoryDao.insert(productCategory);
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    Observable<PagedList<ProductRoomCategory>> result =
        new RxPagedListBuilder(productRoomCategoryDao.all(), config).buildObservable();
    PagedList<ProductRoomCategory> productRoomCategories = result.blockingFirst();
    assertThat(Lists.newArrayList(productRoomCategories.iterator()))
        .containsAtLeastElementsIn(products);
  }

  @Test
  public void all_records_withLiveData() throws InterruptedException {
    ImmutableList<ProductRoomCategory> products = create();
    for (ProductRoomCategory productCategory : products) {
      productRoomCategoryDao.insert(productCategory);
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    LiveData<PagedList<ProductRoomCategory>> listLiveData =
        new LivePagedListBuilder<>(productRoomCategoryDao.all(), config).build();
    PagedList<ProductRoomCategory> pagedList = LiveDataTestUtil.blocking(listLiveData);
    assertThat(Lists.newArrayList(pagedList.iterator())).containsAtLeastElementsIn(products);
  }

  @Test
  public void findById_record() {
    ProductRoomCategory productCategory = builder.build();

    productRoomCategoryDao.insert(productCategory);

    Maybe<ProductRoomCategory> maybe = productRoomCategoryDao.findById(productCategory.id());
    maybe.test().assertValue(productCategory);
  }

  @Test
  public void findById_referenceId() {
    ProductRoomCategory productCategory = builder.build();

    productRoomCategoryDao.insert(productCategory);

    Maybe<ProductRoomCategory> maybe =
        productRoomCategoryDao.findByReferenceId(productCategory.referenceId());
    maybe.test().assertValueCount(1);
  }

  @Test
  public void findAllLikeName_records() {
    ImmutableList<ProductRoomCategory> products = create();
    for (ProductRoomCategory productCategory : products) {
      productRoomCategoryDao.insert(productCategory);
    }

    PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
    Observable<PagedList<ProductRoomCategory>> result =
        new RxPagedListBuilder(productRoomCategoryDao.findAllLikeName("NAME -"), config)
            .buildObservable();
    PagedList<ProductRoomCategory> productRoomCategories = result.blockingFirst();
    assertThat(Lists.newArrayList(productRoomCategories.iterator()))
        .containsAtLeastElementsIn(products);
  }

  @After
  public void teardown() {
    db.close();
  }
}
