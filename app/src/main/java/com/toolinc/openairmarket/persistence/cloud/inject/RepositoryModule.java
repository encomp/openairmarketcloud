package com.toolinc.openairmarket.persistence.cloud.inject;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.toolinc.openairmarket.common.inject.Global;
import com.toolinc.openairmarket.persistence.cloud.ProductsRepository;
import com.toolinc.openairmarket.persistence.cloud.SaleRepository;

import java.util.concurrent.Executor;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

/** Provides the injection for the repositories for firestore database. */
@InstallIn(ApplicationComponent.class)
@Module
public class RepositoryModule {

  @Provides
  ProductsRepository providesProductsRepository(
      @Global.NetworkIO Executor executor, FirebaseFirestore firebaseFirestore) {
    return new ProductsRepository(executor, firebaseFirestore);
  }

  @Provides
  SaleRepository providesSaleRepository(
      @Global.NetworkIO Executor executor,
      FirebaseUser firebaseUser,
      FirebaseFirestore firebaseFirestore) {
    return new SaleRepository(executor, firebaseUser, firebaseFirestore);
  }
}
