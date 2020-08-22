package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.toolinc.openairmarket.OpenAirMarketApplication;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.work.ProductCategorySyncWorker;
import com.toolinc.openairmarket.work.ProductSyncWorker;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@AndroidEntryPoint
/** Initial screen for the entire app. */
public class MainFragment extends Fragment {

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
    NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
    TextView tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);
    tvEmail.setText(currentUser.getEmail());
    return layoutInflater.inflate(R.layout.fragment_main, viewGroup, false /* attachToRoot */);
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
}
