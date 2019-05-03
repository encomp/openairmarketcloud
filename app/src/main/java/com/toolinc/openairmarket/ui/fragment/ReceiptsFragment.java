package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.toolinc.openairmarket.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

public class ReceiptsFragment extends DaggerFragment {

  @Inject ViewModelProvider.Factory viewModelFactory;

  @BindView(R.id.tab_layout)
  TabLayout tabLayout;

  @BindView(R.id.view_pager)
  ViewPager viewPager;

  private ReceiptFragmentStatePagerAdapter receiptFragmentStatePagerAdapter;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view =
        layoutInflater.inflate(R.layout.fragment_receipts, viewGroup, false /* attachToRoot */);
    ButterKnife.bind(this, view);
    receiptFragmentStatePagerAdapter = new ReceiptFragmentStatePagerAdapter(getFragmentManager());
    viewPager.setAdapter(receiptFragmentStatePagerAdapter);
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    return view;
  }

  static final class ReceiptFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private final ReceiptFragment receiptFragment1 = new ReceiptFragment();
    private final ReceiptFragment receiptFragment2 = new ReceiptFragment();
    private final ReceiptFragment receiptFragment3 = new ReceiptFragment();

    public ReceiptFragmentStatePagerAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return receiptFragment1;

        case 1:
          return receiptFragment2;

        case 2:
          return receiptFragment3;

        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return 3;
    }
  }
}
