package de.g00fy2.developerwidget.utils

import android.content.Context
import androidx.core.content.edit

class SharedPreferencesHelper(private val context: Context, private val widgetId: Int) {

  companion object {
    const val FILTERS = "APP_FILTERS"
    const val DELIMITER = ","
  }

  private val sharedPreference by lazy {
    context.getSharedPreferences(
      context.packageName + ".preferences_" + widgetId,
      Context.MODE_PRIVATE
    )
  }

  fun saveFilters(filters: List<String>) =
    sharedPreference.edit { putString(FILTERS, filters.distinct().joinToString(DELIMITER)) }

  fun getFilters(): MutableList<String> {
    return (sharedPreference.getString(FILTERS, "") ?: "")
      .split(DELIMITER)
      .filterNot { it.isEmpty() }
      .distinct()
      .toMutableList()
  }
}