package com.toolinc.openairmarket.viewmodel.inject;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.toolinc.openairmarket.viewmodel.ReceiptsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/** Specifies the view model factory to inject view models. */
@Module
public abstract class ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(ReceiptsViewModel.class)
  abstract ViewModel bindReceiptsViewModel(ReceiptsViewModel userViewModel);

  @Binds
  abstract ViewModelProvider.Factory bindViewModelFactory(DaggerViewModelFactory factory);
}
