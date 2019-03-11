package de.g00fy2.developerwidget.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.g00fy2.developerwidget.receiver.widget.WidgetProviderImpl
import de.g00fy2.developerwidget.receiver.widget.WidgetProviderModule

@Module
abstract class BroadcastBindingModule {

  @ContributesAndroidInjector(modules = [WidgetProviderModule::class])
  abstract fun bindWidgetProvider(): WidgetProviderImpl

}