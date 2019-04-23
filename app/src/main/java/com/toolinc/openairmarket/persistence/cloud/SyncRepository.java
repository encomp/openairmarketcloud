package com.toolinc.openairmarket.persistence.cloud;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
}
