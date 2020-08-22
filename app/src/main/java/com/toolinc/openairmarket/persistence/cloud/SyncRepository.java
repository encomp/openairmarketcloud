package com.toolinc.openairmarket.persistence.cloud;

import com.google.android.gms.tasks.Task;
import com.google.common.collect.FluentIterable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductCategory;
import java.util.List;
import javax.inject.Inject;

/** Product brand repository hides firebase details of the api. */
public final class SyncRepository {

  private final String collectionName;
  private final FirebaseFirestore firestore;

  @Inject
  public SyncRepository(String collectionName, FirebaseFirestore firebaseFirestore) {
    this.collectionName = collectionName;
    this.firestore = firebaseFirestore;
  }

  public Task<QuerySnapshot> retrieveAll() {
    return firestore.collection(collectionName).get();
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

  public static final List<ProductCategory> toProductCategories(
      List<DocumentSnapshot> documentSnapshots) {
    return FluentIterable.from(documentSnapshots)
        .filter(documentSnapshot -> documentSnapshot.exists())
        .transform(documentSnapshot -> {
          ProductCategory product = documentSnapshot.toObject(ProductCategory.class);
          product.setId(documentSnapshot.getId());
          return product;
        })
        .toList();
  }
}
