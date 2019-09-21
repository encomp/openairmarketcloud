package com.toolinc.openairmarket.persistence.local.offline;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Tests for {@link CollectionSyncStateDao}. */
@RunWith(AndroidJUnit4.class)
public class CollectionSyncStateDaoTest {

  private OfflineRoomDatabase mDatabase;

  @Before
  public void setUp() {
    mDatabase =
        Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().getContext(),
                OfflineRoomDatabase.class)
            .allowMainThreadQueries()
            .build();
  }

  @After
  public void tearDown() {
    mDatabase.close();
  }

  @Test
  public void insertAndDelete() {
    CollectionSyncState model = create("new", SyncStatus.IN_PROGRESS);
    mDatabase.collectionStateDao().insert(model);
    mDatabase.collectionStateDao().delete(model).test().assertValue(1);
  }

  @Test
  public void deleteAll() {
    int[] ids = new int[] {1, 2, 3, 4, 5};
    for (int id : ids) {
      CollectionSyncState model = create("new" + id, SyncStatus.IN_PROGRESS);
      mDatabase.collectionStateDao().insert(model);
    }
    mDatabase.collectionStateDao().deleteAll().test().assertComplete();
  }

  private static final CollectionSyncState create(String id, SyncStatus status) {
    CollectionSyncState collectionSyncState = new CollectionSyncState();
    collectionSyncState.setId(id);
    collectionSyncState.setStatus(status);
    collectionSyncState.setLastUpdate(DateTime.now());
    collectionSyncState.setNumberOfDocs(0);
    return collectionSyncState;
  }
}
