package com.g00fy2.developerwidget.di

import com.g00fy2.developerwidget.activities.about.AboutActivity
import com.g00fy2.developerwidget.activities.about.AboutActivityModule
import com.g00fy2.developerwidget.activities.apkinstall.ApkActivity
import com.g00fy2.developerwidget.activities.apkinstall.ApkActivityModule
import com.g00fy2.developerwidget.activities.appmanager.AppsActivity
import com.g00fy2.developerwidget.activities.appmanager.AppsActivityModule
import com.g00fy2.developerwidget.activities.widgetconfig.WidgetConfigActivity
import com.g00fy2.developerwidget.activities.widgetconfig.WidgetConfigActivityModule
import com.g00fy2.developerwidget.di.annotations.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

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