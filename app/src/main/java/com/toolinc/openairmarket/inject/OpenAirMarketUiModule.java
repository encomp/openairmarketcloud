package com.toolinc.openairmarket.inject;

import com.toolinc.openairmarket.ui.EmailPasswordActivity;
import com.toolinc.openairmarket.ui.MainActivity;
import com.toolinc.openairmarket.ui.fragment.MainFragment;
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
  abstract EmailPasswordActivity emailPasswordActivity();

  @ContributesAndroidInjector
  abstract MainActivity mainActivity();

  @ContributesAndroidInjector
  abstract MainFragment contributeMainFragment();

  @ContributesAndroidInjector
  abstract ReceiptsFragment contributeReceiptsFragment();

  @ContributesAndroidInjector
  abstract ReceiptFragment contributeReceiptFragment();
}
