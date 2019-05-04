package com.toolinc.openairmarket.ui.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel;

/** Implementation of {@link PagerAdapter} that renders the content of three tickets. */
public final class ReceiptFragmentStatePagerAdapter extends FragmentStatePagerAdapter
    implements TabLayout.OnTabSelectedListener {

  private final ViewPager viewPager;
  private final TabLayout tabLayout;
  private final ImmutableList<ReceiptFragment> receiptFragments =
      ImmutableList.of(new ReceiptFragment(), new ReceiptFragment(), new ReceiptFragment());

  public ReceiptFragmentStatePagerAdapter(
      FragmentManager fragmentManager, ViewPager viewPager, TabLayout tabLayout) {
    super(fragmentManager);
    this.viewPager = Preconditions.checkNotNull(viewPager, "ViewPager is missing.");
    this.tabLayout = Preconditions.checkNotNull(tabLayout, "TabLayout is missing.");
    viewPager.setAdapter(this);
    tabLayout.addOnTabSelectedListener(this);
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
      case 1:
      case 2:
        return receiptFragments.get(position);

      default:
        return null;
    }
  }

  @Override
  public void onTabSelected(TabLayout.Tab tab) {
    viewPager.setCurrentItem(tab.getPosition());
  }

  @Override
  public void onTabUnselected(TabLayout.Tab tab) {}

  @Override
  public void onTabReselected(TabLayout.Tab tab) {}

  @Override
  public int getCount() {
    return 3;
  }

  public void addProduct(Product product) {
    ReceiptFragment receiptFragment = receiptFragments.get(tabLayout.getSelectedTabPosition());
    ReceiptViewModel receiptViewModel = receiptFragment.getReceiptViewModel();
    receiptViewModel.add(product);
  }
}
