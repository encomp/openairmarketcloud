package com.toolinc.openairmarket.ui.view.tableview.model;

import android.widget.LinearLayout;
import com.google.auto.value.AutoValue;

/** Header model for a table view. */
@AutoValue
public abstract class ColumnHeaderModel {

  public abstract String getData();

  public abstract int getWidth();

  public static final ColumnHeaderModel create(String headerName) {
    return new AutoValue_ColumnHeaderModel(headerName, LinearLayout.LayoutParams.WRAP_CONTENT);
  }

  public static final ColumnHeaderModel create(String headerName, int width) {
    return new AutoValue_ColumnHeaderModel(headerName, width);
  }
}
