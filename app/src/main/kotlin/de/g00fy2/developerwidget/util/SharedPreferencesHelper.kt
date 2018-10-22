package de.g00fy2.developerwidget.util

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) {

  private var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

  fun getString(key: String): String? = sharedPreferences.getString(key, null)

  fun putString(key: String, value: String?) = sharedPreferences.edit().putString(key, value).apply()

  fun putWidgetString(widgetId: Int, key: String, value: String?) = putString(widgetId.toString() + "_" + key, value)

  fun getWidgetString(widgetId: Int, key: String) = getString(widgetId.toString() + "_" + key)
}