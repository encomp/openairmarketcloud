package com.toolinc.openairmarket.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.pos.persistence.model.sale.SaleLine;

import java.math.BigDecimal;
import java.util.Set;

import javax.inject.Inject;

/** Specifies the view model for a single receipt. */
public class ReceiptViewModel extends ViewModel {

  private final MutableLiveData<Set<Product>> products = new MutableLiveData<>();
  private final MutableLiveData<ImmutableList<SaleLine>> lines = new MutableLiveData<>();
  private final MutableLiveData<BigDecimal> amountDue = new MutableLiveData<>();

  @Inject
  public ReceiptViewModel() {
    lines.postValue(ImmutableList.<SaleLine>builder().build());
    products.postValue(ImmutableSet.<Product>builder().build());
    amountDue.postValue(BigDecimal.ZERO);
  }

  public LiveData<ImmutableList<SaleLine>> getLines() {
    return lines;
  }

  public LiveData<Set<Product>> getProducts() {
    return products;
  }

  public LiveData<BigDecimal> getAmountDue() {
    return amountDue;
  }
}
