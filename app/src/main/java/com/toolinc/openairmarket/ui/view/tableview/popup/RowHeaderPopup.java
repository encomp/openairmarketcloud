package com.toolinc.openairmarket.ui.view.tableview.popup;

import androidx.appcompat.widget.PopupMenu;

import com.evrencoskun.tableview.ITableView;
import com.google.auto.value.AutoValue;
import com.toolinc.openairmarket.ui.view.tableview.holder.RowHeaderViewHolder;

/** Provides a Popup for the row headers. */
@AutoValue
public abstract class RowHeaderPopup {

  public abstract PopupMenu popupMenu();

  public static Builder builder() {
    return new RowHeaderPopup.Builder();
  }

  /** Builder for {@link RowHeaderPopup}. */
  public static class Builder {

    private RowHeaderViewHolder rowHeaderViewHolder;
    private ITableView iTableView;

    private Builder() {}

    public Builder setITableView(ITableView iTableView) {
      this.iTableView = iTableView;
      return this;
    }

    public Builder setRowHeaderViewHolder(RowHeaderViewHolder rowHeaderViewHolder) {
      this.rowHeaderViewHolder = rowHeaderViewHolder;
      return this;
    }

    public RowHeaderPopup build() {
      PopupMenu popupMenu = new RowHeaderLongPressPopup(iTableView, rowHeaderViewHolder);
      return new AutoValue_RowHeaderPopup(popupMenu);
    }
  }
}
