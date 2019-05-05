package com.toolinc.openairmarket.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.common.collect.ImmutableList;

import javax.inject.Inject;

/** Specifies a {@link ImmutableList} of {@link ReceiptViewModel}. */
public class ReceiptsViewModel extends ViewModel {

  private ImmutableList<ReceiptViewModel> receiptViewModels =
      ImmutableList.of(new ReceiptViewModel(), new ReceiptViewModel(), new ReceiptViewModel());

  @Inject
  public ReceiptsViewModel() {}

  public ImmutableList<ReceiptViewModel> getReceiptViewModels() {
    return receiptViewModels;
  }
}
