package com.toolinc.openairmarket.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductSalePrice;
import com.toolinc.openairmarket.pos.persistence.model.sale.SaleLine;

import java.math.BigDecimal;
import java.util.Set;

/** Specifies the view model for a single receipt. */
public final class ReceiptViewModel {

  private final Set<Product> products = Sets.newHashSet();
  private final MutableLiveData<ImmutableList<ProductLine>> lines = new MutableLiveData<>();
  private final MutableLiveData<BigDecimal> amountDue = new MutableLiveData<>();

  public ReceiptViewModel() {
    lines.postValue(ImmutableList.of());
    amountDue.postValue(BigDecimal.ZERO);
  }

  public void add(Product product) {
    if (!products.contains(product)) {
      ProductSalePrice productSalePrice = product.getProductSalePrice();
      SaleLine saleLine = new SaleLine();
      saleLine.setLineOrder(lines.getValue().size());
      saleLine.setProduct(product.id());
      saleLine.quantity(BigDecimal.ONE);
      saleLine.price(productSalePrice.price());
      saleLine.total(BigDecimal.ONE.multiply(productSalePrice.price()));
      ProductLine productLine = ProductLine.create(product, saleLine);
      ImmutableList<ProductLine> newLines =
          ImmutableList.<ProductLine>builder().addAll(lines.getValue()).add(productLine).build();
      products.add(productLine.product());
      lines.postValue(newLines);
      BigDecimal newTotal = amountDue.getValue().add(saleLine.total());
      amountDue.postValue(newTotal);
    }
  }

  public LiveData<ImmutableList<ProductLine>> getLines() {
    return lines;
  }

  public LiveData<BigDecimal> getAmountDue() {
    return amountDue;
  }

  @AutoValue
  public abstract static class ProductLine {

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
