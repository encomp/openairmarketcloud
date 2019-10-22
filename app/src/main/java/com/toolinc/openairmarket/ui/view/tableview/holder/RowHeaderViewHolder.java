package com.toolinc.openairmarket.ui.view.tableview.holder;

import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.toolinc.openairmarket.R;

/** Row header view holder for a table view. */
public final class RowHeaderViewHolder extends AbstractViewHolder {

  private final TextView headerTextView;

  public RowHeaderViewHolder(View view) {
    super(view);
    headerTextView = view.findViewById(R.id.row_header_textview);
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
    itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), nBackgroundColorId));
    headerTextView.setTextColor(
        ContextCompat.getColor(headerTextView.getContext(), nForegroundColorId));
  }

  public TextView headerTextView() {
    return headerTextView;
  }
}
