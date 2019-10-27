package com.toolinc.openairmarket.persistence.local.pos;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.web.internal.deps.guava.collect.ImmutableList;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomCategoryDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomCategory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/** Tests for {@link ProductRoomCategoryDao}. */
@RunWith(AndroidJUnit4.class)
public class ProductRoomCategoryDaoTest {

  private static final String[] PRODUCTS = new String[] {"A", "B", "C", "D", "E"};
  private static final ProductRoomCategory.Builder builder =
      ProductRoomCategory.builder()
          .setId("NEW")
          .setReferenceId("REF ID")
          .setName("Name of Category")
          .setActive(true);

  private PosRoomDatabase db;
  private ProductRoomCategoryDao productRoomCategoryDao;

  @Before
  public void setUp() {
    Context context = ApplicationProvider.getApplicationContext();
    db = Room.inMemoryDatabaseBuilder(context, PosRoomDatabase.class).build();
    productRoomCategoryDao = db.productRoomCategoryDao();
  }

  @Test
  public void insert_record() {
    ProductRoomCategory productCategory = builder.build();

    Completable completable = productRoomCategoryDao.insert(productCategory);
    completable.test().assertComplete();
  }

  @Test
  public void insert_records() {
    ImmutableList<ProductRoomCategory> products = create();
    for (ProductRoomCategory productCategory : products) {
      Completable completable = productRoomCategoryDao.insert(productCategory);
      completable.test().assertComplete();
    }
  }

  @Test
  public void delete_record() {
    ProductRoomCategory productCategory = builder.build();

    Completable completable = productRoomCategoryDao.insert(productCategory);
    completable.test().assertComplete();

    completable = productRoomCategoryDao.delete(productCategory.id());
    completable.test().assertComplete();
  }

  @Test
  public void findById_record() {
    ProductRoomCategory productCategory = builder.build();

    Completable completable = productRoomCategoryDao.insert(productCategory);
    completable.test().assertComplete();

    Maybe<ProductRoomCategory> maybe = productRoomCategoryDao.findById(productCategory.id());
    maybe.test().assertValue(productCategory);
  }

  @After
  public void teardown() {
    db.close();
  }

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
}
