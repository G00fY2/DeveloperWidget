package com.g00fy2.developerwidget.controllers

import android.content.Context
import androidx.core.content.edit
import com.g00fy2.developerwidget.utils.ACTIVITY
import com.g00fy2.developerwidget.utils.WIDGET_ID
import javax.inject.Inject
import javax.inject.Named

class WidgetPreferenceControllerImpl @Inject constructor() : WidgetPreferenceController {

  @Inject
  @field:Named(ACTIVITY)
  lateinit var context: Context
  @Inject
  @field:Named(WIDGET_ID)
  lateinit var widgetId: String

  private val sharedPreference by lazy {
    context.getSharedPreferences(context.packageName + ".preferences_" + widgetId, Context.MODE_PRIVATE)
  }

  override fun getAppFilters(): MutableList<String> {
    return (sharedPreference.getString(FILTERS, "") ?: "")
      .split(DELIMITER)
      .filterNot { it.isEmpty() }
      .distinct()
      .toMutableList()
  }

  override fun saveAppFilters(filters: List<String>) =
    sharedPreference.edit { putString(FILTERS, filters.distinct().joinToString(DELIMITER)) }

  override fun saveCustomDeviceName(deviceName: String) =
    sharedPreference.edit { putString(CUSTOM_DEVICE_NAME, deviceName.trim()) }

  override fun getCustomDeviceName() = (sharedPreference.getString(CUSTOM_DEVICE_NAME, "") ?: "")

  companion object {
    const val CUSTOM_DEVICE_NAME = "CUSTOM_DEVICE_NAME"
    const val FILTERS = "APP_FILTERS"
    const val DELIMITER = ","
  }
}