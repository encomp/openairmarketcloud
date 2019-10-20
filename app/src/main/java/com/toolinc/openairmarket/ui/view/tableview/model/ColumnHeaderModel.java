package com.toolinc.openairmarket.ui.view.tableview.model;

import com.google.auto.value.AutoValue;

/** Header model for a table view. */
@AutoValue
public abstract class ColumnHeaderModel {

  public abstract String getData();

  public static final ColumnHeaderModel create(String headerName) {
    return new AutoValue_ColumnHeaderModel(headerName);
  }
}
