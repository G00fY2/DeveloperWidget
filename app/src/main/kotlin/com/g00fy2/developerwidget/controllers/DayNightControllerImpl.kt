package com.g00fy2.developerwidget.controllers

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.g00fy2.developerwidget.utils.APPLICATION
import javax.inject.Inject
import javax.inject.Named

class DayNightControllerImpl @Inject constructor() : DayNightController {

  @Inject
  @field:Named(APPLICATION)
  lateinit var context: Context

  private val defaultMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
  private val sharedPreference by lazy {
    context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
  }

  override fun saveCustomDefaultMode(mode: Int) = sharedPreference.edit { putInt(THEME_MODE, mode) }

  override fun loadCustomDefaultMode(activity: AppCompatActivity?) =
    applyMode(sharedPreference.getInt(THEME_MODE, defaultMode), activity)


  override fun getCurrentDefaultMode() = AppCompatDelegate.getDefaultNightMode()

  override fun isInNightMode() =
    context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO

  override fun toggleMode(activity: AppCompatActivity?) {
    when (getCurrentDefaultMode()) {
      AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
      AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
      else -> AppCompatDelegate.MODE_NIGHT_YES
    }.let {
      saveCustomDefaultMode(it)
      applyMode(it, activity)
    }
  }

  private fun applyMode(mode: Int, activity: AppCompatActivity? = null) {
    activity?.delegate?.localNightMode = mode
    AppCompatDelegate.setDefaultNightMode(mode)
  }

  companion object {
    const val THEME_MODE = "THEME_MODE"
  }
}