package com.toolinc.openairmarket.etl.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.common.flogger.FluentLogger;
import com.google.common.io.Files;
import com.toolinc.openairmarket.etl.file.CsvReader;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductPurchasePrice;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductSalePrice;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductType;

import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

/** Upload the product data from a csv file to firestore database. */
public final class ProductUpload {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private static final String COLLECTION = "products";
  private static final String DATA_FILE = "build/pipeline/data/output/producto.csv";

  public static final void main(String[] args) throws Exception {
    final Firestore firestore = FirestoreHelper.getFirestore();
    File file = new File(FirestoreHelper.PATH + DATA_FILE);
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
        WriteResult writeResult = store(firestore, product);
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
