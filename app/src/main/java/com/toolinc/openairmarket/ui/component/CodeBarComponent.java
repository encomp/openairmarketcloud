package com.toolinc.openairmarket.ui.component;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.google.firebase.firestore.DocumentSnapshot;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.persistence.cloud.ProductsRepository;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.ui.fragment.ReceiptFragmentStatePagerAdapter;

import timber.log.Timber;

@AutoValue
public abstract class CodeBarComponent {

  private static final String TAG = CodeBarComponent.class.getSimpleName();

  public static Builder builder() {
    return new AutoValue_CodeBarComponent.Builder();
  }

  abstract ProductsRepository productsRepository();

  abstract ReceiptFragmentStatePagerAdapter receiptFragmentStatePagerAdapter();

  abstract TextInputEditText textInputEditText();

  abstract ProgressBar progressBar();

  abstract FloatingActionButton floatingActionButton();

  private boolean onKey(View view, int keyCode, KeyEvent event) {
    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
      String productId = textInputEditText().getText().toString();
      textInputEditText().getText().clear();
      if (!Strings.isNullOrEmpty(productId)) {
        showProgressBar();
        productsRepository()
            .findProductById(productId, this::onSuccess, this::onFailure, this::onComplete);
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
      showProgressBar();
      productsRepository()
          .findProductById(productId, this::onSuccess, this::onFailure, this::onComplete);
    }
  }

  public void onSuccess(Product product) {
    hideProgressBar();
    receiptFragmentStatePagerAdapter().addProduct(product);
  }

  public void onFailure(@NonNull Exception e) {
    hideProgressBar();
    Timber.tag(TAG).e(e);
  }

  public void onComplete(@NonNull Task<DocumentSnapshot> documentSnapshot) {
    if (!documentSnapshot.getResult().exists()) {
      hideProgressBar();
      Snackbar.make(
              textInputEditText().getRootView(),
              R.string.receipt_snack_bar_msg,
              BaseTransientBottomBar.LENGTH_LONG)
          .setAnchorView(textInputEditText())
          .show();
    }
  }

  private void showProgressBar() {
    new Handler(Looper.getMainLooper())
        .post(
            () -> {
              progressBar().setVisibility(View.VISIBLE);
            });
  }

  private void hideProgressBar() {
    new Handler(Looper.getMainLooper())
        .post(
            () -> {
              progressBar().setVisibility(View.GONE);
            });
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setProductsRepository(ProductsRepository productsRepository);

    public abstract Builder setReceiptFragmentStatePagerAdapter(
        ReceiptFragmentStatePagerAdapter receiptFragmentStatePagerAdapter);

    public abstract Builder setTextInputEditText(TextInputEditText textInputEditText);

    public abstract Builder setProgressBar(ProgressBar progressBar);

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
