package com.toolinc.openairmarket.ui.fragment;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel;
import com.toolinc.openairmarket.viewmodel.ReceiptsViewModel;

import timber.log.Timber;

/** Implementation of {@link PagerAdapter} that renders the content of three tickets. */
public final class ReceiptFragmentStatePagerAdapter extends FragmentStatePagerAdapter
    implements ViewPager.OnPageChangeListener {

  private static final String TAG = ReceiptFragmentStatePagerAdapter.class.getSimpleName();
  private final TabLayout tabLayout;
  private final ImmutableList<FragmentReceiptViewModel> fragmentWithViewModels;
  private final ImmutableList<String> titles;

  public ReceiptFragmentStatePagerAdapter(
      FragmentManager fragmentManager,
      ReceiptsViewModel receiptsViewModel,
      Context context,
      ViewPager viewPager,
      TabLayout tabLayout) {
    super(fragmentManager);
    Preconditions.checkNotNull(receiptsViewModel, "ReceiptsViewModel is missing.");
    Preconditions.checkNotNull(viewPager, "ViewPager is missing.");
    Preconditions.checkNotNull(context, "Context is missing.");
    this.tabLayout = Preconditions.checkNotNull(tabLayout, "TabLayout is missing.");
    fragmentWithViewModels = initFragmentWithViewModel(receiptsViewModel);
    titles = initTitles(context);
    bindViewPagerWithTabLayout(viewPager);
  }

  @Override
  public Fragment getItem(int position) {
    return fragmentWithViewModels.get(position).receiptFragment();
  }

  @Override
  public int getCount() {
    return fragmentWithViewModels.size();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return titles.get(position);
  }

  @Override
  public void onPageSelected(int position) {
    Timber.tag(TAG).d("Append observers " + position);
    fragmentWithViewModels.get(position).bindObservers();
  }

  public void addProduct(Product product) {
    Timber.tag(TAG).d("Current Tab Position: " + tabLayout.getSelectedTabPosition());
    Timber.tag(TAG).d("Append product: " + product.id());
    getReceiptViewModel().add(product);
  }

  public void removeAllProducts() {
    getReceiptViewModel().removeAllProducts();
  }

  private ReceiptViewModel getReceiptViewModel() {
    FragmentReceiptViewModel fragmentReceiptViewModel =
        fragmentWithViewModels.get(tabLayout.getSelectedTabPosition());
    return fragmentReceiptViewModel.receiptViewModel();
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

  @Override
  public void onPageScrollStateChanged(int state) {}

  private void bindViewPagerWithTabLayout(ViewPager viewPager) {
    viewPager.addOnPageChangeListener(this);
    viewPager.setAdapter(this);
    tabLayout.setupWithViewPager(viewPager);
    for (int i = 0; i < tabLayout.getTabCount(); i++) {
      tabLayout.getTabAt(i).setIcon(R.drawable.ic_receipt);
    }
  }

  private ImmutableList<String> initTitles(Context context) {
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    builder.add(context.getString(R.string.receipt_tab_1));
    builder.add(context.getString(R.string.receipt_tab_2));
    builder.add(context.getString(R.string.receipt_tab_3));
    return builder.build();
  }

  private ImmutableList<FragmentReceiptViewModel> initFragmentWithViewModel(
      ReceiptsViewModel receiptsViewModel) {
    ImmutableList.Builder<FragmentReceiptViewModel> builder = ImmutableList.builder();
    for (ReceiptViewModel receiptViewModel : receiptsViewModel.getReceiptViewModels()) {
      builder.add(FragmentReceiptViewModel.create(new ReceiptFragment(), receiptViewModel));
    }
    return builder.build();
  }

  @AutoValue
  abstract static class FragmentReceiptViewModel {

    abstract ReceiptFragment receiptFragment();

    abstract ReceiptViewModel receiptViewModel();

    void bindObservers() {
      receiptViewModel().getLines().observe(receiptFragment(), receiptFragment()::newProductLines);
      receiptViewModel()
          .getAmountDue()
          .observe(receiptFragment(), receiptFragment()::newTotalAmount);
    }

    static FragmentReceiptViewModel create(
        ReceiptFragment receiptFragment, ReceiptViewModel receiptViewModel) {
      FragmentReceiptViewModel fragmentReceiptViewModel =
          new AutoValue_ReceiptFragmentStatePagerAdapter_FragmentReceiptViewModel(
              receiptFragment, receiptViewModel);
      fragmentReceiptViewModel.bindObservers();
      return fragmentReceiptViewModel;
    }
  }
}
