package com.toolinc.openairmarket.persistence.cloud;

import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.toolinc.openairmarket.common.inject.Global;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;

import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

/** Products repository hides firebase details of the api. */
public final class ProductsRepository {

  private final Executor executor;
  private final FirebaseFirestore firebaseFirestore;

  @Inject
  public ProductsRepository(
      @Global.NetworkIO Executor executor, FirebaseFirestore firebaseFirestore) {
    this.executor = executor;
    this.firebaseFirestore = firebaseFirestore;
  }

  public Task<DocumentSnapshot> findProductById(
      String productId,
      OnSuccessListener<Product> successListener,
      OnFailureListener onFailureListener) {
    return firebaseFirestore
        .collection(CollectionsNames.PRODUCTS)
        .document(productId)
        .get()
        .addOnSuccessListener(
            executor, documentSnapshot -> onSuccess(documentSnapshot, successListener))
        .addOnFailureListener(
            executor,
            exc -> {
              onFailure(exc, onFailureListener);
            });
  }

  public static final List<Product> toProducts(List<DocumentSnapshot> documentSnapshots) {
    return FluentIterable.from(documentSnapshots)
        .filter(documentSnapshot -> documentSnapshot.exists())
        .transform(documentSnapshot -> {
          Product product = documentSnapshot.toObject(Product.class);
          product.setId(documentSnapshot.getId());
          return product;
        })
        .toList();
  }

  private void onSuccess(
      DocumentSnapshot documentSnapshot, OnSuccessListener<Product> successListener) {
    if (documentSnapshot.exists()) {
      Product product = documentSnapshot.toObject(Product.class);
      product.setId(documentSnapshot.getId());
      successListener.onSuccess(product);
    }
  }

  private void onFailure(Exception exc, OnFailureListener onFailureListener) {
    onFailureListener.onFailure(exc);
  }
}
