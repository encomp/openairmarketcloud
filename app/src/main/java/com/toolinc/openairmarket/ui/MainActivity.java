package com.toolinc.openairmarket.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.toolinc.openairmarket.R;
import com.toolinc.openairmarket.viewmodel.ReceiptsViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/** Main activity. */
@AndroidEntryPoint
public final class MainActivity extends AppCompatActivity {

  @Inject ViewModelProvider.Factory viewModelFactory;
  private ReceiptsViewModel receiptsViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    receiptsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ReceiptsViewModel.class);
  }
}
