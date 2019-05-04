package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.adapter.SaleLineListAdapter;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/** Receipt fragment to handle all the items of a sale. */
public final class ReceiptFragment extends DaggerFragment {

  @Inject ViewModelProvider.Factory viewModelFactory;

  @BindView(R.id.product_list_recycler_view)
  RecyclerView recyclerView;

  private ReceiptViewModel receiptViewModel;
  private SaleLineListAdapter saleLineListAdapter;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    final View view = layoutInflater.inflate(R.layout.fragment_receipt, viewGroup, false);
    ButterKnife.bind(this, view);
    receiptViewModel = create();

    saleLineListAdapter = new SaleLineListAdapter(receiptViewModel);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(saleLineListAdapter);
    return view;
  }

  public ReceiptViewModel getReceiptViewModel() {
    return receiptViewModel;
  }

  private ReceiptViewModel create() {
    return ViewModelProviders.of(this, viewModelFactory).get(ReceiptViewModel.class);
  }
}
