package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.work.WorkManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.toolinc.openairmarket.OpenAirMarketApplication;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.work.FirestoreSync;
import dagger.hilt.android.AndroidEntryPoint;
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
    OpenAirMarketApplication.setWorkerFactory(workerFactory);
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
    WorkManager.getInstance(getActivity()).enqueue(FirestoreSync.syncCatalogData());
  }
}
