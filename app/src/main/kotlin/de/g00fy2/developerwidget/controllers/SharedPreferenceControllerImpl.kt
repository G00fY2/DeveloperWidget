package de.g00fy2.developerwidget.controllers

import android.content.Context
import androidx.core.content.edit
import de.g00fy2.developerwidget.utils.ACTIVITY
import de.g00fy2.developerwidget.utils.WIDGET_ID
import javax.inject.Inject
import javax.inject.Named

class SharedPreferenceControllerImpl @Inject constructor() : SharedPreferenceController {

  @Inject @field:Named(ACTIVITY) lateinit var context: Context
  @Inject @field:Named(WIDGET_ID) lateinit var widgetId: String

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

  companion object {
    const val FILTERS = "APP_FILTERS"
    const val DELIMITER = ","
  }
}