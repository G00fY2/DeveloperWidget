package de.g00fy2.developerwidget.controllers

import android.content.Context
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class SharedPreferenceControllerImpl @Inject constructor() : SharedPreferenceController {

  @Inject
  @field:Named("activity")
  lateinit var context: Context

  override fun test(widgetId: Int) {
    Timber.d("test " + widgetId + " " + context.packageName)
  }

}