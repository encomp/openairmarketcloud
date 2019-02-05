package com.toolinc.openairmarket.pos.persistence.model.product;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

@RunWith(JUnit4.class)
public final class ProductTest {

  private static final String PATH =
      "/Users/edgarrico/AndroidStudioProjects/OpenAirMarketCloud/pos_persistence/";
  private static final String FILE_NAME =
      "openairmarket-150121-firebase-adminsdk-2flfk-1db5fe164d.json";
  private Firestore db;

  @Before
  public void setUp() throws IOException {
    InputStream serviceAccount = new FileInputStream(PATH + FILE_NAME);
    GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
    FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
    FirebaseApp.initializeApp(options);
    db = FirestoreClient.getFirestore();
  }

  @Test
  public void shouldDummy() {}

  // @Test
  public void shouldInsert() throws InterruptedException, ExecutionException {
    DocumentReference docRef = db.collection("productos").document("123");
    Product product =
        Product.newBuilder()
            .setReferenceId("referenceId")
            .setName("nombre")
            .setImage("image")
            .setProductType(ProductType.ITEM)
            .setProductCategory("category")
            .setProductBrand("brand")
            .setProductMeasureUnit("measureUnit")
            .build();
    ApiFuture<WriteResult> result = docRef.create(product);
    System.out.println("Update time : " + result.get());
  }

  // @Test
  public void shouldInsertPrice() throws InterruptedException, ExecutionException {
    DocumentReference docRef =
        db.collection("productos").document("123").collection("salePrices").document();
    ProductSalePrice product =
        ProductSalePrice.newBuilder().setPrice(BigDecimal.TEN).setProfit(BigDecimal.ONE).build();
    ApiFuture<WriteResult> result = docRef.create(product);
    System.out.println("Update time : " + result.get());
  }

  // @Test
  public void shouldRead() throws InterruptedException, ExecutionException {
    DocumentReference docRef = db.collection("productos").document("123");
    ApiFuture<DocumentSnapshot> future = docRef.get();
    DocumentSnapshot documentSnapshot = future.get();
    documentSnapshot.exists();
    Product product = documentSnapshot.toObject(Product.class);
    product.setId(documentSnapshot.getId());
    System.out.println("Update time : " + product);
  }
}
