package com.g00fy2.developerwidget.controllers

import com.g00fy2.developerwidget.base.HasThemeDelegate

interface DayNightController {

  fun saveCustomDefaultMode(mode: Int)

  fun loadCustomDefaultMode()

  fun getCurrentDefaultMode(): Int

  fun isInNightMode(): Boolean

  fun toggleMode(delegate: HasThemeDelegate)
}