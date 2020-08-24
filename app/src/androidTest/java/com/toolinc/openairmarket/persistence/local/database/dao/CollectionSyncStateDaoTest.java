package com.toolinc.openairmarket.persistence.local.database.dao;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.toolinc.openairmarket.persistence.local.database.LocalRoomDatabase;
import com.toolinc.openairmarket.persistence.local.database.model.CollectionSyncState;
import com.toolinc.openairmarket.persistence.local.database.model.SyncStatus;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link CollectionSyncStateDao}.
 */
@RunWith(AndroidJUnit4.class)
public class CollectionSyncStateDaoTest {

  private LocalRoomDatabase mDatabase;

  private static final CollectionSyncState create(String id, SyncStatus status) {
    CollectionSyncState collectionSyncState = new CollectionSyncState();
    collectionSyncState.setId(id);
    collectionSyncState.setStatus(status);
    collectionSyncState.setLastUpdate(DateTime.now());
    collectionSyncState.setNumberOfDocs(0);
    return collectionSyncState;
  }

  @Before
  public void setUp() {
    mDatabase =
        Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocalRoomDatabase.class)
            .allowMainThreadQueries()
            .build();
  }

  @After
  public void tearDown() {
    mDatabase.close();
  }

  @Test
  public void delete() {
    CollectionSyncState model = create("new", SyncStatus.IN_PROGRESS);
    mDatabase.collectionStateDao().delete(model);
  }

  @Test
  public void insertAndDelete() {
    CollectionSyncState model = create("new", SyncStatus.IN_PROGRESS);
    mDatabase.collectionStateDao().insert(model);
    mDatabase.collectionStateDao().delete(model);
  }
}
