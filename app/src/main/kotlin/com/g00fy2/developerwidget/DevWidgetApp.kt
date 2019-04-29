package com.g00fy2.developerwidget

import com.g00fy2.developerwidget.controllers.DayNightController
import com.g00fy2.developerwidget.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

class DevWidgetApp : DaggerApplication() {

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.factory().create(this)

  @Inject
  lateinit var dayNightController: DayNightController

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) Timber.plant(DebugTree())

    dayNightController.loadCustomDefaultMode()
  }
}