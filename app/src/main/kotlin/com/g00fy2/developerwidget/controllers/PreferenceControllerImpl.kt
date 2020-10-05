package com.g00fy2.developerwidget.controllers

import android.content.Context
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.annotations.ACTIVITY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class PreferenceControllerImpl @Inject constructor() : PreferenceController {

  @Inject
  @Named(ACTIVITY)
  lateinit var activity: BaseActivity

  private val sharedPreference by lazy {
    activity.getSharedPreferences(activity.packageName + ".preferences", Context.MODE_PRIVATE)
  }

  override suspend fun get(key: String, default: String): String {
    return withContext(Dispatchers.IO) {
      sharedPreference.getString(key, default) ?: default
    }
  }

  override suspend fun get(key: String, default: Long): Long {
    return withContext(Dispatchers.IO) {
      sharedPreference.getLong(key, default)
    }
  }

  override suspend fun get(key: String, default: Int): Int {
    return withContext(Dispatchers.IO) {
      sharedPreference.getInt(key, default)
    }
  }

  override suspend fun get(key: String, default: Boolean): Boolean {
    return withContext(Dispatchers.IO) {
      sharedPreference.getBoolean(key, default)
    }
  }

  override suspend fun set(key: String, data: String) {
    return withContext(Dispatchers.IO) {
      sharedPreference.edit().putString(key, data).apply()
    }
  }

  override suspend fun set(key: String, data: Long) {
    return withContext(Dispatchers.IO) {
      sharedPreference.edit().putLong(key, data).apply()
    }
  }

  override suspend fun set(key: String, data: Int) {
    return withContext(Dispatchers.IO) {
      sharedPreference.edit().putInt(key, data).apply()
    }
  }

  override suspend fun set(key: String, data: Boolean) {
    return withContext(Dispatchers.IO) {
      sharedPreference.edit().putBoolean(key, data).apply()
    }
  }
}