package com.toolinc.openairmarket.ui.component;

import android.view.KeyEvent;
import android.view.View;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.toolinc.openairmarket.persistence.cloud.ProductsRepository;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.ui.fragment.ReceiptFragmentStatePagerAdapter;
import timber.log.Timber;

@AutoValue
public abstract class CodeBarComponent {

  private static final String TAG = CodeBarComponent.class.getSimpleName();

  abstract ProductsRepository productsRepository();

  abstract ReceiptFragmentStatePagerAdapter receiptFragmentStatePagerAdapter();

  abstract TextInputEditText textInputEditText();

  abstract FloatingActionButton floatingActionButton();

  private boolean onKey(View view, int keyCode, KeyEvent event) {
    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
      String productId = textInputEditText().getText().toString();
      textInputEditText().getText().clear();
      if (!Strings.isNullOrEmpty(productId)) {
        productsRepository().findProductById(productId, this::onSuccess, this::onFailure);
        Timber.tag(TAG).d("Searching for Product: [%s].", productId);
      }
      return true;
    }
    return false;
  }

  private void onClick(View view) {
    String productId = textInputEditText().getText().toString();
    textInputEditText().getText().clear();
    if (!Strings.isNullOrEmpty(productId)) {
      productsRepository().findProductById(productId, this::onSuccess, this::onFailure);
    }
  }

  public void onSuccess(Product product) {
    receiptFragmentStatePagerAdapter().addProduct(product);
  }

  public void onFailure(@NonNull Exception e) {
    Timber.tag(TAG).e(e);
  }

  public static Builder builder() {
    return new AutoValue_CodeBarComponent.Builder();
  }

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Builder setProductsRepository(ProductsRepository productsRepository);

    public abstract Builder setReceiptFragmentStatePagerAdapter(
        ReceiptFragmentStatePagerAdapter receiptFragmentStatePagerAdapter);

    public abstract Builder setTextInputEditText(TextInputEditText textInputEditText);

    public abstract Builder setFloatingActionButton(FloatingActionButton floatingActionButton);

    abstract CodeBarComponent autoBuild();

    public CodeBarComponent build() {
      CodeBarComponent codeBarComponent = autoBuild();
      codeBarComponent.floatingActionButton().setOnClickListener(codeBarComponent::onClick);
      codeBarComponent.textInputEditText().setOnKeyListener(codeBarComponent::onKey);
      return codeBarComponent;
    }
  }
}
