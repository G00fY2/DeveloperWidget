package com.g00fy2.developerwidget.controllers

import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.di.annotations.APPLICATION
import com.g00fy2.developerwidget.receiver.widget.WidgetProvider
import javax.inject.Inject
import javax.inject.Named

class DayNightControllerImpl @Inject constructor() : DayNightController {

  @Inject
  @field:Named(APPLICATION)
  lateinit var context: Context
  @Inject
  lateinit var toastController: ToastController

  private val defaultMode =
    if (VERSION.SDK_INT >= VERSION_CODES.P) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM else AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
  private val sharedPreference by lazy {
    context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
  }

  override fun saveCustomDefaultMode(mode: Int) = sharedPreference.edit { putInt(THEME_MODE, mode) }

  override fun loadCustomDefaultMode() = applyMode(sharedPreference.getInt(THEME_MODE, defaultMode))

  override fun getCurrentDefaultMode() = AppCompatDelegate.getDefaultNightMode()

  override fun toggleMode() {
    when (getCurrentDefaultMode()) {
      AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
      AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
      else -> AppCompatDelegate.MODE_NIGHT_YES
    }.let {
      // TODO remove if https://issuetracker.google.com/issues/131851825 is fixed
      if (VERSION.SDK_INT <= VERSION_CODES.M) toastController.showToast(R.string.day_night_issue)
      saveCustomDefaultMode(it)
      applyMode(it)
      updateWidgetTheme()
    }
  }

  private fun applyMode(mode: Int) = AppCompatDelegate.setDefaultNightMode(mode)

  private fun updateWidgetTheme() {
    context.sendBroadcast(Intent(context, WidgetProvider::class.java).apply {
      action = WidgetProvider.UPDATE_WIDGET_MANUALLY_ACTION
      putExtra(UPDATE_WIDGET_THEME, true)
    })
  }

  companion object {
    const val THEME_MODE = "THEME_MODE"
    const val UPDATE_WIDGET_THEME = "UPDATE_WIDGET_THEME"
  }
}