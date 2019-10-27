package com.toolinc.openairmarket.persistence.local.pos;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.espresso.web.internal.deps.guava.collect.ImmutableList;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.toolinc.openairmarket.common.reactivex.TrampolineSchedulerRule;
import com.toolinc.openairmarket.persistence.local.pos.dao.ProductRoomMeasureUnitDao;
import com.toolinc.openairmarket.persistence.local.pos.model.ProductRoomMeasureUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Completable;

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
