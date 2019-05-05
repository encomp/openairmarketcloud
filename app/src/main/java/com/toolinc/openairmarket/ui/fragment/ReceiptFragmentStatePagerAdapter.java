package com.toolinc.openairmarket.ui.fragment;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel;

/** Implementation of {@link PagerAdapter} that renders the content of three tickets. */
public final class ReceiptFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

  @StringRes private static final int TAB_1 = R.string.receipt_tab_1;
  @StringRes private static final int TAB_2 = R.string.receipt_tab_2;
  @StringRes private static final int TAB_3 = R.string.receipt_tab_3;
  @DrawableRes private static final int ICON_TAB = R.drawable.ic_receipt;

  private final Context context;
  private final ViewPager viewPager;
  private final TabLayout tabLayout;
  private final ImmutableList<ReceiptFragment> receiptFragments =
      ImmutableList.of(new ReceiptFragment(), new ReceiptFragment(), new ReceiptFragment());

  public ReceiptFragmentStatePagerAdapter(
      FragmentManager fragmentManager, Context context, ViewPager viewPager, TabLayout tabLayout) {
    super(fragmentManager);
    this.context = Preconditions.checkNotNull(context, "Context is missing.");
    this.viewPager = Preconditions.checkNotNull(viewPager, "ViewPager is missing.");
    this.tabLayout = Preconditions.checkNotNull(tabLayout, "TabLayout is missing.");
    viewPager.setAdapter(this);
    tabLayout.setupWithViewPager(viewPager);
    for (int i = 0; i < tabLayout.getTabCount(); i++) {
      tabLayout.getTabAt(i).setIcon(ICON_TAB);
    }
  }

  @Override
  public Fragment getItem(int position) {
    return receiptFragments.get(position);
  }

  @Override
  public int getCount() {
    return receiptFragments.size();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return context.getString(TAB_1);

      case 1:
        return context.getString(TAB_2);

      case 2:
        return context.getString(TAB_3);

      default:
        break;
    }
    return "";
  }

  public void addProduct(Product product) {
    ReceiptFragment receiptFragment = receiptFragments.get(tabLayout.getSelectedTabPosition());
    ReceiptViewModel receiptViewModel = receiptFragment.getReceiptViewModel();
    receiptViewModel.add(product);
  }
}
