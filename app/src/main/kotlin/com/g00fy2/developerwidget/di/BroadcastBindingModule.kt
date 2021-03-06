package com.g00fy2.developerwidget.di

import com.g00fy2.developerwidget.receiver.widget.WidgetProviderImpl
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastBindingModule {

  @ContributesAndroidInjector
  abstract fun bindWidgetProvider(): WidgetProviderImpl
}