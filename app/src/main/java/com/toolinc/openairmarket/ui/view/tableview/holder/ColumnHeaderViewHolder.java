package com.toolinc.openairmarket.ui.view.tableview.holder;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.evrencoskun.tableview.sort.SortState;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.view.tableview.model.ColumnHeaderModel;

/** Column header holder for a table view. */
public final class ColumnHeaderViewHolder extends AbstractSorterViewHolder {

  private final LinearLayout headerContainer;
  private final TextView headerTextView;
  private final ImageButton headerSortButton;
  private final ITableView iTableView;
  private final View.OnClickListener mSortButtonClickListener =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (getSortState() == SortState.ASCENDING) {
            iTableView.sortColumn(getAdapterPosition(), SortState.DESCENDING);
          } else if (getSortState() == SortState.DESCENDING) {
            iTableView.sortColumn(getAdapterPosition(), SortState.ASCENDING);
          } else {
            // Default one
            iTableView.sortColumn(getAdapterPosition(), SortState.DESCENDING);
          }
        }
      };

  public ColumnHeaderViewHolder(@NonNull View itemView, @NonNull ITableView iTableView) {
    super(itemView);
    this.iTableView = iTableView;
    headerTextView = itemView.findViewById(R.id.column_header_textView);
    headerContainer = itemView.findViewById(R.id.column_header_container);
    headerSortButton = itemView.findViewById(R.id.column_header_sort_imageButton);
    // Set click listener to the sort button
    headerSortButton.setOnClickListener(mSortButtonClickListener);
  }

  public void setColumnHeaderModel(ColumnHeaderModel columnHeaderModel, int columnPosition) {
    // Change alignment of textView
    headerTextView.setGravity(COLUMN_TEXT_ALIGNS[columnPosition] | Gravity.CENTER_VERTICAL);
    // Set text data
    headerTextView.setText(columnHeaderModel.getData());
    // It is necessary to remeasure itself.
    headerContainer.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
    headerTextView.requestLayout();
  }

  @Override
  public void setSelected(SelectionState selectionState) {
    super.setSelected(selectionState);

    int nBackgroundColorId;
    int nForegroundColorId;

    if (selectionState == SelectionState.SELECTED) {
      nBackgroundColorId = R.color.selected_background_color;
      nForegroundColorId = R.color.selected_text_color;

    } else if (selectionState == SelectionState.UNSELECTED) {
      nBackgroundColorId = R.color.unselected_header_background_color;
      nForegroundColorId = R.color.unselected_text_color;
    } else {
      nBackgroundColorId = R.color.shadow_background_color;
      nForegroundColorId = R.color.unselected_text_color;
    }

    headerContainer.setBackgroundColor(
        ContextCompat.getColor(headerContainer.getContext(), nBackgroundColorId));
    headerTextView.setTextColor(
        ContextCompat.getColor(headerContainer.getContext(), nForegroundColorId));
  }

  @Override
  public void onSortingStatusChanged(SortState sortState) {
    super.onSortingStatusChanged(sortState);
    // It is necessary to remeasure itself.
    headerContainer.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;

    controlSortState(sortState);

    headerTextView.requestLayout();
    headerSortButton.requestLayout();
    headerContainer.requestLayout();
    itemView.requestLayout();
  }

  private void controlSortState(SortState sortState) {
    if (sortState == SortState.ASCENDING) {
      headerSortButton.setVisibility(View.VISIBLE);
      headerSortButton.setImageResource(R.drawable.ic_down);
    } else if (sortState == SortState.DESCENDING) {
      headerSortButton.setVisibility(View.VISIBLE);
      headerSortButton.setImageResource(R.drawable.ic_up);
    } else {
      headerSortButton.setVisibility(View.GONE);
    }
  }

  public static final int[] COLUMN_TEXT_ALIGNS = {
    // Id
    Gravity.CENTER,
    // Name
    Gravity.LEFT,
    // Nickname
    Gravity.LEFT,
    // Email
    Gravity.LEFT,
    // BirthDay
    Gravity.CENTER,
    // Gender (Sex)
    Gravity.CENTER,
    // Age
    Gravity.CENTER,
    // Job
    Gravity.LEFT,
    // Salary
    Gravity.CENTER,
    // CreatedAt
    Gravity.CENTER,
    // UpdatedAt
    Gravity.CENTER,
    // Address
    Gravity.LEFT,
    // Zip Code
    Gravity.RIGHT,
    // Phone
    Gravity.RIGHT,
    // Fax
    Gravity.RIGHT
  };
}
