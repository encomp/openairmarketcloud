package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.adapter.SaleLineListAdapter;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel.ProductLine;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/** Receipt fragment to handle all the items of a sale. */
public final class ReceiptFragment extends DaggerFragment {

  private final SaleLineListAdapter saleLineListAdapter = new SaleLineListAdapter();
  private final ReceiptViewModel receiptViewModel;
  @Inject ViewModelProvider.Factory viewModelFactory;

  @BindView(R.id.product_list_recycler_view)
  RecyclerView recyclerView;

  @BindView(R.id.total_tv)
  TextView textView;

  public ReceiptFragment(ReceiptViewModel receiptViewModel) {
    this.receiptViewModel = receiptViewModel;
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    final View view = layoutInflater.inflate(R.layout.fragment_receipt, viewGroup, false);
    ButterKnife.bind(this, view);
    receiptViewModel.getLines().observe(this, this::newProductLines);
    receiptViewModel.getAmountDue().observe(this, this::newTotalAmount);

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(saleLineListAdapter);
    return view;
  }

  private void newProductLines(ImmutableList<ProductLine> productLines) {
    saleLineListAdapter.setProductLines(productLines);
    recyclerView.setAdapter(saleLineListAdapter);
  }

  private void newTotalAmount(BigDecimal bigDecimal) {
    DecimalFormat moneyFormat = new DecimalFormat();
    moneyFormat.setMinimumFractionDigits(0);
    moneyFormat.setMaximumFractionDigits(4);
    moneyFormat.setMaximumIntegerDigits(10);
    moneyFormat.setMinimumIntegerDigits(0);
    textView.setText(moneyFormat.format(bigDecimal));
  }

  public ReceiptViewModel getReceiptViewModel() {
    return receiptViewModel;
  }
}
