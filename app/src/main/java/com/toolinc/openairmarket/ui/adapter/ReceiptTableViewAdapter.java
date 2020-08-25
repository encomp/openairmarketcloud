package com.toolinc.openairmarket.ui.adapter;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.ui.view.tableview.model.CellModel;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel.ProductLine;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Receipt table view adapter.
 */
public class ReceiptTableViewAdapter implements Function<ProductLine, ImmutableList<CellModel>> {

  @Nullable
  @Override
  public ImmutableList<CellModel> apply(@Nullable ProductLine productLine) {
    return ImmutableList.<CellModel>builder()
        .add(CellModel
            .create(productLine.saleLine().getProduct(), productLine.saleLine().getProduct(), 300))
        .add(CellModel
            .create(productLine.saleLine().getName(), productLine.saleLine().getName(), 800))
        .add(CellModel
            .create(productLine.saleLine().getPrice(), productLine.saleLine().price(), 180))
        .add(CellModel
            .create(productLine.saleLine().getQuantity(), productLine.saleLine().quantity(), 120))
        .add(CellModel
            .create(productLine.saleLine().getTotal(), productLine.saleLine().total(), 180))
        .build();
  }
}
