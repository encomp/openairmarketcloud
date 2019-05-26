package com.toolinc.openairmarket.persistence.cloud;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.FluentIterable;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.toolinc.openairmarket.common.inject.Global;
import com.toolinc.openairmarket.pos.persistence.model.PaymentMethod;
import com.toolinc.openairmarket.pos.persistence.model.sale.Sale;
import com.toolinc.openairmarket.pos.persistence.model.sale.SaleType;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.concurrent.Executor;

import javax.inject.Inject;

/** Sale repository hides firebase details of the api. */
public final class SaleRepository {
  private final Executor executor;
  private final FirebaseFirestore firebaseFirestore;
  private final FirebaseUser firebaseUser;

  @Inject
  public SaleRepository(
      @Global.NetworkIO Executor executor,
      FirebaseUser firebaseUser,
      FirebaseFirestore firebaseFirestore) {
    this.executor = executor;
    this.firebaseUser = firebaseUser;
    this.firebaseFirestore = firebaseFirestore;
  }

  /** Create a new {@link Sale} on Firestore database. */
  public void create(
      ReceiptViewModel receiptViewModel,
      BigDecimal paymentAmount,
      OnSuccessListener<Sale> successListener,
      OnFailureListener onFailureListener) {
    WriteBatch writeBatch = firebaseFirestore.batch();
    final Sale sale =
        Sale.newBuilder()
            .setSaleType(SaleType.NEW)
            .setPaymentMethod(PaymentMethod.CASH)
            .setDate(DateTime.now())
            .total(receiptViewModel.getAmountDue().getValue())
            .setAmount(paymentAmount)
            .setSystemUser(firebaseUser.getUid())
            .setSaleLines(
                FluentIterable.from(receiptViewModel.getLines().getValue())
                    .transform((productLine) -> productLine.saleLine())
                    .toList())
            .build();
    final DocumentReference documentReference =
        firebaseFirestore.collection(CollectionsNames.SALES).document();
    writeBatch.set(documentReference, sale);
    writeBatch
        .commit()
        .addOnSuccessListener(
            executor, listener -> onSuccess(documentReference, sale, successListener))
        .addOnFailureListener(executor, onFailureListener);
  }

  private void onSuccess(
      DocumentReference documentReference, Sale sale, OnSuccessListener<Sale> successListener) {
    sale.setId(documentReference.getId());
    successListener.onSuccess(sale);
  }
}
