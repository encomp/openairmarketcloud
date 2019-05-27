package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.toolinc.openairmarket.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/** Initial screen for the pos app. */
public class MainFragment extends DaggerFragment
    implements OnBackPressedHandler, NavigationView.OnNavigationItemSelectedListener {

  DrawerLayout drawer;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.bottom_app_bar)
  BottomAppBar bottomAppBar;

  @BindView(R.id.fab_add_to_receipt)
  FloatingActionButton floatingActionButton;

  @Inject FirebaseUser currentUser;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {

    View view = layoutInflater.inflate(R.layout.fragment_main, viewGroup, false /* attachToRoot */);
    ButterKnife.bind(this, view);

    AppCompatActivity activity = (AppCompatActivity) getActivity();
    toolbar.setTitle("POS");
    activity.setSupportActionBar(toolbar);
    drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(
            activity,
            drawer,
            bottomAppBar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = activity.findViewById(R.id.nav_view);
    TextView tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);
    tvEmail.setText(currentUser.getEmail());

    // TODO (edgar): Refactor this to extends from FullScreenFragment.
    ReceiptsFragment receiptsFragment = new ReceiptsFragment();
    getChildFragmentManager()
        .beginTransaction()
        .add(R.id.full_screen_fragment_container, receiptsFragment)
        .commit();

    navigationView.setNavigationItemSelectedListener(this);
    return view;
  }

  public boolean onBackPressed() {
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
      return true;
    }
    return false;
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();
    if (id == R.id.nav_log_out) {
      getActivity().finish();
    }
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
