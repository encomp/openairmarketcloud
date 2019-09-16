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
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.fragment.MainFragmentDirections.ActionLogout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/** Initial screen for the pos app. */
public class MainFragment extends DaggerFragment
    implements NavigationView.OnNavigationItemSelectedListener {

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
    navigationView.setNavigationItemSelectedListener(this);
    TextView tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);
    tvEmail.setText(currentUser.getEmail());

    // TODO (edgar): Refactor this to extends from FullScreenFragment.
    ReceiptsFragment receiptsFragment = new ReceiptsFragment();
    getChildFragmentManager()
        .beginTransaction()
        .add(R.id.full_screen_fragment_container, receiptsFragment)
        .commit();
    return view;
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();
    if (id == R.id.nav_log_out) {
      ActionLogout actionLogout = MainFragmentDirections.actionLogout();
      actionLogout = actionLogout.setLogout(Boolean.TRUE);
      NavHostFragment.findNavController(this).navigate(actionLogout);
    }
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
