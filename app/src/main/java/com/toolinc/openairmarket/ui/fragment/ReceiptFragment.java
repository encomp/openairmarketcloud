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
import androidx.viewpager.widget.ViewPager;

import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.adapter.SaleLineListAdapter;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel.ProductLine;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import timber.log.Timber;

/** Receipt fragment to handle all the items of a sale. */
public final class ReceiptFragment extends DaggerFragment {

  private static final String TAG = ReceiptFragment.class.getSimpleName();
  private final SaleLineListAdapter saleLineListAdapter = new SaleLineListAdapter();
  @Inject ViewModelProvider.Factory viewModelFactory;

  @BindView(R.id.product_list_recycler_view)
  RecyclerView recyclerView;

  @BindView(R.id.total_tv)
  TextView textView;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    final View view = layoutInflater.inflate(R.layout.fragment_receipt, viewGroup, false);
    ButterKnife.bind(this, view);
    ViewPager viewPager = ((ViewPager) viewGroup);

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(saleLineListAdapter);
    return view;
  }

  public void newProductLines(ImmutableList<ProductLine> productLines) {
    Timber.tag(TAG).d("About to add more products: " + productLines.size());
    saleLineListAdapter.setProductLines(productLines);
    recyclerView.setAdapter(saleLineListAdapter);
  }

  public void newTotalAmount(BigDecimal bigDecimal) {
    Timber.tag(TAG).d("New Total Amount: " + bigDecimal.toPlainString());
    DecimalFormat moneyFormat = new DecimalFormat();
    moneyFormat.setMinimumFractionDigits(0);
    moneyFormat.setMaximumFractionDigits(4);
    moneyFormat.setMaximumIntegerDigits(10);
    moneyFormat.setMinimumIntegerDigits(0);
    textView.setText(moneyFormat.format(bigDecimal));
  }
}
