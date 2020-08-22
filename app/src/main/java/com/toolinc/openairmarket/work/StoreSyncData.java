package com.toolinc.openairmarket.work;

import com.google.firebase.firestore.DocumentSnapshot;
import java.util.List;

/** Stores the data synced on a room database. **/
public interface StoreSyncData {

  void store(List<DocumentSnapshot> documentSnapshots);
}
