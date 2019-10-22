package com.toolinc.openairmarket.ui.view.tableview.holder;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.view.tableview.model.CellModel;

/** Cell view holder for a table view. */
public final class CellViewHolder extends AbstractViewHolder {

  private final TextView cellTextView;
  private final LinearLayout cellContainer;

  public CellViewHolder(@NonNull View view) {
    super(view);
    cellTextView = view.findViewById(R.id.cell_data);
    cellContainer = view.findViewById(R.id.cell_container);
  }

  public void setCellModel(CellModel cellModel, int columnPosition) {
    // Change textView align by column
    cellTextView.setGravity(Gravity.CENTER_VERTICAL);
    // Set text
    cellTextView.setText(String.valueOf(cellModel.getData()));
    // It is necessary to remeasure itself.
    cellContainer.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
    cellTextView.requestLayout();
  }

  @Override
  public void setSelected(SelectionState selectionState) {
    super.setSelected(selectionState);
    if (selectionState == SelectionState.SELECTED) {
      cellTextView.setTextColor(
          ContextCompat.getColor(cellTextView.getContext(), R.color.selected_text_color));
    } else {
      cellTextView.setTextColor(
          ContextCompat.getColor(cellTextView.getContext(), R.color.unselected_text_color));
    }
  }
}
