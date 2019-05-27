package com.g00fy2.developerwidget.controllers

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.receiver.widget.WidgetProviderImpl
import com.g00fy2.developerwidget.utils.APPLICATION
import javax.inject.Inject
import javax.inject.Named

class DayNightControllerImpl @Inject constructor() : DayNightController {

  @Inject
  @field:Named(APPLICATION)
  lateinit var context: Context
  @Inject
  lateinit var toastController: ToastController

  private val defaultMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
  private val sharedPreference by lazy {
    context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
  }

  override fun saveCustomDefaultMode(mode: Int) = sharedPreference.edit { putInt(THEME_MODE, mode) }

  override fun loadCustomDefaultMode() = applyMode(sharedPreference.getInt(THEME_MODE, defaultMode))

  override fun getCurrentDefaultMode() = AppCompatDelegate.getDefaultNightMode()

  override fun isInNightMode() =
    context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO

  override fun toggleMode() {
    when (getCurrentDefaultMode()) {
      AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
      AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
      else -> AppCompatDelegate.MODE_NIGHT_YES
    }.let {
      // TODO remove if issuetracker.google.com/issues/131851825 is fixed
      if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) toastController.showToast(R.string.day_night_issue)
      saveCustomDefaultMode(it)
      applyMode(it)
      updateWidgetTheme()
    }
  }

  private fun applyMode(mode: Int) {
    AppCompatDelegate.setDefaultNightMode(mode)
  }

  private fun updateWidgetTheme() {
    context.sendBroadcast(Intent(context, WidgetProviderImpl::class.java).apply {
      action = WidgetProviderImpl.UPDATE_WIDGET_ACTION
      putExtra(UPDATE_WIDGET_THEME, true)
    })
  }

  companion object {
    const val THEME_MODE = "THEME_MODE"
    const val UPDATE_WIDGET_THEME = "UPDATE_WIDGET_THEME"
  }
}