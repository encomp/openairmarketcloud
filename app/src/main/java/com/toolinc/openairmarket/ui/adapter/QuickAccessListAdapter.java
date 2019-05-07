package com.toolinc.openairmarket.ui.adapter;

import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.databinding.ItemQuickAccessBinding;
import com.toolinc.openairmarket.model.QuickAccess;

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
    FrameLayout frameLayout =
        holder.itemBinding.getRoot().findViewById(R.id.quick_access_btn_container);
    ContextThemeWrapper contextThemeWrapper =
        new ContextThemeWrapper(holder.itemBinding.getRoot().getContext(), quickAccess.style());
    MaterialButton button = new MaterialButton(contextThemeWrapper, null, quickAccess.style());
    button.setBackgroundTintMode(PorterDuff.Mode.CLEAR);
    button.setRippleColorResource(quickAccess.rippleColor());
    button.setText(quickAccess.shortDesc());
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.gravity = Gravity.CENTER;
    button.setLayoutParams(layoutParams);
    button.setOnClickListener(holder);
    frameLayout.addView(button);
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
