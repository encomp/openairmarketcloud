package com.toolinc.openairmarket.etl.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.common.flogger.FluentLogger;
import com.google.common.io.Files;
import com.toolinc.openairmarket.etl.file.CsvReader;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductMeasureUnit;

import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

/** Upload the product measure units data from a csv file to firestore database. */
public final class ProductMeasureUnitUpload {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String COLLECTION = "productMeasureUnits";
  private static final String DATA_FILE = "build/pipeline/data/output/unidad_medida.csv";

  public static final void main(String[] args) throws Exception {
    final Firestore firestore = FirestoreHelper.getFirestore();
    File file = new File(FirestoreHelper.PATH + DATA_FILE);
    try (CsvReader csvReader =
        CsvReader.builder().setReader(Files.newReader(file, Charset.forName("UTF-8"))).build()) {
      String[] line = csvReader.readNext();
      line = csvReader.readNext();
      while (line != null) {
        ProductMeasureUnit productMeasureUnit =
            ProductMeasureUnit.newBuilder()
                .setReferenceId(line[1])
                .setName(line[2])
                .setCountable(Boolean.valueOf(line[3]))
                .build();
        productMeasureUnit.setId(line[0]);
        WriteResult writeResult = store(firestore, productMeasureUnit);
        logger.atInfo().log(
            String.format(
                "Stored ProductMeasureUnit: [%s], at: [%s]",
                productMeasureUnit.id(), writeResult.getUpdateTime()));
        line = csvReader.readNext();
      }
    }
  }

  private static final WriteResult store(Firestore db, ProductMeasureUnit productMeasureUnit)
      throws InterruptedException, ExecutionException {
    DocumentReference docRef = db.collection(COLLECTION).document(productMeasureUnit.id());
    ApiFuture<WriteResult> apiFuture = docRef.create(productMeasureUnit);
    return apiFuture.get();
  }
}
