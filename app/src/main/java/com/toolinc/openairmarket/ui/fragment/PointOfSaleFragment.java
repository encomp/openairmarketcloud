package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.fragment.base.DrawerMenuFragment;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
/** Initial screen for the pos app. */
public class PointOfSaleFragment extends DrawerMenuFragment {

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    // bind common components
    bindComponents();
    View view = layoutInflater.inflate(R.layout.fragment_pos, viewGroup, false /* attachToRoot */);
    // Add left side panel
    if (isLandscape()) {
      SearchBoxFragment searchBoxFragment = new SearchBoxFragment();
      getChildFragmentManager()
          .beginTransaction()
          .replace(R.id.left_side_fragment_container, searchBoxFragment)
          .commit();
    }
    ReceiptsFragment receiptsFragment = new ReceiptsFragment();
    getChildFragmentManager()
        .beginTransaction()
        .add(R.id.full_screen_fragment_container, receiptsFragment)
        .commit();
    return view;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_drawer_fragment);
    return NavigationUI.onNavDestinationSelected(item, navController)
        || super.onOptionsItemSelected(item);
  }
}
