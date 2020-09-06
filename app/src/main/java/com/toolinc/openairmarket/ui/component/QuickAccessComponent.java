package com.toolinc.openairmarket.ui.component;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.annotation.UiThread;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.persistence.cloud.QuickAccessProductRepository;
import com.toolinc.openairmarket.ui.adapter.QuickAccessProductListAdapter;
import com.toolinc.openairmarket.ui.adapter.QuickAccessProductListAdapter.OnClick;
import com.toolinc.openairmarket.viewmodel.QuickAccessProductViewModel;
import com.toolinc.openairmarket.viewmodel.ReceiptsViewModel;

/**
 * Takes cares of the list of quick access products.
 */
@AutoValue
public abstract class QuickAccessComponent {

  public static Builder builder() {
    return new AutoValue_QuickAccessComponent.Builder();
  }

  abstract Context context();

  abstract LifecycleOwner lifecycleOwner();

  abstract ReceiptsViewModel receiptsViewModel();

  abstract QuickAccessProductRepository quickAccessProductRepository();

  abstract LottieAnimationView lottieAnimationView();

  abstract RecyclerView recyclerView();

  abstract OnClick onClickQuickAccess();

  void showQuickAccessProgressBar() {
    lottieAnimationView().setVisibility(View.VISIBLE);
    recyclerView().setVisibility(View.GONE);
  }

  void hideQuickAccessProgressBar() {
    lottieAnimationView().setVisibility(View.GONE);
    recyclerView().setVisibility(View.VISIBLE);
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder setContext(Context context);

    public abstract Builder setLifecycleOwner(LifecycleOwner lifecycleOwner);

    public abstract Builder setReceiptsViewModel(ReceiptsViewModel receiptsViewModel);

    public abstract Builder setQuickAccessProductRepository(
        QuickAccessProductRepository quickAccessProductRepository);

    public abstract Builder setLottieAnimationView(LottieAnimationView lottieAnimationView);

    public abstract Builder setRecyclerView(RecyclerView recyclerView);

    public abstract Builder setOnClickQuickAccess(OnClick onClickQuickAccess);

    abstract QuickAccessComponent autoBuild();

    @UiThread
    public QuickAccessComponent build() {
      QuickAccessComponent quickAccessComponent = autoBuild();
      GridLayoutManager gridLayoutManager = new GridLayoutManager(quickAccessComponent.context(),
          2);
      quickAccessComponent.recyclerView().setLayoutManager(gridLayoutManager);
      quickAccessComponent.receiptsViewModel().getQuickAccessProducts()
          .observe(quickAccessComponent.lifecycleOwner(), (observer) -> {
            QuickAccessProductListAdapter adapter =
                new QuickAccessProductListAdapter(
                    observer.asList(),
                    quickAccessComponent.onClickQuickAccess());
            quickAccessComponent.recyclerView().setAdapter(adapter);
            quickAccessComponent.hideQuickAccessProgressBar();
          });
      quickAccessComponent.quickAccessProductRepository().getAll(quickAccessProducts -> {
        ImmutableList<QuickAccessProductViewModel> quickAccessProductViewModels
            = QuickAccessProductViewModel
            .quickAccessesButtons(quickAccessComponent.context(), quickAccessProducts);
        quickAccessComponent.receiptsViewModel().getQuickAccessProducts()
            .postValue(quickAccessProductViewModels);
      }, e -> {
        new Handler(Looper.getMainLooper())
            .post(
                () -> {
                  quickAccessComponent.hideQuickAccessProgressBar();
                });
      });
      quickAccessComponent.showQuickAccessProgressBar();
      return quickAccessComponent;
    }
  }
}
