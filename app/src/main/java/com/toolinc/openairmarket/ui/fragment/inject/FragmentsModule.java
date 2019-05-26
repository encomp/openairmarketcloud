package com.toolinc.openairmarket.ui.fragment.inject;

import androidx.fragment.app.FragmentManager;

import com.toolinc.openairmarket.ui.MainActivity;
import com.toolinc.openairmarket.viewmodel.inject.ViewModelModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ViewModelModule.class})
public class FragmentsModule {

  @Provides
  FragmentManager providesFragmentManager(MainActivity activity) {
    return activity.getSupportFragmentManager();
  }
}
