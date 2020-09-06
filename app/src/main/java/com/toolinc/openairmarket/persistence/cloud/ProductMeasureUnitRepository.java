package com.toolinc.openairmarket.persistence.cloud;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Maps;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.toolinc.openairmarket.common.inject.Global;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductMeasureUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.inject.Inject;

/**
 * Measure unit repository hides firebase details of the api.
 */
public class ProductMeasureUnitRepository {

  private final Executor executor;
  private final FirebaseFirestore firebaseFirestore;

  @Inject
  public ProductMeasureUnitRepository(
      @Global.NetworkIO Executor executor, FirebaseFirestore firebaseFirestore) {
    this.executor = executor;
    this.firebaseFirestore = firebaseFirestore;
  }

  public Task<QuerySnapshot> findAllUncountable(
      OnSuccessListener<Map<String, ProductMeasureUnit>> successListener,
      OnFailureListener onFailureListener) {
    return firebaseFirestore
        .collection(CollectionsNames.PRODUCT_UNITS)
        .whereEqualTo("countable", Boolean.FALSE)
        .get()
        .addOnSuccessListener(
            executor, documentSnapshot -> onSuccess(documentSnapshot, successListener))
        .addOnFailureListener(
            executor,
            exc -> {
              onFailure(exc, onFailureListener);
            });
  }

  private void onSuccess(
      QuerySnapshot querySnapshot,
      OnSuccessListener<Map<String, ProductMeasureUnit>> successListener) {
    List<ProductMeasureUnit> productMeasureUnits = SyncRepository
        .toProductMeasureUnits(querySnapshot.getDocuments());
    Map<String, ProductMeasureUnit> map = Maps
        .uniqueIndex(productMeasureUnits, ProductMeasureUnit::id);
    successListener.onSuccess(map);
  }

  private void onFailure(Exception exc, OnFailureListener onFailureListener) {
    onFailureListener.onFailure(exc);
  }
}
