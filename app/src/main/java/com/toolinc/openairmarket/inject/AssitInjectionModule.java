package com.toolinc.openairmarket.inject;

import com.squareup.inject.assisted.dagger2.AssistedModule;

import dagger.Module;

@Module(includes = {AssistedInject_AssitInjectionModule.class})
@AssistedModule
/** Enables {@link com.squareup.inject.assisted.AssistedInject} on the app. */
public interface AssitInjectionModule {}
