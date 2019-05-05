package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.base.Preconditions;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.persistence.cloud.ProductsRepository;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.viewmodel.ReceiptsViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

public class ReceiptsFragment extends DaggerFragment {

  private final ReceiptsViewModel receiptsViewModel;
  private final FloatingActionButton floatingActionButton;
  @Inject ProductsRepository productsRepository;

  @BindView(R.id.tab_layout)
  TabLayout tabLayout;

  @BindView(R.id.view_pager)
  ViewPager viewPager;

  @BindView(R.id.text_input_edit_text)
  TextInputEditText textInputEditText;

  private ReceiptFragmentStatePagerAdapter receiptFragmentStatePagerAdapter;

  public ReceiptsFragment(
      FloatingActionButton floatingActionButton, ReceiptsViewModel receiptsViewModel) {
    this.floatingActionButton = Preconditions.checkNotNull(floatingActionButton, "FAB is missing.");
    this.receiptsViewModel =
        Preconditions.checkNotNull(receiptsViewModel, "ReceiptsViewModel is missing.");
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view =
        layoutInflater.inflate(R.layout.fragment_receipts, viewGroup, false /* attachToRoot */);
    ButterKnife.bind(this, view);
    receiptFragmentStatePagerAdapter =
        new ReceiptFragmentStatePagerAdapter(
            getFragmentManager(), receiptsViewModel, getContext(), viewPager, tabLayout);
    textInputEditText.setOnKeyListener(this::onKey);
    floatingActionButton.setOnClickListener(this::onClick);
    return view;
  }

  private boolean onKey(View view, int keyCode, KeyEvent event) {
    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
      String productId = textInputEditText.getText().toString();
      textInputEditText.getText().clear();
      productsRepository.findProductById(productId, this::onSuccess, this::onFailure);
      return true;
    }
    return false;
  }

  private void onClick(View view) {
    String productId = textInputEditText.getText().toString();
    textInputEditText.getText().clear();
    productsRepository.findProductById(productId, this::onSuccess, this::onFailure);
  }

  void onSuccess(Product product) {
    receiptFragmentStatePagerAdapter.addProduct(product);
  }

  private void onFailure(@NonNull Exception e) {}
}
