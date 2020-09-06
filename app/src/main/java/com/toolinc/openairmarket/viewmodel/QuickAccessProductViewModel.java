package com.toolinc.openairmarket.viewmodel;

import android.content.Context;
import androidx.annotation.ColorRes;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.pos.persistence.model.ui.QuickAccessProduct;
import java.util.List;

/**
 * Specifies the product id and short description that will be render on the quick access layout.
 */
@AutoValue
public abstract class QuickAccessProductViewModel {

  @ColorRes
  private static final int BUTTON_COLOR = R.color.teal_800;

  @ColorRes
  private static final int BUTTON_RIPPLE = R.color.teal_300;

  @ColorRes
  private static final int BUTTON_ACCENT_COLOR = R.color.indigo_800;

  @ColorRes
  private static final int BUTTON_ACCENT_RIPPLE = R.color.indigo_300;

  public static final Builder builder() {
    return new AutoValue_QuickAccessProductViewModel.Builder();
  }

  public static ImmutableList<QuickAccessProductViewModel> quickAccessesButtons(Context context,
      List<QuickAccessProduct> quickAccessProducts) {
    ImmutableList.Builder<QuickAccessProductViewModel> builder = ImmutableList.builder();
    int counter = 1;
    for (QuickAccessProduct quickAccessProduct : quickAccessProducts) {
      boolean even = counter % 2 == 0;
      builder.add(
          QuickAccessProductViewModel.builder()
              .setQuickAccessProduct(quickAccessProduct)
              .setTextColor(even ? BUTTON_ACCENT_COLOR : BUTTON_COLOR)
              .setRippleColor(even ? BUTTON_ACCENT_RIPPLE : BUTTON_RIPPLE)
              .build());
      counter++;
    }
    return builder.build();
  }

  public abstract QuickAccessProduct quickAccessProduct();

  public String productId() {
    return quickAccessProduct().getProduct();
  }

  public String shortDesc() {
    return quickAccessProduct().getLabel();
  }

  @ColorRes
  public abstract int textColor();

  @ColorRes
  public abstract int rippleColor();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setQuickAccessProduct(QuickAccessProduct quickAccessProduct);

    public abstract Builder setTextColor(@ColorRes int textColor);

    public abstract Builder setRippleColor(@ColorRes int rippleColor);

    public abstract QuickAccessProductViewModel build();
  }
}
