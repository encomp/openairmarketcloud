package com.toolinc.openairmarket.ui.view.tableview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import com.toolinc.openairmarket.ui.view.tableview.holder.ColumnHeaderViewHolder;
import com.toolinc.openairmarket.ui.view.tableview.holder.RowHeaderViewHolder;
import com.toolinc.openairmarket.ui.view.tableview.popup.ColumnHeaderPopup;
import com.toolinc.openairmarket.ui.view.tableview.popup.RowHeaderPopup;

import timber.log.Timber;

/** Defines a table view listener that renders a popup for the column headers. */
@AutoValue
public abstract class TableViewListener implements ITableViewListener {

  private static final String TAG = TableViewListener.class.getSimpleName();

  abstract Optional<ColumnHeaderPopup.Builder> columnHeaderPopup();

  abstract Optional<RowHeaderPopup.Builder> rowHeaderPopup();

  abstract Optional<OnColumnHeaderClickListener> onColumnHeaderClick();

  abstract Optional<onRowHeaderClickListener> onRowHeaderClick();

  abstract Optional<onCellClickListener> onCellClick();

  /** Interface definition for a callback to be invoked when a column header is clicked. */
  public interface OnColumnHeaderClickListener {

    /** Called when a column header has been clicked. */
    void onClick(@NonNull RecyclerView.ViewHolder columnHeaderView, int column);
  }

  /** Interface definition for a callback to be invoked when a row header is clicked. */
  public interface onRowHeaderClickListener {

    /** Called when a row header has been clicked. */
    void onClick(@NonNull RecyclerView.ViewHolder rowHeaderView, int row);
  }

  /** Interface definition for a callback to be invoked when a cell is clicked. */
  public interface onCellClickListener {

    /** Called when a cell header has been clicked. */
    void onClick(@NonNull RecyclerView.ViewHolder cellView, int column, int row);
  }

  @Override
  public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
    if (onCellClick().isPresent()) {
      onCellClick().get().onClick(cellView, column, row);
    }
  }

  @Override
  public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
    Timber.d(TAG, "onCellLongPressed has been clicked for " + row);
  }

  @Override
  public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
    if (onColumnHeaderClick().isPresent()) {
      onColumnHeaderClick().get().onClick(columnHeaderView, column);
    }
  }

  @Override
  public void onColumnHeaderLongPressed(
      @NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
    if (columnHeaderPopup().isPresent()) {
      columnHeaderPopup()
          .get()
          .setColumnHeaderViewHolder((ColumnHeaderViewHolder) columnHeaderView)
          .build()
          .popupMenu()
          .show();
    }
  }

  @Override
  public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
    if (onRowHeaderClick().isPresent()) {
      onRowHeaderClick().get().onClick(rowHeaderView, row);
    }
  }

  @Override
  public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
    if (rowHeaderPopup().isPresent()) {
      rowHeaderPopup()
          .get()
          .setRowHeaderViewHolder((RowHeaderViewHolder) rowHeaderView)
          .build()
          .popupMenu()
          .show();
    }
  }

  public static final Builder builder(ITableView iTableView) {
    return new Builder(iTableView);
  }

  /** Builder class for {@link TableViewListener}. */
  public static final class Builder {

    private final ITableView iTableView;
    private Optional<ColumnHeaderPopup.Builder> columnHeaderPopup = Optional.absent();
    private Optional<RowHeaderPopup.Builder> rowHeaderPopup = Optional.absent();
    private Optional<OnColumnHeaderClickListener> onColumnHeaderClick = Optional.absent();
    private Optional<onRowHeaderClickListener> onRowHeaderClick = Optional.absent();
    private Optional<onCellClickListener> onCellClick = Optional.absent();

    private Builder(ITableView iTableView) {
      this.iTableView = iTableView;
    }

    public Builder withColumnHeaderPopup() {
      columnHeaderPopup = Optional.of(ColumnHeaderPopup.builder().setITableView(iTableView));
      return this;
    }

    public Builder addColumnPosition(int columnPosition) {
      if (!columnHeaderPopup.isPresent()) {
        withColumnHeaderPopup();
      }
      columnHeaderPopup.get().addColumnPosition(columnPosition);
      return this;
    }

    public Builder withRowHeaderPopup() {
      rowHeaderPopup = Optional.of(RowHeaderPopup.builder().setITableView(iTableView));
      return this;
    }

    public Builder setOnColumnHeaderClickListener(OnColumnHeaderClickListener onColumnHeaderClick) {
      this.onColumnHeaderClick = Optional.fromNullable(onColumnHeaderClick);
      return this;
    }

    public Builder setOnRowHeaderClickListener(onRowHeaderClickListener onRowHeaderClick) {
      this.onRowHeaderClick = Optional.fromNullable(onRowHeaderClick);
      return this;
    }

    public Builder setOnCellClickListener(onCellClickListener onCellClick) {
      this.onCellClick = Optional.fromNullable(onCellClick);
      return this;
    }

    public TableViewListener build() {
      return new AutoValue_TableViewListener(
          columnHeaderPopup, rowHeaderPopup, onColumnHeaderClick, onRowHeaderClick, onCellClick);
    }
  }
}
