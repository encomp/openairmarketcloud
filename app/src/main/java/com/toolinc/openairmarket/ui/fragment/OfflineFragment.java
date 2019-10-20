package com.toolinc.openairmarket.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.TableView;
import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.ui.view.tableview.adapter.TableViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/** Offline configuration fragment. */
public class OfflineFragment extends DaggerFragment {

  @BindView(R.id.offlineTableView)
  TableView tableView;

  @BindView(R.id.progressBar)
  ProgressBar progressBar;

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

  private void initializeTableView(TableView tableView){
    tableViewAdapter = new TableViewAdapter(getContext());
    tableView.setAdapter(tableViewAdapter);
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
