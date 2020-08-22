package com.toolinc.openairmarket.ui.fragment;

import android.content.res.Configuration;
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
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.toolinc.openairmarket.OpenAirMarketApplication;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.fragment.MainFragmentDirections.ActionLogout;
import com.toolinc.openairmarket.work.ProductCategorySyncWorker;
import com.toolinc.openairmarket.work.ProductSyncWorker;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
/** Initial screen for the pos app. */
public class MainFragment extends Fragment
    implements NavigationView.OnNavigationItemSelectedListener {

  DrawerLayout drawer;

  @BindView(R.id.bottom_app_bar)
  BottomAppBar bottomAppBar;

  @Nullable
  @BindView(R.id.fab_add_to_receipt)
  FloatingActionButton floatingActionButton;

  @Inject FirebaseUser currentUser;

  @Inject HiltWorkerFactory workerFactory;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: workaround to install the HiltWorkerFactory
    OpenAirMarketApplication.setWorkerFactory(workerFactory, getContext());
    // END
    syncFromFirestore();
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {

    View view = layoutInflater.inflate(R.layout.fragment_main, viewGroup, false /* attachToRoot */);
    ButterKnife.bind(this, view);

    AppCompatActivity activity = (AppCompatActivity) getActivity();
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

    // TODO (edgar): Consider this created a nav graph.
    ReceiptsFragment receiptsFragment = new ReceiptsFragment();
    getChildFragmentManager()
        .beginTransaction()
        .add(R.id.full_screen_fragment_container, receiptsFragment)
        .commit();
    if (isLandscape()) {
      SearchBoxFragment searchBoxFragment = new SearchBoxFragment();
      getChildFragmentManager()
          .beginTransaction()
          .add(R.id.left_side_fragment_container, searchBoxFragment)
          .commit();
    }
    return view;
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // Handle navigation view item clicks here.
    switch (item.getItemId()) {
      case R.id.nav_log_out:
        ActionLogout actionLogout = MainFragmentDirections.actionLogout();
        actionLogout = actionLogout.setLogout(Boolean.TRUE);
        NavHostFragment.findNavController(this).navigate(actionLogout);
        break;

      case R.id.nav_offline:
        NavDirections navDirections = MainFragmentDirections.actionOffline();
        NavHostFragment.findNavController(this).navigate(navDirections.getActionId());
        break;
    }
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  private void syncFromFirestore() {
    WorkManager.getInstance(getActivity())
        .enqueue(
            new OneTimeWorkRequest.Builder(ProductCategorySyncWorker.class)
                .setInitialDelay(2000, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS)
                .build());

    WorkManager.getInstance(getActivity())
        .enqueue(
            new OneTimeWorkRequest.Builder(ProductSyncWorker.class)
                .setInitialDelay(2000, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS)
                .build());
  }

  private boolean isLandscape() {
    return Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation;
  }
}
