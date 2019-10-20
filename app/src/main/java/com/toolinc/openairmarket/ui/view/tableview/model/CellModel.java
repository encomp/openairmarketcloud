package com.toolinc.openairmarket.ui.view.tableview.model;

import com.evrencoskun.tableview.sort.ISortableModel;
import com.google.auto.value.AutoValue;

/** Cell model that stores the data for a specific cell. */
@AutoValue
public abstract class CellModel implements ISortableModel {

  public abstract Object getData();
  @Override
  public abstract String getId();
  @Override
  public abstract Object getContent();

  public static final CellModel create(String id, Object data) {
    return new AutoValue_CellModel(data, id, data);
  }
}
