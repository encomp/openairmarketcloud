package com.toolinc.openairmarket.ui.view.tableview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.view.tableview.holder.CellViewHolder;
import com.toolinc.openairmarket.ui.view.tableview.holder.ColumnHeaderViewHolder;
import com.toolinc.openairmarket.ui.view.tableview.holder.RowHeaderViewHolder;
import com.toolinc.openairmarket.ui.view.tableview.model.CellModel;
import com.toolinc.openairmarket.ui.view.tableview.model.ColumnHeaderModel;
import com.toolinc.openairmarket.ui.view.tableview.model.RowHeaderModel;
import com.toolinc.openairmarket.ui.view.tableview.model.TableViewModel;

/** Define the table adapter for a table view. */
public final class TableViewAdapter
    extends AbstractTableAdapter<ColumnHeaderModel, RowHeaderModel, CellModel> {

  public TableViewAdapter(@NonNull Context context) {
    super(context);
  }

  @Override
  public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
    View layout =
        LayoutInflater.from(mContext).inflate(R.layout.tableview_cell_layout, parent, false);
    return new CellViewHolder(layout);
  }

  @Override
  public void onBindCellViewHolder(
      AbstractViewHolder holder, Object cellItemModel, int columnPosition, int rowPosition) {
    CellModel cell = (CellModel) cellItemModel;
    ((CellViewHolder) holder).setCellModel(cell, columnPosition);
  }

  @Override
  public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
    View layout =
        LayoutInflater.from(mContext)
            .inflate(R.layout.tableview_column_header_layout, parent, false);
    return new ColumnHeaderViewHolder(layout, getTableView());
  }

  @Override
  public void onBindColumnHeaderViewHolder(
      AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
    ColumnHeaderModel columnHeader = (ColumnHeaderModel) columnHeaderItemModel;
    ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;
    columnHeaderViewHolder.setColumnHeaderModel(columnHeader, columnPosition);
  }

  @Override
  public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
    View layout =
        LayoutInflater.from(mContext).inflate(R.layout.tableview_row_header_layout, parent, false);
    return new RowHeaderViewHolder(layout);
  }

  @Override
  public void onBindRowHeaderViewHolder(
      AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
    RowHeaderModel rowHeaderModel = (RowHeaderModel) rowHeaderItemModel;
    RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
    rowHeaderViewHolder.headerTextView().setText(rowHeaderModel.getData());
  }

  @Override
  public View onCreateCornerView() {
    return LayoutInflater.from(mContext).inflate(R.layout.tableview_corner_layout, null, false);
  }

  @Override
  public int getColumnHeaderItemViewType(int position) {
    return 0;
  }

  @Override
  public int getRowHeaderItemViewType(int position) {
    return 0;
  }

  @Override
  public int getCellItemViewType(int position) {
    return 0;
  }

  public void setTableViewModel(TableViewModel tableViewModel) {
    setAllItems(
        tableViewModel.columnHeaderModels(),
        tableViewModel.rowHeaderModels(),
        tableViewModel.cellModels());
  }
}
