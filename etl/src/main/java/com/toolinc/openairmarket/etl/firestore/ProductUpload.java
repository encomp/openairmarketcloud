package com.toolinc.openairmarket.etl.firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.common.flogger.FluentLogger;
import com.google.common.io.Files;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.toolinc.openairmarket.etl.file.CsvReader;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductPurchasePrice;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductSalePrice;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductType;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

public final class ProductUpload {

  private static final String COLLECTION = "productos";
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String PATH =
      "/Users/edgarrico/AndroidStudioProjects/OpenAirMarketCloud/pos_persistence/";
  private static final String FILE_NAME =
      "openairmarket-150121-firebase-adminsdk-2flfk-1db5fe164d.json";

  public static final void main(String[] args) throws Exception {
    InputStream serviceAccount = new FileInputStream(PATH + FILE_NAME);
    GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
    FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
    FirebaseApp.initializeApp(options);
    final Firestore db = FirestoreClient.getFirestore();
    File file =
        new File(
            "/Users/edgarrico/AndroidStudioProjects/OpenAirMarketCloud/etl/build/pipeline/data/output/producto.csv");
    try (CsvReader csvReader =
        CsvReader.builder().setReader(Files.newReader(file, Charset.forName("UTF-8"))).build()) {
      String[] line = csvReader.readNext();
      line = csvReader.readNext();
      while (line != null) {
        Product product =
            Product.newBuilder()
                .setReferenceId(line[1])
                .setName(line[2])
                .setImage(line[3])
                .setProductType(ProductType.ITEM)
                .setProductCategory(line[5])
                .setProductMeasureUnit(line[6])
                .setProductBrand(line[7])
                .setProductSalePrice(
                    ProductSalePrice.newBuilder().setPrice(line[8]).setProfit(line[9]).build())
                .setProductPurchasePrice(
                    ProductPurchasePrice.newBuilder().setPrice(line[10]).build())
                .build();
        String id = line[0].length() == 0 ? line[1] : line[0];
        product.setId(id);
        WriteResult writeResult = store(db, product);
        logger.atInfo().log(
            String.format(
                "Stored Producto: [%s], at: [%s]", product.id(), writeResult.getUpdateTime()));
        line = csvReader.readNext();
      }
    }
  }

  private static final WriteResult store(Firestore db, Product product)
      throws InterruptedException, ExecutionException {
    DocumentReference docRef = db.collection(COLLECTION).document(product.id());
    ApiFuture<WriteResult> apiFuture = docRef.create(product);
    return apiFuture.get();
  }
}
