package com.toolinc.openairmarket.persistence.cloud;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.ImmutableList;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.toolinc.openairmarket.common.inject.Global;
import com.toolinc.openairmarket.pos.persistence.model.ui.QuickAccessProduct;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;

/** Quick Access Products repository hides firebase details of the api. */
public class QuickAccessProductRepository {

  private final Executor executor;
  private final FirebaseFirestore firebaseFirestore;

  @Inject
  public QuickAccessProductRepository(
      @Global.NetworkIO Executor executor, FirebaseFirestore firebaseFirestore) {
    this.executor = executor;
    this.firebaseFirestore = firebaseFirestore;
  }

  public Task<QuerySnapshot> getAll(
      OnSuccessListener<List<QuickAccessProduct>> successListener,
      OnFailureListener onFailureListener) {
    return firebaseFirestore
        .collection(CollectionsNames.QUICK_ACCESS_PRODUCTS)
        .get()
        .addOnSuccessListener(
            executor, querySnapshot -> onSuccess(querySnapshot, successListener))
        .addOnFailureListener(
            executor,
            exc -> {
              onFailure(exc, onFailureListener);
            });
  }

  private void onSuccess(
      QuerySnapshot querySnapshot, OnSuccessListener<List<QuickAccessProduct>> successListener) {
    ImmutableList.Builder<QuickAccessProduct> builder = ImmutableList.builder();
    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
      QuickAccessProduct quickAccessProduct = documentSnapshot.toObject(QuickAccessProduct.class);
      quickAccessProduct.setId(documentSnapshot.getId());
      builder.add(quickAccessProduct);
    }
    successListener.onSuccess(builder.build());
  }

  private void onFailure(Exception exc, OnFailureListener onFailureListener) {
    onFailureListener.onFailure(exc);
  }
}
