package de.g00fy2.developerwidget.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.g00fy2.developerwidget.activities.widget.WidgetProvider

@Module
abstract class BroadcastBindingModule {

  @ContributesAndroidInjector
  abstract fun bindWidgetProvider(): WidgetProvider

}