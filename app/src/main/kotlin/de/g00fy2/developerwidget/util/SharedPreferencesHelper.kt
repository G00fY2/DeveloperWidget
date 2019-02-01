package de.g00fy2.developerwidget.util

import android.content.Context

class SharedPreferencesHelper(private val context: Context, private val widgetId: Int) {

  companion object {
    const val FILTERS = "APP_FILTERS"
  }

  private val sharedPreference by lazy {
    context.getSharedPreferences(
      context.packageName + ".preferences_" + widgetId,
      Context.MODE_PRIVATE
    )
  }

  fun saveFilters(filters: Set<String>) = sharedPreference.edit().putStringSet(FILTERS, filters).apply()

  fun getFilters(): MutableSet<String> = sharedPreference.getStringSet(FILTERS, mutableSetOf()) ?: mutableSetOf()
}