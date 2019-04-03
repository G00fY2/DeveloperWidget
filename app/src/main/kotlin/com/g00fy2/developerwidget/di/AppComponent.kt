package com.g00fy2.developerwidget.di

import com.g00fy2.developerwidget.DevWidgetApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBindingModule::class, BroadcastBindingModule::class, DeviceDataModule::class])
interface AppComponent : AndroidInjector<DevWidgetApp> {

  @Component.Factory
  interface Factory : AndroidInjector.Factory<DevWidgetApp>
}