package com.g00fy2.developerwidget.controllers

interface DayNightController {

  fun saveCustomDefaultMode(mode: Int)

  fun loadCustomDefaultMode()

  fun getCurrentDefaultMode(): Int

  fun isInNightMode(): Boolean

  fun toggleMode()
}