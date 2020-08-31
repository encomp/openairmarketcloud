package com.toolinc.openairmarket.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.databinding.ItemQuickAccessBinding;
import com.toolinc.openairmarket.viewmodel.QuickAccess;

/**
 * Adapter that provides a binding from an {@link ImmutableList} of {@link QuickAccess} to the view
 * {@code R.layout.item_quick_access} displayed within a RecyclerView.
 */
public final class QuickAccessListAdapter
    extends RecyclerView.Adapter<QuickAccessListAdapter.QuickAccessViewHolder> {

  /** Specifies the quick access product that was click by the user. */
  public interface OnClick {
    void onClickQuickAccess(String productId);
  }

  private final ImmutableList<QuickAccess> quickAccesses;
  private final OnClick onClick;

  public QuickAccessListAdapter(ImmutableList<QuickAccess> quickAccesses, OnClick onClick) {
    this.quickAccesses =
        Preconditions.checkNotNull(quickAccesses, "Quick Access items are missing.");
    this.onClick = Preconditions.checkNotNull(onClick, "OnClick listener is missing.");
  }

  @NonNull
  @Override
  public QuickAccessViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    ItemQuickAccessBinding itemBinding = ItemQuickAccessBinding.inflate(inflater, viewGroup, false);
    return new QuickAccessViewHolder(itemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull QuickAccessViewHolder holder, int position) {
    QuickAccess quickAccess = quickAccesses.get(position);
    Chip textChip =
        holder.itemBinding.getRoot().findViewById(R.id.quick_access_btn_container);
    textChip.setChipStrokeColorResource(quickAccess.textColor());
    textChip.setRippleColorResource(quickAccess.rippleColor());
    textChip.setOnClickListener(holder);
    holder.itemBinding.setQuickAccess(quickAccess);
  }

  @Override
  public int getItemCount() {
    return quickAccesses.size();
  }

  /** Describes a {@link QuickAccess} item about its place within the RecyclerView. */
  final class QuickAccessViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {

    private final ItemQuickAccessBinding itemBinding;

    public QuickAccessViewHolder(@NonNull ItemQuickAccessBinding itemBinding) {
      super(itemBinding.getRoot());
      this.itemBinding = itemBinding;
    }

    @Override
    public void onClick(View v) {
      onClick.onClickQuickAccess(itemBinding.getQuickAccess().productId());
    }
  }
}
