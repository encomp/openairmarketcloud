package com.toolinc.openairmarket.ui.fragment.base;

import android.content.res.Configuration;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationView;
import com.toolinc.openairmarket.R;

/** Base fragment for the left side menu drawer. */
public abstract class DrawerMenuFragment extends Fragment {

  @Nullable private MaterialToolbar topAppBar;
  @Nullable private BottomAppBar bottomAppBar;
  @NonNull private Toolbar toolbar;
  @NonNull private DrawerLayout drawerLayout;
  @NonNull private NavigationView navigationView;
  @NonNull private NavController navController;

  protected void bindComponents() {
    navigationView = getActivity().findViewById(R.id.nav_view);
    navController = Navigation.findNavController(getActivity(), R.id.nav_drawer_fragment);
    NavigationUI.setupWithNavController(navigationView, navController);
    drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
    if (isLandscape()) {
      topAppBar = (MaterialToolbar) getActivity().findViewById(R.id.top_app_bar);
      toolbar = topAppBar;
    } else {
      bottomAppBar = (BottomAppBar) getActivity().findViewById(R.id.bottom_app_bar);
      toolbar = bottomAppBar;
    }
    AppBarConfiguration appBarConfiguration =
        new AppBarConfiguration.Builder(navController.getGraph())
            .setOpenableLayout(drawerLayout)
            .build();
    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
  }

  protected boolean isLandscape() {
    return Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation;
  }

  @Nullable
  public MaterialToolbar getTopAppBar() {
    return topAppBar;
  }

  @Nullable
  public BottomAppBar getBottomAppBar() {
    return bottomAppBar;
  }

  @NonNull
  public DrawerLayout getDrawerLayout() {
    return drawerLayout;
  }

  @NonNull
  public NavigationView getNavigationView() {
    return navigationView;
  }
}
