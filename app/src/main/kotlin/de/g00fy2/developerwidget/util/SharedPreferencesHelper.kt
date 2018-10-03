package de.g00fy2.developerwidget.util

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) {

  private var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

  fun getString(key: String): String? {
    return sharedPreferences.getString(key, null)
  }

  fun putString(key: String, value: String?) {
    return sharedPreferences.edit().putString(key, value).apply()
  }

  fun putString(widgetId: Int, key: String, value: String?) {
    return sharedPreferences.edit().putString(widgetId.toString() + "_" + key, value).apply()
  }

  fun getString(widgetId: Int, key: String): String? {
    return sharedPreferences.getString(widgetId.toString() + "_" + key, null)
  }

  companion object {
    const val GITHUB_AUTHOR_IMAGE_URL = "cache_author_image_url"
    const val GITHUB_PROJECT_DESC = "cache_project_desc"
  }

}