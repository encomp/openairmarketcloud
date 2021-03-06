package com.toolinc.openairmarket.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.toolinc.openairmarket.OpenAirMarketApplication;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.common.NotificationUtil;
import com.toolinc.openairmarket.common.NotificationUtil.ChannelProperties;
import com.toolinc.openairmarket.common.NotificationUtil.NotificationProperties;
import com.toolinc.openairmarket.persistence.cloud.ProductMeasureUnitRepository;
import com.toolinc.openairmarket.persistence.cloud.ProductsRepository;
import com.toolinc.openairmarket.persistence.cloud.QuickAccessProductRepository;
import com.toolinc.openairmarket.persistence.cloud.SaleRepository;
import com.toolinc.openairmarket.ui.MainActivity;
import com.toolinc.openairmarket.ui.component.CodeBarComponent;
import com.toolinc.openairmarket.ui.component.QuickAccessComponent;
import com.toolinc.openairmarket.ui.fragment.base.BaseFragment;
import com.toolinc.openairmarket.ui.fragment.inject.Annotations.Sale;
import com.toolinc.openairmarket.ui.fragment.inject.Annotations.Sale.Failed;
import com.toolinc.openairmarket.ui.fragment.inject.Annotations.Sale.Succeed;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel;
import com.toolinc.openairmarket.viewmodel.ReceiptsViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import java.math.BigDecimal;
import javax.inject.Inject;
import timber.log.Timber;

/**
 * Receipts fragment to handle product search and append.
 */
@AndroidEntryPoint
public class ReceiptsFragment extends BaseFragment {

  private static final String TAG = ReceiptsFragment.class.getSimpleName();

  @Inject ViewModelProvider.Factory viewModelFactory;
  @Inject ProductsRepository productsRepository;
  @Inject ProductMeasureUnitRepository productMeasureUnitRepository;
  @Inject SaleRepository saleRepository;
  @Inject QuickAccessProductRepository quickAccessProductRepository;
  @Inject @Sale ChannelProperties channelProperties;
  @Inject @Succeed NotificationProperties saleSucceedNotification;
  @Inject @Failed NotificationProperties saleFailedNotification;

  @BindView(R.id.tab_layout)
  TabLayout tabLayout;

  @BindView(R.id.view_pager)
  ViewPager viewPager;

  @Nullable
  @BindView(R.id.text_input_edit_text)
  TextInputEditText textInputEditText;

  @Nullable
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;

  @BindView(R.id.cancel_btn)
  Button cancelBtn;

  @BindView(R.id.pay_btn)
  Button payBtn;

  @Nullable RecyclerView recyclerViewQuickButtons;
  @Nullable LottieAnimationView progressBarQuickAccess;

  private ReceiptsViewModel receiptsViewModel;
  private ReceiptFragmentStatePagerAdapter receiptFragmentStatePagerAdapter;
  // Portrait variables
  private @Nullable
  BottomAppBar bottomAppBar;
  private @Nullable
  FloatingActionButton floatingActionButton;
  private @Nullable
  BottomSheetDialog bottomSheetDialog;
  private @Nullable CodeBarComponent codeBarComponent;
  private @Nullable QuickAccessComponent quickAccessComponent;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    receiptsViewModel =
        ViewModelProviders.of((MainActivity) getActivity(), viewModelFactory)
            .get(ReceiptsViewModel.class);
  }

  @Override
  public void onCreate(@Nullable Bundle bundle) {
    super.onCreate(bundle);
    NotificationUtil.createChannel(getContext(), channelProperties);
    setHasOptionsMenu(true);
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
            getParentFragmentManager(), receiptsViewModel, getContext(), viewPager, tabLayout);
    receiptsViewModel.getReceiptFragmentStatePagerAdapter()
        .setValue(receiptFragmentStatePagerAdapter);
    payBtn.setOnClickListener(this::displayCompleteSaleDialog);
    cancelBtn.setOnClickListener(this::displayCancelSaleDialog);
    if (isPortrait()) {
      bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
      floatingActionButton = getActivity().findViewById(R.id.fab_add_to_receipt);
      setUpBottomSheet();
      bottomAppBar.replaceMenu(R.menu.bottom_app_bar_receipts);
      bottomAppBar.setOnMenuItemClickListener(this::onMenuItemClick);
      codeBarComponent = CodeBarComponent.builder()
          .setFloatingActionButton(floatingActionButton)
          .setTextInputEditText(textInputEditText)
          .setProgressBar(progressBar)
          .setProductsRepository(productsRepository)
          .setProductMeasureUnitRepository(productMeasureUnitRepository)
          .setReceiptFragmentStatePagerAdapter(receiptFragmentStatePagerAdapter)
          .build();
    }
    return view;
  }

  private void setUpBottomSheet() {
    bottomSheetDialog = new BottomSheetDialog(getActivity());
    bottomSheetDialog.setContentView(R.layout.bottomsheet_quick_access_product);
    recyclerViewQuickButtons = bottomSheetDialog.findViewById(R.id.quick_access_btn);
    progressBarQuickAccess = bottomSheetDialog.findViewById(R.id.quick_access_progress_bar);
    quickAccessComponent = QuickAccessComponent
        .builder()
        .setContext(getContext())
        .setLifecycleOwner(getViewLifecycleOwner())
        .setReceiptsViewModel(receiptsViewModel)
        .setQuickAccessProductRepository(quickAccessProductRepository)
        .setLottieAnimationView(progressBarQuickAccess)
        .setRecyclerView(recyclerViewQuickButtons)
        .setOnClickQuickAccess(this::onClickQuickAccess)
        .build();
    View bottomSheetInternal = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
    BottomSheetBehavior.from(bottomSheetInternal).setPeekHeight(250);
  }

  private boolean onMenuItemClick(MenuItem menuItem) {
    if (menuItem.getItemId() == R.id.quick_access) {
      bottomSheetDialog.show();
      return true;
    }
    return false;
  }

  private void onClickQuickAccess(String productId) {
    productsRepository
        .findProductById(productId, product -> codeBarComponent.addProduct(product),
            e -> Timber.tag(TAG).e(e), task -> {
            });
  }

  private void displayCompleteSaleDialog(View view) {
    if (receiptFragmentStatePagerAdapter.getReceiptViewModel().getLines().getValue().size() > 0) {
      final AlertDialog alertDialog =
          new AlertDialog.Builder(getContext()).setView(R.layout.dialog_complete_sale).create();
      alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                        //  this situation better.
                        Timber.tag(TAG).d("Sale: [%s].", sale.id());
                        NotificationUtil.notify(getContext(), saleSucceedNotification);
                      },
                      (exc) -> {
                        // TODO (edgarrico): In case of a failure consider storing such information
                        //  in a local database to further audit in case the connection is lost and
                        //  a sale was actually performed.
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
      alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
}
