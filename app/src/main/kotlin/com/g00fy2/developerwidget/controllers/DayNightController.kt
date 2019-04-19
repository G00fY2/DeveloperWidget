package com.g00fy2.developerwidget.controllers

import androidx.appcompat.app.AppCompatActivity

interface DayNightController {

  fun saveCustomDefaultMode(mode: Int)

  fun loadCustomDefaultMode(activity: AppCompatActivity? = null)

  fun getCurrentDefaultMode(): Int

  fun isInNightMode(): Boolean

  fun toggleMode(activity: AppCompatActivity? = null)
}