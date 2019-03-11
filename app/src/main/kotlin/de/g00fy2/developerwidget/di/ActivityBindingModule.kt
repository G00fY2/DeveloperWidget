package de.g00fy2.developerwidget.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.g00fy2.developerwidget.activities.about.AboutActivity
import de.g00fy2.developerwidget.activities.about.AboutActivityModule
import de.g00fy2.developerwidget.activities.apkinstall.ApkActivity
import de.g00fy2.developerwidget.activities.apkinstall.ApkActivityModule
import de.g00fy2.developerwidget.activities.appsettings.AppsActivity
import de.g00fy2.developerwidget.activities.appsettings.AppsActivityModule
import de.g00fy2.developerwidget.activities.widgetconfig.WidgetConfigActivity
import de.g00fy2.developerwidget.activities.widgetconfig.WidgetConfigActivityModule
import de.g00fy2.developerwidget.di.annotations.ActivityScope

@Module
abstract class ActivityBindingModule {

  @ActivityScope
  @ContributesAndroidInjector(modules = [AboutActivityModule::class, ControllerModule::class])
  abstract fun bindAboutInjector(): AboutActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [ApkActivityModule::class, ControllerModule::class])
  abstract fun bindApkInjector(): ApkActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [AppsActivityModule::class, ControllerModule::class])
  abstract fun bindAppsInjector(): AppsActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [WidgetConfigActivityModule::class, ControllerModule::class])
  abstract fun bindWidgetConfigInjector(): WidgetConfigActivity
}