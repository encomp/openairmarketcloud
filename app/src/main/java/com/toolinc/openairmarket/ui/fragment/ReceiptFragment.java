package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/** Receipt fragment to handle all the items of a sale. */
public final class ReceiptFragment extends DaggerFragment {

  @Inject ViewModelProvider.Factory viewModelFactory;
  private ReceiptViewModel receiptViewModel;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    final View view = layoutInflater.inflate(R.layout.fragment_receipt, viewGroup, false);
    receiptViewModel = create();
    return view;
  }

  private ReceiptViewModel create() {
    return ViewModelProviders.of(this, viewModelFactory).get(ReceiptViewModel.class);
  }
}
