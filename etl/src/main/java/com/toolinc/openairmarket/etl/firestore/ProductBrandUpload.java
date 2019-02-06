package com.toolinc.openairmarket.etl.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.common.flogger.FluentLogger;
import com.google.common.io.Files;
import com.toolinc.openairmarket.etl.file.CsvReader;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductBrand;

import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

/** Upload the product brands data from a csv file to firestore database. */
public final class ProductBrandUpload {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String COLLECTION = "productBrands";
  private static final String DATA_FILE = "build/pipeline/data/output/marca.csv";

  public static final void main(String[] args) throws Exception {
    final Firestore firestore = FirestoreHelper.getFirestore();
    File file = new File(FirestoreHelper.PATH + DATA_FILE);
    try (CsvReader csvReader =
        CsvReader.builder().setReader(Files.newReader(file, Charset.forName("UTF-8"))).build()) {
      String[] line = csvReader.readNext();
      line = csvReader.readNext();
      while (line != null) {
        ProductBrand productBrand =
            ProductBrand.newBuilder()
                .setReferenceId(line[0])
                .setName(line[1])
                .setProductManufacturer(line[2])
                .build();
        productBrand.setId(line[0]);
        WriteResult writeResult = store(firestore, productBrand);
        logger.atInfo().log(
            String.format(
                "Stored ProductBrand: [%s], at: [%s]",
                productBrand.id(), writeResult.getUpdateTime()));
        line = csvReader.readNext();
      }
    }
  }

  private static final WriteResult store(Firestore db, ProductBrand productBrand)
      throws InterruptedException, ExecutionException {
    DocumentReference docRef = db.collection(COLLECTION).document(productBrand.id());
    ApiFuture<WriteResult> apiFuture = docRef.create(productBrand);
    return apiFuture.get();
  }
}
