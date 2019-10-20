package com.toolinc.openairmarket.ui.view.tableview.popup;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.PopupMenu;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.sort.SortState;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.view.tableview.holder.ColumnHeaderViewHolder;

/** Defines the long press functionality of the table view headers. */
public class ColumnHeaderLongPressPopup extends PopupMenu
    implements PopupMenu.OnMenuItemClickListener {

  private static final String LOG_TAG = ColumnHeaderLongPressPopup.class.getSimpleName();

  private static final int ASCENDING = 1;
  private static final int DESCENDING = 2;
  private static final int CLEAR = 3;

  private final ITableView iTableView;
  private final Context context;
  private final ColumnHeaderViewHolder columnHeaderViewHolder;
  private int position;

  public ColumnHeaderLongPressPopup(
      ColumnHeaderViewHolder columnHeaderViewHolder, ITableView iTableView) {
    super(columnHeaderViewHolder.itemView.getContext(), columnHeaderViewHolder.itemView);
    this.iTableView = iTableView;
    this.context = columnHeaderViewHolder.itemView.getContext();
    this.position = columnHeaderViewHolder.getAdapterPosition();
    this.columnHeaderViewHolder =
        (ColumnHeaderViewHolder)
            iTableView.getColumnHeaderRecyclerView().findViewHolderForAdapterPosition(position);
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
    // Determine which one shouldn't be visible
    SortState sortState = iTableView.getSortingStatus(position);
    if (sortState == SortState.UNSORTED) {
      getMenu().getItem(2).setVisible(false);
    } else if (sortState == SortState.DESCENDING) {
      // Hide DESCENDING menu item
      getMenu().getItem(1).setVisible(false);
    } else if (sortState == SortState.ASCENDING) {
      // Hide ASCENDING menu item
      getMenu().getItem(0).setVisible(false);
    }
  }

  @Override
  public boolean onMenuItemClick(MenuItem menuItem) {
    // Note: item id is index of menu item..
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
    // Recalculate of the width values of the columns
    iTableView.remeasureColumnWidth(position);
    return true;
  }
}
