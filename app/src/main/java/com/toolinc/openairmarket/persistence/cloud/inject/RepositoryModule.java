package com.toolinc.openairmarket.persistence.cloud.inject;

import com.toolinc.openairmarket.persistence.cloud.ProductsRepository;
import com.toolinc.openairmarket.persistence.cloud.SaleRepository;

import dagger.Module;

/** Provides the injection for the repositories for firestore database. */
@Module
public interface RepositoryModule {

  ProductsRepository providesProductsRepository();

  SaleRepository providesSaleRepository();
}
