package com.toolinc.openairmarket.persistence.cloud.inject;

import com.toolinc.openairmarket.persistence.ProductBrandOfflineRefresher;
import com.toolinc.openairmarket.persistence.cloud.ProductBrandRepository;
import com.toolinc.openairmarket.persistence.local.offline.OfflineDatabaseModule;

import javax.inject.Singleton;

import dagger.Module;

/** Provides the repositories for firestore database. */
@Module(includes = {OfflineDatabaseModule.class})
public interface PersistenceModule {

  ProductBrandRepository providesProductBrandRepository();

  @Singleton
  ProductBrandOfflineRefresher providesProductBrandOfflineRefresher();
}
