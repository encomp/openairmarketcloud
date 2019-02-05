package com.toolinc.openairmarket.etl.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.common.flogger.FluentLogger;
import com.google.common.io.Files;
import com.toolinc.openairmarket.etl.file.CsvReader;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductCategory;

import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

/** Upload the product categories data from a csv file to firestore database. */
public final class ProductCategoryUpload {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String COLLECTION = "productCategories";
  private static final String DATA_FILE = "build/pipeline/data/output/categoria.csv";

  public static final void main(String[] args) throws Exception {
    final Firestore firestore = FirestoreHelper.getFirestore();
    File file = new File(FirestoreHelper.PATH + DATA_FILE);
    try (CsvReader csvReader =
        CsvReader.builder().setReader(Files.newReader(file, Charset.forName("UTF-8"))).build()) {
      String[] line = csvReader.readNext();
      line = csvReader.readNext();
      while (line != null) {
        ProductCategory productCategory =
            ProductCategory.newBuilder().setReferenceId(line[0]).setName(line[1]).build();
        productCategory.setId(line[0]);
        WriteResult writeResult = store(firestore, productCategory);
        logger.atInfo().log(
            String.format(
                "Stored ProductCategory: [%s], at: [%s]",
                productCategory.id(), writeResult.getUpdateTime()));
        line = csvReader.readNext();
      }
    }
  }

  private static final WriteResult store(Firestore db, ProductCategory productCategory)
      throws InterruptedException, ExecutionException {
    DocumentReference docRef = db.collection(COLLECTION).document(productCategory.id());
    ApiFuture<WriteResult> apiFuture = docRef.create(productCategory);
    return apiFuture.get();
  }
}
