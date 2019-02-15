package de.g00fy2.developerwidget

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import de.g00fy2.developerwidget.di.DaggerAppComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

class DevWidgetApp : DaggerApplication() {

  private val appComponent: AndroidInjector<DevWidgetApp> by lazy {
    DaggerAppComponent.builder().create(this)
  }

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> = appComponent

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) Timber.plant(DebugTree())
  }
}