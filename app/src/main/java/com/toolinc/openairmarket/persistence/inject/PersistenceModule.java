package com.toolinc.openairmarket.persistence.inject;

import com.toolinc.openairmarket.persistence.ProductBrandOfflineRefresher;
import com.toolinc.openairmarket.persistence.ProductBrandRepository;
import com.toolinc.openairmarket.persistence.offline.room.OfflineDatabaseModule;

import javax.inject.Singleton;

import dagger.Module;

/** Provides the repositories for firestore database. */
@Module(includes = {OfflineDatabaseModule.class})
public interface PersistenceModule {

  ProductBrandRepository providesProductBrandRepository();

  @Singleton
  ProductBrandOfflineRefresher providesProductBrandOfflineRefresher();
}
