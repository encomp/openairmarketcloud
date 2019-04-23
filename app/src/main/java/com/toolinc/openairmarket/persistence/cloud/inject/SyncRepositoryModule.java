package com.toolinc.openairmarket.persistence.cloud.inject;

import com.google.firebase.firestore.FirebaseFirestore;
import com.toolinc.openairmarket.persistence.cloud.CollectionsNames;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import com.toolinc.openairmarket.persistence.local.offline.OfflineDatabaseModule;

import dagger.Module;
import dagger.Provides;

/** Provides the injection for the sync repositories for firestore database. */
@Module(includes = {OfflineDatabaseModule.class})
public class SyncRepositoryModule {

  @Repository.Product.Brands
  @Provides
  SyncRepository providesProductBrandsRepo(FirebaseFirestore firebaseFirestore) {
    return new SyncRepository(CollectionsNames.PRODUCT_BRANDS, firebaseFirestore);
  }

  @Repository.Product.Categories
  @Provides
  SyncRepository providesProductCategoriesRepo(FirebaseFirestore firebaseFirestore) {
    return new SyncRepository(CollectionsNames.PRODUCT_CATEGORIES, firebaseFirestore);
  }

  @Repository.Product.Manufacturers
  @Provides
  SyncRepository providesProductManufacturersRepo(FirebaseFirestore firebaseFirestore) {
    return new SyncRepository(CollectionsNames.PRODUCT_MANUFACTURERS, firebaseFirestore);
  }

  @Repository.Product.Products
  @Provides
  SyncRepository providesProductProductsRepo(FirebaseFirestore firebaseFirestore) {
    return new SyncRepository(CollectionsNames.PRODUCTS, firebaseFirestore);
  }

  @Repository.Product.Units
  @Provides
  SyncRepository providesProductUnitsRepo(FirebaseFirestore firebaseFirestore) {
    return new SyncRepository(CollectionsNames.PRODUCT_UNITS, firebaseFirestore);
  }
}
