package com.toolinc.openairmarket.inject;

import com.toolinc.openairmarket.ui.MainActivity;
import com.toolinc.openairmarket.ui.fragment.LoginFragment;
import com.toolinc.openairmarket.ui.fragment.MainFragment;
import com.toolinc.openairmarket.ui.fragment.OfflineFragment;
import com.toolinc.openairmarket.ui.fragment.ReceiptFragment;
import com.toolinc.openairmarket.ui.fragment.ReceiptsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Specifies the object graph to inject different instances to activities and fragments of the
 * application.
 */
@Module
public abstract class OpenAirMarketUiModule {

  @ContributesAndroidInjector
  abstract MainActivity mainActivity();

  @ContributesAndroidInjector
  abstract MainFragment contributeMainFragment();

  @ContributesAndroidInjector
  abstract LoginFragment contributeLoginFragment();

  @ContributesAndroidInjector
  abstract ReceiptsFragment contributeReceiptsFragment();

  @ContributesAndroidInjector
  abstract ReceiptFragment contributeReceiptFragment();

  @ContributesAndroidInjector
  abstract OfflineFragment contributeOfflineFragment();
}
