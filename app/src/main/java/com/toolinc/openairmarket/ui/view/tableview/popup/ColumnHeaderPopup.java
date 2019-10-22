package com.toolinc.openairmarket.ui.view.tableview.popup;

import androidx.appcompat.widget.PopupMenu;

import com.evrencoskun.tableview.ITableView;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import com.toolinc.openairmarket.ui.view.tableview.holder.ColumnHeaderViewHolder;

/** Provides a Popup for the column headers. */
@AutoValue
public abstract class ColumnHeaderPopup {

  public abstract PopupMenu popupMenu();

  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link ColumnHeaderPopup}. */
  public static class Builder {

    private ColumnHeaderViewHolder columnHeaderViewHolder;
    private ITableView iTableView;
    private ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();

    private Builder() {}

    public Builder setITableView(ITableView iTableView) {
      this.iTableView = iTableView;
      return this;
    }

    public Builder setColumnHeaderViewHolder(ColumnHeaderViewHolder columnHeaderViewHolder) {
      this.columnHeaderViewHolder = columnHeaderViewHolder;
      return this;
    }

    public Builder addColumnPosition(int columnPosition) {
      builder.add(columnPosition);
      return this;
    }

    public ColumnHeaderPopup build() {
      PopupMenu popupMenu =
          new ColumnHeaderLongPressPopup(iTableView, columnHeaderViewHolder, builder.build());
      return new AutoValue_ColumnHeaderPopup(popupMenu);
    }
  }
}
