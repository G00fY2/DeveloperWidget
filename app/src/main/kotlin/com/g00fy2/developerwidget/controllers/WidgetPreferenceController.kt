package com.g00fy2.developerwidget.controllers

interface WidgetPreferenceController {

  fun saveAppFilters(filters: List<String>)

  fun getAppFilters(): MutableList<String>

}