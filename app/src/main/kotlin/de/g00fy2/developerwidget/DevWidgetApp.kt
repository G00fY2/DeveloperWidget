package de.g00fy2.developerwidget

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class DevWidgetApp : Application() {

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) Timber.plant(DebugTree())
  }
}