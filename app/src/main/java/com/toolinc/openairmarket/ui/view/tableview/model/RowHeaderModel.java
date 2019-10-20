package com.toolinc.openairmarket.ui.view.tableview.model;

import com.google.auto.value.AutoValue;

/** Row model for a table view. */
@AutoValue
public abstract class RowHeaderModel {

  public abstract String getData();

  public static final RowHeaderModel create(String rowHeaderName) {
    return new AutoValue_RowHeaderModel(rowHeaderName);
  }
}
