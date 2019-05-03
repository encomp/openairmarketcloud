package com.toolinc.openairmarket.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.toolinc.openairmarket.databinding.ItemSaleLineBinding;
import com.toolinc.openairmarket.pos.persistence.model.sale.SaleLine;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel;

import javax.inject.Inject;

/**
 * Adapter that provides a binding from an {@link ImmutableList} of {@link SaleLine} to the view
 * {@code R.layout.item_sale_line} displayed within a RecyclerView.
 */
public final class SaleLineListAdapter
    extends RecyclerView.Adapter<SaleLineListAdapter.SaleLineViewHolder>
    implements Observer<ImmutableList<SaleLine>> {

  private static final ImmutableList<SaleLine> EMPTY = ImmutableList.copyOf(Lists.newArrayList());
  private final ReceiptViewModel receiptViewModel;
  private ImmutableList<SaleLine> saleLines = EMPTY;

  @Inject
  public SaleLineListAdapter(ReceiptViewModel receiptViewModel) {
    this.receiptViewModel = receiptViewModel;
    receiptViewModel.getLines().observeForever(this::onChanged);
  }

  @NonNull
  @Override
  public SaleLineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    ItemSaleLineBinding itemBinding = ItemSaleLineBinding.inflate(inflater, viewGroup, false);
    return new SaleLineViewHolder(itemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull SaleLineViewHolder saleLineViewHolder, int position) {
    saleLineViewHolder.itemSaleLineBinding.setSaleLine(saleLines.get(0));
  }

  @Override
  public int getItemCount() {
    return saleLines.size();
  }

  @Override
  public void onChanged(ImmutableList<SaleLine> saleLines) {
    if (Optional.fromNullable(saleLines).isPresent()) {
      this.saleLines = saleLines;
    } else {
      this.saleLines = EMPTY;
    }
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
