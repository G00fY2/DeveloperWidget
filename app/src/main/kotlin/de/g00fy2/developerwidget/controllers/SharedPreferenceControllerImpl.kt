package de.g00fy2.developerwidget.controllers

import android.content.Context
import de.g00fy2.developerwidget.utils.ACTIVITY
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class SharedPreferenceControllerImpl @Inject constructor() : SharedPreferenceController {

  @Inject
  @field:Named(ACTIVITY)
  lateinit var context: Context

  override fun test(widgetId: Int) {
    Timber.d("test " + widgetId + " " + context.packageName)
  }

}