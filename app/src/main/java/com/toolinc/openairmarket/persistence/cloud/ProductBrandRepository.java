package com.toolinc.openairmarket.persistence.cloud;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.toolinc.openairmarket.common.inject.Global;

import java.util.concurrent.Executor;

import javax.inject.Inject;

/** Product brand repository hides firebase details of the api. */
public final class ProductBrandRepository {

  private static final String COLLECTION = "productBrands";
  private final Executor executor;
  private final FirebaseFirestore firestore;

  @Inject
  public ProductBrandRepository(
      @Global.NetworkIO Executor executor, FirebaseFirestore firebaseFirestore) {
    this.executor = executor;
    this.firestore = firebaseFirestore;
  }

  public Task<QuerySnapshot> retrieveAll() {
    return firestore.collection(COLLECTION).get();
  }
}
