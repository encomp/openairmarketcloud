package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.evrencoskun.tableview.TableView;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncState;
import com.toolinc.openairmarket.persistence.local.offline.CollectionSyncStateRepository;
import com.toolinc.openairmarket.ui.adapter.OfflineTableViewAdapter;
import com.toolinc.openairmarket.ui.view.tableview.TableViewListener;
import com.toolinc.openairmarket.ui.view.tableview.adapter.TableViewAdapter;
import com.toolinc.openairmarket.ui.view.tableview.TableViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.hilt.android.AndroidEntryPoint;

/** Offline configuration fragment. */
@AndroidEntryPoint
public class OfflineFragment extends Fragment {

  @BindView(R.id.offlineTableView)
  TableView tableView;

  @BindView(R.id.progressBar)
  ProgressBar progressBar;

  @Inject CollectionSyncStateRepository collectionSyncStateRepository;

  private LiveData<List<CollectionSyncState>> collectionSyncStates;
  private TableViewModel.Builder tableViewModel =
      TableViewModel.builder(new OfflineTableViewAdapter());
  private TableViewAdapter tableViewAdapter;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_offline, container, false);
    ButterKnife.bind(this, view);
    initializeTableView(tableView);
    return view;
  }

  private void initializeTableView(TableView tableView) {
    tableViewAdapter = new TableViewAdapter(getContext());
    tableView.setAdapter(tableViewAdapter);
    tableView.setTableViewListener(
        TableViewListener.builder(tableView)
            .withColumnHeaderPopup()
            .addColumnPosition(0)
            .addColumnPosition(1)
            .withRowHeaderPopup()
            .setOnCellClickListener(
                (cellView, row, column) -> {
                  Toast.makeText(
                          OfflineFragment.this.getContext(),
                          "" + row + ":" + column,
                          Toast.LENGTH_LONG)
                      .show();
                })
            .build());
    retrieveAllData();
  }

  private void retrieveAllData() {
    showProgressBar();
    collectionSyncStates = collectionSyncStateRepository.getAll();
    collectionSyncStates.observe(
        this,
        collectionSyncStates -> {
          TableViewModel tableViewModel =
              TableViewModel.builder(new OfflineTableViewAdapter())
                  .addAllCellModels(collectionSyncStates)
                  .addColumnHeaderModel(getString(R.string.offline_header_id))
                  .addColumnHeaderModel(getString(R.string.offline_header_number_docs))
                  .addColumnHeaderModel(getString(R.string.offline_header_status))
                  .addColumnHeaderModel(getString(R.string.offline_header_last_update))
                  .build();
          tableViewAdapter.setTableViewModel(tableViewModel);
          hideProgressBar();
        });
  }

  public void showProgressBar() {
    progressBar.setVisibility(View.VISIBLE);
    tableView.setVisibility(View.INVISIBLE);
  }

  public void hideProgressBar() {
    progressBar.setVisibility(View.INVISIBLE);
    tableView.setVisibility(View.VISIBLE);
  }
}
