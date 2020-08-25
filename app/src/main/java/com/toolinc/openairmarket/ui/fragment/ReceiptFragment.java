package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.evrencoskun.tableview.TableView;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.OpenAirMarketApplication;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.adapter.ReceiptTableViewAdapter;
import com.toolinc.openairmarket.ui.view.tableview.TableViewListener;
import com.toolinc.openairmarket.ui.view.tableview.TableViewModel;
import com.toolinc.openairmarket.ui.view.tableview.adapter.TableViewAdapter;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel.ProductLine;
import dagger.hilt.android.AndroidEntryPoint;
import java.math.BigDecimal;
import javax.inject.Inject;
import timber.log.Timber;

/** Receipt fragment to handle all the items of a sale. */
@AndroidEntryPoint
public final class ReceiptFragment extends Fragment {

  private static final String TAG = ReceiptFragment.class.getSimpleName();
  @Inject ViewModelProvider.Factory viewModelFactory;

  @BindView(R.id.table_layout)
  TableView tableView;

  @BindView(R.id.total_tv)
  TextView textView;

  private TableViewModel.Builder tableViewModel = TableViewModel
      .builder(new ReceiptTableViewAdapter());
  private TableViewAdapter tableViewAdapter;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    final View view = layoutInflater.inflate(R.layout.fragment_receipt, viewGroup, false);
    ButterKnife.bind(this, view);
    initializeTableView();
    return view;
  }

  private void initializeTableView() {
    tableViewAdapter = new TableViewAdapter(getContext());
    tableView.setAdapter(tableViewAdapter);
    tableView.setTableViewListener(
        TableViewListener.builder(tableView)
            .withColumnHeaderPopup()
            .addColumnPosition(0)
            .addColumnPosition(1)
            .withRowHeaderPopup()
            .setOnCellClickListener(
                (cellView, row, column) -> {
                  Toast.makeText(
                      ReceiptFragment.this.getContext(),
                      "" + row + ":" + column,
                      Toast.LENGTH_LONG)
                      .show();
                })
            .build());
  }

  public void newProductLines(ImmutableList<ProductLine> productLines) {
    Timber.tag(TAG).d("About to add more products: [%d].", productLines.size());
    showProgressBar();
    TableViewModel tableViewModel =
        TableViewModel.builder(new ReceiptTableViewAdapter())
            .addAllCellModels(productLines)
            .addColumnHeaderModel(getString(R.string.receipt_product_code), 300)
            .addColumnHeaderModel(getString(R.string.receipt_product_name), 800)
            .addColumnHeaderModel(getString(R.string.receipt_price), 180)
            .addColumnHeaderModel(getString(R.string.receipt_quantity), 120)
            .addColumnHeaderModel(getString(R.string.receipt_subtotal), 180)
            .build();
    tableViewAdapter.setTableViewModel(tableViewModel);
    hideProgressBar();
  }

  public void newTotalAmount(BigDecimal bigDecimal) {
    Timber.tag(TAG).d("New Total Amount: [%s].", bigDecimal.toPlainString());
    textView.setText(OpenAirMarketApplication.toString(bigDecimal));
  }

  private void showProgressBar() {
    //progressBar.setVisibility(View.VISIBLE);
    tableView.setVisibility(View.INVISIBLE);
  }

  private void hideProgressBar() {
    //progressBar.setVisibility(View.INVISIBLE);
    tableView.setVisibility(View.VISIBLE);
  }
}
