package com.toolinc.openairmarket.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductSalePrice;
import com.toolinc.openairmarket.pos.persistence.model.sale.SaleLine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import timber.log.Timber;

/** Specifies the view model for a single receipt. */
public final class ReceiptViewModel {

  private static final String TAG = ReceiptViewModel.class.getSimpleName();
  private final Map<Product, SaleLine> productToSale = Maps.newHashMap();
  private final MutableLiveData<ImmutableList<ProductLine>> lines = new MutableLiveData<>();
  private final MutableLiveData<BigDecimal> amountDue = new MutableLiveData<>();

  public ReceiptViewModel() {
    removeAllProducts();
  }

  public void add(Product product, BigDecimal quantity) {
    if (!productToSale.containsKey(product)) {
      Timber.tag(TAG).d("Adding new product: [%s]-[%s].", product.id(), product.getName());
      ProductSalePrice productSalePrice = product.getProductSalePrice();
      SaleLine saleLine = new SaleLine();
      saleLine.setLineOrder(lines.getValue().size() + 1);
      saleLine.setProduct(product.id());
      saleLine.setName(product.getName());
      saleLine.quantity(quantity);
      saleLine.price(productSalePrice.price());
      saleLine.total(BigDecimal.ONE.multiply(productSalePrice.price()));
      ProductLine productLine = ProductLine.create(product, saleLine);
      ImmutableList<ProductLine> newLines =
          ImmutableList.<ProductLine>builder().addAll(lines.getValue()).add(productLine).build();
      BigDecimal newTotal = amountDue.getValue().add(saleLine.total());
      productToSale.put(product, saleLine);
      lines.postValue(newLines);
      amountDue.postValue(newTotal);
      Timber.tag(TAG).d("Finished adding new product: [%s]-[%s].", product.id(), product.getName());
    } else {
      Timber.tag(TAG).d("Existing product [%s]-[%s].", product.id(), product.getName());
      ProductSalePrice productSalePrice = product.getProductSalePrice();
      SaleLine saleLine = productToSale.get(product);
      saleLine.quantity(saleLine.quantity().add(quantity));
      saleLine.total(saleLine.total().add(productSalePrice.price()));
      BigDecimal newTotal = amountDue.getValue().add(productSalePrice.price());
      lines.postValue(lines.getValue());
      amountDue.postValue(newTotal);
      Timber.tag(TAG).d("Complete existing product [%s]-[%s].", product.id(), product.getName());
    }
  }

  public void removeAllProducts() {
    Timber.tag(TAG).d("Remove all products.");
    productToSale.clear();
    lines.postValue(ImmutableList.of());
    amountDue.postValue(BigDecimal.ZERO);
  }

  public LiveData<ImmutableList<ProductLine>> getLines() {
    return lines;
  }

  public LiveData<BigDecimal> getAmountDue() {
    return amountDue;
  }

  @AutoValue
  public abstract static class ProductLine implements Serializable {

    public static final ProductLine create(Product product, SaleLine saleLine) {
      return new AutoValue_ReceiptViewModel_ProductLine(product, saleLine);
    }

    public abstract Product product();

    public abstract SaleLine saleLine();

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof ProductLine) {
        ProductLine that = (ProductLine) o;
        return product().equals(that.product());
      }
      return false;
    }

    @Override
    public int hashCode() {
      int h$ = 1;
      h$ *= 1000003;
      h$ ^= product().hashCode();
      h$ *= 1000003;
      return h$;
    }
  }
}
