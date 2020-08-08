package com.toolinc.openairmarket.model;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.annotation.StyleRes;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.R;

/**
 * Specifies the product id and short description that will be render on the quick access layout.
 */
@AutoValue
public abstract class QuickAccess {

  @StyleRes private static final int BUTTON = R.style.Widget_Pos_Button_OutlinedButton_Blue;

  @ColorRes private static final int BUTTON_RIPPLE = R.color.color_secondary_variant;

  @StyleRes
  private static final int BUTTON_ACCENT = R.style.Widget_Pos_Button_OutlinedButton_Yellow;

  @ColorRes
  private static final int BUTTON_ACCENT_RIPPLE = R.color.amber_300;

  public abstract String productId();

  public abstract String shortDesc();

  @StyleRes
  public abstract int style();

  @ColorRes
  public abstract int rippleColor();

  public static final Builder builder() {
    return new AutoValue_QuickAccess.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setProductId(String productId);

    public abstract Builder setShortDesc(String shortDesc);

    public abstract Builder setStyle(@StyleRes int style);

    public abstract Builder setRippleColor(@ColorRes int rippleColor);

    public abstract QuickAccess build();
  }

  // TODO (edgarrico): Move this to a settings so that it can be changed and perhaps also store this
  //  information in room.
  public static ImmutableList<QuickAccess> quickAccessesButtons(Context context) {
    return ImmutableList.of(
        QuickAccess.builder()
            .setStyle(BUTTON)
            .setRippleColor(BUTTON_RIPPLE)
            .setProductId(context.getString(R.string.product_one_id))
            .setShortDesc(context.getString(R.string.product_one_label))
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON_ACCENT)
            .setRippleColor(BUTTON_ACCENT_RIPPLE)
            .setProductId(context.getString(R.string.product_two_id))
            .setShortDesc(context.getString(R.string.product_two_label))
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON)
            .setRippleColor(BUTTON_RIPPLE)
            .setProductId(context.getString(R.string.product_three_id))
            .setShortDesc(context.getString(R.string.product_three_label))
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON_ACCENT)
            .setRippleColor(BUTTON_ACCENT_RIPPLE)
            .setProductId(context.getString(R.string.product_four_id))
            .setShortDesc(context.getString(R.string.product_four_label))
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON)
            .setRippleColor(BUTTON_RIPPLE)
            .setProductId(context.getString(R.string.product_five_id))
            .setShortDesc(context.getString(R.string.product_five_label))
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON_ACCENT)
            .setRippleColor(BUTTON_ACCENT_RIPPLE)
            .setProductId(context.getString(R.string.product_six_id))
            .setShortDesc(context.getString(R.string.product_six_label))
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON)
            .setRippleColor(BUTTON_RIPPLE)
            .setProductId(context.getString(R.string.product_seven_id))
            .setShortDesc(context.getString(R.string.product_seven_label))
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON_ACCENT)
            .setRippleColor(BUTTON_ACCENT_RIPPLE)
            .setProductId(context.getString(R.string.product_eigth_id))
            .setShortDesc(context.getString(R.string.product_eigth_label))
            .build());
  }
}
