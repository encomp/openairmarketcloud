package com.toolinc.openairmarket.viewmodel.inject;

import androidx.lifecycle.ViewModel;

import com.toolinc.openairmarket.viewmodel.ReceiptViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.Binds;
import dagger.MapKey;
import dagger.Module;
import dagger.multibindings.IntoMap;

/** Specifies the view model factory to inject view models. */
@Module
public abstract class ViewModelModule {

  @Target({ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @MapKey
  @interface ViewModelKey {
    Class<? extends ViewModel> value();
  }

  @Binds
  @IntoMap
  @ViewModelKey(ReceiptViewModel.class)
  abstract ViewModel bindUserViewModel(ReceiptViewModel userViewModel);

  abstract DaggerViewModelFactory viewModelFactory();
}
