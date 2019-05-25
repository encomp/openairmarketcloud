package com.toolinc.openairmarket.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.model.QuickAccess;
import com.toolinc.openairmarket.persistence.cloud.ProductsRepository;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.ui.MainActivity;
import com.toolinc.openairmarket.ui.adapter.QuickAccessListAdapter;
import com.toolinc.openairmarket.viewmodel.ReceiptsViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import timber.log.Timber;

/** Receipts fragment to handle product search and append. */
public class ReceiptsFragment extends DaggerFragment {

  private static final String TAG = ReceiptsFragment.class.getSimpleName();

  @Inject ViewModelProvider.Factory viewModelFactory;
  @Inject ProductsRepository productsRepository;

  @BindView(R.id.tab_layout)
  TabLayout tabLayout;

  @BindView(R.id.view_pager)
  ViewPager viewPager;

  @BindView(R.id.text_input_edit_text)
  TextInputEditText textInputEditText;

  @BindView(R.id.cancel_btn)
  Button cancelBtn;

  @BindView(R.id.pay_btn)
  Button payBtn;

  private ReceiptsViewModel receiptsViewModel;
  private BottomAppBar bottomAppBar;
  private BottomSheetDialog bottomSheetDialog;
  private FloatingActionButton floatingActionButton;
  private ReceiptFragmentStatePagerAdapter receiptFragmentStatePagerAdapter;

  @Override
  public void onCreate(@Nullable Bundle bundle) {
    super.onCreate(bundle);
    setHasOptionsMenu(true);
    receiptsViewModel =
        ViewModelProviders.of((MainActivity) getActivity(), viewModelFactory)
            .get(ReceiptsViewModel.class);
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view =
        layoutInflater.inflate(R.layout.fragment_receipts, viewGroup, false /* attachToRoot */);
    ButterKnife.bind(this, view);
    bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
    floatingActionButton = getActivity().findViewById(R.id.fab_add_to_receipt);
    receiptFragmentStatePagerAdapter =
        new ReceiptFragmentStatePagerAdapter(
            getFragmentManager(), receiptsViewModel, getContext(), viewPager, tabLayout);
    setUpBottomSheet();
    bottomAppBar.replaceMenu(R.menu.bottom_app_bar_receipts);
    bottomAppBar.setOnMenuItemClickListener(this::onMenuItemClick);
    floatingActionButton.setOnClickListener(this::onClick);
    textInputEditText.setOnKeyListener(this::onKey);
    cancelBtn.setOnClickListener(this::onClickCancelSale);
    return view;
  }

  private void setUpBottomSheet() {
    bottomSheetDialog = new BottomSheetDialog(getActivity());
    bottomSheetDialog.setContentView(R.layout.bottomsheet_quick_buttons);
    RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.quick_access_btn);
    QuickAccessListAdapter adapter =
        new QuickAccessListAdapter(QuickAccess.quickAccessesButtons(), this::onClickQuickAccess);
    recyclerView.setAdapter(adapter);
    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
    recyclerView.setLayoutManager(gridLayoutManager);
    View bottomSheetInternal = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
    BottomSheetBehavior.from(bottomSheetInternal).setPeekHeight(230);
  }

  private boolean onMenuItemClick(MenuItem menuItem) {
    if (menuItem.getItemId() == R.id.quick_access) {
      bottomSheetDialog.show();
      return true;
    }
    return false;
  }

  private void onClick(View view) {
    String productId = textInputEditText.getText().toString();
    textInputEditText.getText().clear();
    productsRepository.findProductById(productId, this::onSuccess, this::onFailure);
  }

  private boolean onKey(View view, int keyCode, KeyEvent event) {
    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
      String productId = textInputEditText.getText().toString();
      textInputEditText.getText().clear();
      productsRepository.findProductById(productId, this::onSuccess, this::onFailure);
      Timber.tag(TAG).d("Searching for Product: " + productId);
      return true;
    }
    return false;
  }

  private void onClickQuickAccess(String productId) {
    productsRepository.findProductById(productId, this::onSuccess, this::onFailure);
  }

  private void onClickCancelSale(View view) {
    final AlertDialog alertDialog =
        new AlertDialog.Builder(getContext())
            .setTitle(getString(R.string.cancel_sale_dialog_title))
            .setMessage(getString(R.string.cancel_sale_dialog_message))
            .setPositiveButton(getString(R.string.cancel_sale_dialog_positive_btn), this::onClickCancel)
            .setNegativeButton(getString(R.string.cancel_sale_dialog_negative_btn), null)
            .setIcon(R.drawable.ic_remove_shopping)
            .create();

    alertDialog.setOnShowListener(
        (dialog) -> {
          LinearLayout.LayoutParams params =
              new LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
          params.setMargins(10, 0, 0, 0);
          Button positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
          positiveBtn.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            positiveBtn.setTextColor(getContext().getColor(R.color.colorAccent));
          }
          positiveBtn.setLayoutParams(params);

          params =
              new LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
          params.setMargins(0, 0, 10, 0);
          Button negativeBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
          negativeBtn.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            negativeBtn.setTextColor(getContext().getColor(R.color.red));
          }
          negativeBtn.setLayoutParams(params);
        });
    alertDialog.show();
  }

  void onClickCancel(DialogInterface dialog, int which) {
    receiptFragmentStatePagerAdapter.removeAllProducts();
  }

  void onSuccess(Product product) {
    receiptFragmentStatePagerAdapter.addProduct(product);
  }

  private void onFailure(@NonNull Exception e) {
    Timber.tag(TAG).e(e.getMessage(), e);
  }
}
