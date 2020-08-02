package com.toolinc.openairmarket.persistence.cloud.inject;

import com.google.firebase.firestore.FirebaseFirestore;
import com.toolinc.openairmarket.persistence.cloud.CollectionsNames;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Categories;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Products;
import com.toolinc.openairmarket.persistence.local.offline.OfflineDatabaseModule;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

/** Provides the injection for the sync repositories for firestore database. */
@InstallIn(ApplicationComponent.class)
@Module(includes = {OfflineDatabaseModule.class})
public class SyncRepositoryModule {

  @Categories
  @Provides
  SyncRepository providesProductCategoriesRepo(FirebaseFirestore firebaseFirestore) {
    return new SyncRepository(CollectionsNames.PRODUCT_CATEGORIES, firebaseFirestore);
  }

  @Products
  @Provides
  SyncRepository providesProductProductsRepo(FirebaseFirestore firebaseFirestore) {
    return new SyncRepository(CollectionsNames.PRODUCTS, firebaseFirestore);
  }
}
