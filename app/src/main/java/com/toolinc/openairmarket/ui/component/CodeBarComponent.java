package com.toolinc.openairmarket.ui.component;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.google.firebase.firestore.DocumentSnapshot;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.persistence.cloud.ProductMeasureUnitRepository;
import com.toolinc.openairmarket.persistence.cloud.ProductsRepository;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.pos.persistence.model.product.ProductMeasureUnit;
import com.toolinc.openairmarket.ui.fragment.ReceiptFragmentStatePagerAdapter;
import java.util.Map;
import timber.log.Timber;

@AutoValue
public abstract class CodeBarComponent {

  private static final String TAG = CodeBarComponent.class.getSimpleName();

  private final MutableLiveData<Map<String, ProductMeasureUnit>> productUnits = new MutableLiveData<>();

  abstract ProductsRepository productsRepository();

  abstract ProductMeasureUnitRepository productMeasureUnitRepository();

  abstract ReceiptFragmentStatePagerAdapter receiptFragmentStatePagerAdapter();

  abstract TextInputEditText textInputEditText();

  abstract ProgressBar progressBar();

  abstract FloatingActionButton floatingActionButton();

  public static Builder builder() {
    return new AutoValue_CodeBarComponent.Builder();
  }

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

  /** Adds a new product to the receipt. */
  public void addProduct(Product product) {
    // If the product is found on the map, it means the product must be sold using a measurement
    // instrument such as a scale.
    if (productUnits.getValue().containsKey(product.getProductMeasureUnit())) {
      ProductMeasureUnit productUnit = productUnits.getValue().get(product.getProductMeasureUnit());
      Timber.tag(TAG).i("Product Unit is %s", productUnit.getName());
    }
    receiptFragmentStatePagerAdapter().addProduct(product);
  }

  public void onSuccess(Product product) {
    hideProgressBar();
    addProduct(product);
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

    public abstract Builder setProductMeasureUnitRepository(
        ProductMeasureUnitRepository productMeasureUnitRepository);

    public abstract Builder setReceiptFragmentStatePagerAdapter(
        ReceiptFragmentStatePagerAdapter receiptFragmentStatePagerAdapter);

    public abstract Builder setTextInputEditText(TextInputEditText textInputEditText);

    public abstract Builder setProgressBar(ProgressBar progressBar);

    public abstract Builder setFloatingActionButton(FloatingActionButton floatingActionButton);

    abstract CodeBarComponent autoBuild();

    @UiThread
    public CodeBarComponent build() {
      CodeBarComponent codeBarComponent = autoBuild();
      codeBarComponent.productMeasureUnitRepository().findAllUncountable(productMeasureUnits -> {
        codeBarComponent.productUnits.postValue(productMeasureUnits);
      }, e -> {
        Timber.tag(TAG).e(e, "Unable to retrieve the measure units from firestore.");
      });
      codeBarComponent.floatingActionButton().setOnClickListener(codeBarComponent::onClick);
      codeBarComponent.textInputEditText().setOnKeyListener(codeBarComponent::onKey);
      return codeBarComponent;
    }
  }
}
