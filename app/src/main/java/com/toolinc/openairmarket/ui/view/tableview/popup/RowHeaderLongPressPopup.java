package com.toolinc.openairmarket.ui.view.tableview.popup;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.PopupMenu;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.sort.SortState;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.view.tableview.holder.RowHeaderViewHolder;

/** Internal Popup to add long press functionality for rows of a table view headers. */
final class RowHeaderLongPressPopup extends PopupMenu implements PopupMenu.OnMenuItemClickListener {

  private static final int ASCENDING = 1;
  private static final int DESCENDING = 2;
  private static final int CLEAR = 3;

  private final ITableView iTableView;
  private final Context context;
  private final RowHeaderViewHolder rowHeaderViewHolder;
  private int position;

  RowHeaderLongPressPopup(ITableView iTableView, RowHeaderViewHolder rowHeaderViewHolder) {
    super(rowHeaderViewHolder.itemView.getContext(), rowHeaderViewHolder.itemView);
    this.iTableView = iTableView;
    this.context = rowHeaderViewHolder.itemView.getContext();
    this.position = rowHeaderViewHolder.getAdapterPosition();
    this.rowHeaderViewHolder =
        (RowHeaderViewHolder)
            iTableView.getRowHeaderRecyclerView().findViewHolderForAdapterPosition(position);
    initialize();
  }

  private void initialize() {
    createMenuItem();
    changeMenuItemVisibility();
    setOnMenuItemClickListener(this);
  }

  private void createMenuItem() {
    this.getMenu().add(Menu.NONE, ASCENDING, 0, context.getString(R.string.sort_ascending));
    this.getMenu().add(Menu.NONE, DESCENDING, 1, context.getString(R.string.sort_descending));
    this.getMenu().add(Menu.NONE, CLEAR, 2, context.getString(R.string.sort_clear));
  }

  private void changeMenuItemVisibility() {
    SortState sortState = iTableView.getSortingStatus(position);
    if (sortState == SortState.UNSORTED) {
      getMenu().getItem(2).setVisible(false);
    } else if (sortState == SortState.DESCENDING) {
      getMenu().getItem(1).setVisible(false);
    } else if (sortState == SortState.ASCENDING) {
      getMenu().getItem(0).setVisible(false);
    }
  }

  @Override
  public boolean onMenuItemClick(MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case CLEAR:
        iTableView.sortColumn(position, SortState.UNSORTED);
        break;
      case ASCENDING:
        iTableView.sortColumn(position, SortState.ASCENDING);
        break;
      case DESCENDING:
        iTableView.sortColumn(position, SortState.DESCENDING);
        break;
    }
    iTableView.remeasureColumnWidth(position);
    return true;
  }
}
