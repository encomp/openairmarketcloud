package com.toolinc.openairmarket.ui.view.tableview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.toolinc.openairmarket.ui.view.tableview.holder.ColumnHeaderViewHolder;
import com.toolinc.openairmarket.ui.view.tableview.popup.ColumnHeaderPopup;

import timber.log.Timber;

/** Defines a table view listener that renders a popup for the column headers. */
public class TableViewListener implements ITableViewListener {

  private static final String TAG = TableViewListener.class.getSimpleName();
  private final ITableView iTableView;

  public TableViewListener(ITableView iTableView) {
    this.iTableView = iTableView;
  }

  @Override
  public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
    Timber.d(TAG, "onCellClicked has been clicked for x= " + column + " y= " + row);
  }

  @Override
  public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
    Timber.d(TAG, "onCellLongPressed has been clicked for " + row);
  }

  @Override
  public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
    Timber.d(TAG, "onColumnHeaderClicked has been clicked for " + column);
  }

  @Override
  public void onColumnHeaderLongPressed(
      @NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
    // Create Long Press Popup
    ColumnHeaderPopup columnHeaderPopup =
        ColumnHeaderPopup.builder()
            .setITableView(iTableView)
            .setColumnHeaderViewHolder((ColumnHeaderViewHolder) columnHeaderView)
            .build();
    // Show
    columnHeaderPopup.popupMenu().show();
  }

  @Override
  public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
    Timber.d(TAG, "onRowHeaderClicked has been clicked for " + row);
  }

  @Override
  public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder owHeaderView, int row) {
    Timber.d(TAG, "onRowHeaderLongPressed has been clicked for " + row);
  }
}
