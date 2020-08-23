package com.toolinc.openairmarket.persistence.cloud.inject;

import com.google.firebase.firestore.FirebaseFirestore;
import com.toolinc.openairmarket.persistence.cloud.CollectionsNames;
import com.toolinc.openairmarket.persistence.cloud.SyncRepository;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Brands;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Categories;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Manufacturers;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.MeasureUnit;
import com.toolinc.openairmarket.persistence.inject.Annotations.Product.Products;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;

/** Provides the injection for the sync repositories for firestore database. */
@InstallIn(FragmentComponent.class)
@Module
public class SyncRepositoryModule {

  @Brands
  @Provides
  SyncRepository providesProductBrandsRepo(FirebaseFirestore firebaseFirestore) {
    return new SyncRepository(CollectionsNames.PRODUCT_BRANDS, firebaseFirestore);
  }

  @Categories
  @Provides
  SyncRepository providesProductCategoriesRepo(FirebaseFirestore firebaseFirestore) {
    return new SyncRepository(CollectionsNames.PRODUCT_CATEGORIES, firebaseFirestore);
  }

  @Manufacturers
  @Provides
  SyncRepository providesProductManufacturersRepo(FirebaseFirestore firebaseFirestore) {
    return new SyncRepository(CollectionsNames.PRODUCT_MANUFACTURERS, firebaseFirestore);
  }

  @MeasureUnit
  @Provides
  SyncRepository providesProductMeasureUnitsRepo(FirebaseFirestore firebaseFirestore) {
    return new SyncRepository(CollectionsNames.PRODUCT_UNITS, firebaseFirestore);
  }

  @Products
  @Provides
  SyncRepository providesProductProductsRepo(FirebaseFirestore firebaseFirestore) {
    return new SyncRepository(CollectionsNames.PRODUCTS, firebaseFirestore);
  }
}
