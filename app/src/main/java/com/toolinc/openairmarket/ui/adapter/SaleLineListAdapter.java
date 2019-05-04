package com.toolinc.openairmarket.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.databinding.ItemSaleLineBinding;
import com.toolinc.openairmarket.pos.persistence.model.sale.SaleLine;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel.ProductLine;

/**
 * Adapter that provides a binding from an {@link ImmutableList} of {@link SaleLine} to the view
 * {@code R.layout.item_sale_line} displayed within a RecyclerView.
 */
public final class SaleLineListAdapter
    extends RecyclerView.Adapter<SaleLineListAdapter.SaleLineViewHolder> {

  private ImmutableList<ProductLine> productLines = ImmutableList.of();

  @NonNull
  @Override
  public SaleLineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    ItemSaleLineBinding itemBinding = ItemSaleLineBinding.inflate(inflater, viewGroup, false);
    return new SaleLineViewHolder(itemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull SaleLineViewHolder saleLineViewHolder, int position) {
    saleLineViewHolder.itemSaleLineBinding.setSaleLine(productLines.get(position).saleLine());
  }

  @Override
  public int getItemCount() {
    return productLines.size();
  }

  public ImmutableList<ProductLine> getProductLines() {
    return productLines;
  }

  public void setProductLines(@NonNull ImmutableList<ProductLine> productLines) {
    this.productLines = productLines;
  }

  /** Describes a {@link SaleLine} item about its place within the RecyclerView. */
  public static final class SaleLineViewHolder extends RecyclerView.ViewHolder {

    private final ItemSaleLineBinding itemSaleLineBinding;

    public SaleLineViewHolder(@NonNull ItemSaleLineBinding itemSaleLineBinding) {
      super(itemSaleLineBinding.getRoot());
      this.itemSaleLineBinding = itemSaleLineBinding;
    }
  }
}
