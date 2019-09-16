package com.toolinc.openairmarket.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.base.Strings;
import com.toolinc.openairmarket.OpenAirMarketApplication;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.common.NotificationUtil;
import com.toolinc.openairmarket.common.NotificationUtil.ChannelProperties;
import com.toolinc.openairmarket.common.NotificationUtil.NotificationProperties;
import com.toolinc.openairmarket.model.QuickAccess;
import com.toolinc.openairmarket.persistence.cloud.ProductsRepository;
import com.toolinc.openairmarket.persistence.cloud.SaleRepository;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.ui.MainActivity;
import com.toolinc.openairmarket.ui.adapter.QuickAccessListAdapter;
import com.toolinc.openairmarket.ui.fragment.inject.Annotations.Sale;
import com.toolinc.openairmarket.ui.fragment.inject.Annotations.Sale.Failed;
import com.toolinc.openairmarket.ui.fragment.inject.Annotations.Sale.Succeed;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel;
import com.toolinc.openairmarket.viewmodel.ReceiptsViewModel;

import java.math.BigDecimal;

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
  @Inject SaleRepository saleRepository;
  @Inject @Sale ChannelProperties channelProperties;
  @Inject @Succeed NotificationProperties saleSucceedNotification;
  @Inject @Failed NotificationProperties saleFailedNotification;

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
    NotificationUtil.createChannel(getContext(), channelProperties);
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
    payBtn.setOnClickListener(this::displayCompleteSaleDialog);
    cancelBtn.setOnClickListener(this::displayCancelSaleDialog);
    return view;
  }

  private void setUpBottomSheet() {
    bottomSheetDialog = new BottomSheetDialog(getActivity());
    bottomSheetDialog.setContentView(R.layout.bottomsheet_quick_buttons);
    RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.quick_access_btn);
    QuickAccessListAdapter adapter =
        new QuickAccessListAdapter(
            QuickAccess.quickAccessesButtons(getContext()), this::onClickQuickAccess);
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
    if (!Strings.isNullOrEmpty(productId)) {
      productsRepository.findProductById(productId, this::onSuccess, this::onFailure);
    }
  }

  private boolean onKey(View view, int keyCode, KeyEvent event) {
    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
      String productId = textInputEditText.getText().toString();
      textInputEditText.getText().clear();
      if (!Strings.isNullOrEmpty(productId)) {
        productsRepository.findProductById(productId, this::onSuccess, this::onFailure);
        Timber.tag(TAG).d("Searching for Product: [%s].", productId);
      }
      return true;
    }
    return false;
  }

  private void onClickQuickAccess(String productId) {
    productsRepository.findProductById(productId, this::onSuccess, this::onFailure);
  }

  private void displayCompleteSaleDialog(View view) {
    if (receiptFragmentStatePagerAdapter.getReceiptViewModel().getLines().getValue().size() > 0) {
      final AlertDialog alertDialog =
          new AlertDialog.Builder(getContext()).setView(R.layout.dialog_complete_sale).create();
      // Define the dialog listeners.
      alertDialog.setOnShowListener(
          (dialog) -> {
            final TextInputEditText tietPayment =
                alertDialog.findViewById(R.id.tiet_payment_amount);
            final TextView tvTotal = alertDialog.findViewById(R.id.tv_total_amount);
            final TextView tvChange = alertDialog.findViewById(R.id.tv_change_amount);
            final MaterialButton mbPositive = alertDialog.findViewById(R.id.btn_positive);
            final ReceiptViewModel receiptViewModel =
                receiptFragmentStatePagerAdapter.getReceiptViewModel();
            final BigDecimal total = receiptViewModel.getAmountDue().getValue();
            tvTotal.setText(OpenAirMarketApplication.toString(total));
            tvChange.setText(OpenAirMarketApplication.toString(BigDecimal.ZERO));
            tietPayment.setOnKeyListener(
                (viewDialog, keyCode, event) -> {
                  Timber.tag(TAG).d("Payment Amount: [%s].", tietPayment.getText().toString());
                  try {
                    BigDecimal paymentAmount = new BigDecimal(tietPayment.getText().toString());
                    BigDecimal change = paymentAmount.subtract(total);
                    tvChange.setText(OpenAirMarketApplication.toString(change));
                    if (change.compareTo(BigDecimal.ZERO) >= 0) {
                      mbPositive.setVisibility(View.VISIBLE);
                    } else {
                      mbPositive.setVisibility(View.GONE);
                    }
                  } catch (NumberFormatException exc) {
                    tvChange.setText(OpenAirMarketApplication.toString(BigDecimal.ZERO));
                    mbPositive.setVisibility(View.GONE);
                  }
                  return false;
                });
            mbPositive.setVisibility(View.GONE);
            mbPositive.setOnClickListener(
                (viewBtn) -> {
                  saleRepository.create(
                      receiptViewModel,
                      new BigDecimal(tietPayment.getText().toString()),
                      (sale) -> {
                        // TODO (edgarrico): In case of a temporary offline we need to keep track of
                        // this situation better.
                        Timber.tag(TAG).d("Sale: [%s].", sale.id());
                        NotificationUtil.notify(getContext(), saleSucceedNotification);
                      },
                      (exc) -> {
                        // TODO (edgarrico): In case of a failure consider storing such information
                        // in a local database to further audit in case the connection is lost and a
                        // sale was actually performed.
                        Timber.tag(TAG).d("Unable to perform the Sale: [%s].", exc.getMessage());
                        NotificationUtil.notify(getContext(), saleFailedNotification);
                      });
                  dialog.cancel();
                  receiptFragmentStatePagerAdapter.removeAllProducts();
                });
            alertDialog
                .findViewById(R.id.btn_negative)
                .setOnClickListener((viewBtn) -> dialog.cancel());
          });
      alertDialog.show();
    }
  }

  private void displayCancelSaleDialog(View view) {
    if (receiptFragmentStatePagerAdapter.getReceiptViewModel().getLines().getValue().size() > 0) {
      final AlertDialog alertDialog =
          new AlertDialog.Builder(getContext()).setView(R.layout.dialog_cancel_sale).create();
      // Define the button listeners.
      alertDialog.setOnShowListener(
          (dialog) -> {
            alertDialog
                .findViewById(R.id.btn_positive)
                .setOnClickListener(
                    (viewBtn) -> {
                      receiptFragmentStatePagerAdapter.removeAllProducts();
                      dialog.cancel();
                    });
            alertDialog
                .findViewById(R.id.btn_negative)
                .setOnClickListener((viewBtn) -> dialog.cancel());
          });
      alertDialog.show();
    }
  }

  void onSuccess(Product product) {
    receiptFragmentStatePagerAdapter.addProduct(product);
  }

  private void onFailure(@NonNull Exception e) {
    Timber.tag(TAG).e(e);
  }
}
