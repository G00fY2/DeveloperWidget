package de.g00fy2.developerwidget.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.g00fy2.developerwidget.activities.about.AboutActivity
import de.g00fy2.developerwidget.activities.about.AboutActivityModule
import de.g00fy2.developerwidget.activities.apkinstall.ApkActivity
import de.g00fy2.developerwidget.activities.apkinstall.ApkActivityModule
import de.g00fy2.developerwidget.activities.appsettings.AppsActivity
import de.g00fy2.developerwidget.activities.appsettings.AppsActivityModule
import de.g00fy2.developerwidget.activities.widget.WidgetConfigActivity
import de.g00fy2.developerwidget.activities.widget.WidgetConfigActivityModule
import de.g00fy2.developerwidget.di.annotations.ActivityScope

@Module
abstract class ActivityBindingModule {

  @ActivityScope
  @ContributesAndroidInjector(modules = [AboutActivityModule::class])
  abstract fun bindAboutInjector(): AboutActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [ApkActivityModule::class])
  abstract fun bindApkInjector(): ApkActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [AppsActivityModule::class])
  abstract fun bindAppsInjector(): AppsActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [WidgetConfigActivityModule::class])
  abstract fun bindWidgetConfigInjector(): WidgetConfigActivity
}