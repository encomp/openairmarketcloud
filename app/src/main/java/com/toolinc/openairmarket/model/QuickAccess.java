package com.toolinc.openairmarket.model;

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

  @StyleRes private static final int BUTTON = R.style.Widget_Shrine_Button_OutlinedButton;

  @ColorRes private static final int BUTTON_RIPPLE = R.color.btn_text_btn_ripple_color_accent;

  @StyleRes
  private static final int BUTTON_ACCENT = R.style.Widget_Shrine_Button_OutlinedButton_Accent;

  @ColorRes
  private static final int BUTTON_ACCENT_RIPPLE = R.color.btn_text_btn_ripple_color_accent2;

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
  public static ImmutableList<QuickAccess> quickAccessesButtons() {
    return ImmutableList.of(
        QuickAccess.builder()
            .setStyle(BUTTON)
            .setRippleColor(BUTTON_RIPPLE)
            .setProductId("1")
            .setShortDesc("Fud Ham")
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON_ACCENT)
            .setRippleColor(BUTTON_ACCENT_RIPPLE)
            .setProductId("2")
            .setShortDesc("Turkey Ham")
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON)
            .setRippleColor(BUTTON_RIPPLE)
            .setProductId("3")
            .setShortDesc("Gali Ham")
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON_ACCENT)
            .setRippleColor(BUTTON_ACCENT_RIPPLE)
            .setProductId("5")
            .setShortDesc("American Cheese")
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON)
            .setRippleColor(BUTTON_RIPPLE)
            .setProductId("7")
            .setShortDesc("Cheese")
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON_ACCENT)
            .setRippleColor(BUTTON_ACCENT_RIPPLE)
            .setProductId("6")
            .setShortDesc("Oaxaca Chees")
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON)
            .setRippleColor(BUTTON_RIPPLE)
            .setProductId("12")
            .setShortDesc("Bacon")
            .build(),
        QuickAccess.builder()
            .setStyle(BUTTON_ACCENT)
            .setRippleColor(BUTTON_ACCENT_RIPPLE)
            .setProductId("14")
            .setShortDesc("Pork Sausage")
            .build());
  }
}
