package com.toolinc.openairmarket.ui.view.tableview.model;

import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.evrencoskun.tableview.sort.ISortableModel;
import com.google.auto.value.AutoValue;
import com.google.common.collect.Table.Cell;

/** Cell model that stores the data for a specific cell. */
@AutoValue
public abstract class CellModel implements ISortableModel {

  public abstract Object getData();

  @Override
  public abstract String getId();

  @Override
  public abstract Object getContent();

  public abstract int getWidth();

  public static final CellModel create(String id, Object data) {
    return new AutoValue_CellModel(data, id, data, LayoutParams.MATCH_PARENT);
  }

  public static final CellModel create(String id, Object data, int width) {
    return new AutoValue_CellModel(data, id, data, width);
  }
}
