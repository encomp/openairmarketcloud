package com.toolinc.openairmarket.ui.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.ImmutableList;
import com.toolinc.openairmarket.pos.persistence.model.product.Product;
import com.toolinc.openairmarket.viewmodel.ReceiptViewModel;

/** Implementation of {@link PagerAdapter} that renders the content of three tickets. */
public final class ReceiptFragmentStatePagerAdapter extends FragmentStatePagerAdapter
    implements OnSuccessListener<Product> {

  private final ImmutableList<ReceiptFragment> receiptFragments =
      ImmutableList.of(new ReceiptFragment(), new ReceiptFragment(), new ReceiptFragment());
  private int position;

  public ReceiptFragmentStatePagerAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
      case 1:
      case 2:
        this.position = position;
        return receiptFragments.get(position);

      default:
        this.position = 0;
        return null;
    }
  }

  @Override
  public int getCount() {
    return 3;
  }

  @Override
  public void onSuccess(Product product) {
      ReceiptFragment receiptFragment = receiptFragments.get(position);
      ReceiptViewModel receiptViewModel = receiptFragment.getReceiptViewModel();
      receiptViewModel.add(product);
  }
}
