package com.g00fy2.developerwidget.di

import com.g00fy2.developerwidget.receiver.widget.WidgetProviderImpl
import com.g00fy2.developerwidget.receiver.widget.WidgetProviderModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastBindingModule {

  @ContributesAndroidInjector(modules = [WidgetProviderModule::class])
  abstract fun bindWidgetProvider(): WidgetProviderImpl

}