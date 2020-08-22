package com.toolinc.openairmarket.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.collect.ImmutableList;

import com.toolinc.openairmarket.ui.fragment.ReceiptFragmentStatePagerAdapter;
import javax.inject.Inject;

/** Specifies a {@link ImmutableList} of {@link ReceiptViewModel}. */
public class ReceiptsViewModel extends ViewModel {

  private ImmutableList<ReceiptViewModel> receiptViewModels =
      ImmutableList.of(new ReceiptViewModel(), new ReceiptViewModel(), new ReceiptViewModel());

  private final MutableLiveData<ReceiptFragmentStatePagerAdapter> receiptFragmentStatePagerAdapter = new MutableLiveData<>();;

  @Inject
  public ReceiptsViewModel() {}

  public ImmutableList<ReceiptViewModel> getReceiptViewModels() {
    return receiptViewModels;
  }

  public MutableLiveData<ReceiptFragmentStatePagerAdapter> getReceiptFragmentStatePagerAdapter() {
    return receiptFragmentStatePagerAdapter;
  }
}
