package com.toolinc.openairmarket.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.persistence.cloud.ProductsRepository;
import com.toolinc.openairmarket.ui.MainActivity;
import com.toolinc.openairmarket.ui.adapter.QuickAccessProductListAdapter;
import com.toolinc.openairmarket.ui.component.CodeBarComponent;
import com.toolinc.openairmarket.viewmodel.QuickAccessProductViewModel;
import com.toolinc.openairmarket.viewmodel.ReceiptsViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;
import timber.log.Timber;

/** SearchBox fragment to handle product search and quick access products. */
@AndroidEntryPoint
public class SearchBoxFragment extends Fragment {

  private static final String TAG = SearchBoxFragment.class.getSimpleName();

  @Inject ViewModelProvider.Factory viewModelFactory;
  @Inject ProductsRepository productsRepository;

  @BindView(R.id.text_input_edit_text)
  TextInputEditText textInputEditText;

  @BindView(R.id.progress_bar)
  ProgressBar progressBar;

  @BindView(R.id.fab_add_to_receipt)
  FloatingActionButton floatingActionButton;

  @BindView(R.id.quick_access_btn)
  RecyclerView recyclerViewQuickButtons;

  private ReceiptsViewModel receiptsViewModel;
  private ReceiptFragmentStatePagerAdapter receiptFragmentStatePagerAdapter;
  private CodeBarComponent codeBarComponent;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    receiptsViewModel =
        ViewModelProviders.of((MainActivity) getActivity(), viewModelFactory)
            .get(ReceiptsViewModel.class);
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view =
        layoutInflater.inflate(R.layout.fragment_search_box, viewGroup, false /* attachToRoot */);
    ButterKnife.bind(this, view);
    receiptsViewModel.getReceiptFragmentStatePagerAdapter()
        .observe(getViewLifecycleOwner(), receiptFragmentStatePagerAdapter -> {
          SearchBoxFragment.this.receiptFragmentStatePagerAdapter = receiptFragmentStatePagerAdapter;
          initCodeBarComponent();
        });
    initCodeBarComponent();
    initQuickButtons();
    return view;
  }

  private void initCodeBarComponent() {
    if (codeBarComponent == null && textInputEditText != null && floatingActionButton != null
        && receiptFragmentStatePagerAdapter != null) {
      codeBarComponent = CodeBarComponent.builder()
          .setProductsRepository(productsRepository)
          .setTextInputEditText(textInputEditText)
          .setProgressBar(progressBar)
          .setFloatingActionButton(floatingActionButton)
          .setReceiptFragmentStatePagerAdapter(receiptFragmentStatePagerAdapter).build();
    }
  }

  private void initQuickButtons() {
    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
    recyclerViewQuickButtons.setLayoutManager(gridLayoutManager);
    QuickAccessProductListAdapter adapter =
        new QuickAccessProductListAdapter(
            QuickAccessProductViewModel.quickAccessesButtons(getContext()), this::onClickQuickAccess);
    recyclerViewQuickButtons.setAdapter(adapter);
  }

  private void onClickQuickAccess(String productId) {
    productsRepository
        .findProductById(productId, product -> receiptFragmentStatePagerAdapter.addProduct(product),
            e -> Timber.tag(TAG).e(e), task -> {
            });
  }
}
