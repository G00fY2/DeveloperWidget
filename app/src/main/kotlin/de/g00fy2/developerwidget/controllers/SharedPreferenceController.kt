package de.g00fy2.developerwidget.controllers

interface SharedPreferenceController {

  fun saveAppFilters(filters: List<String>)

  fun getAppFilters(): MutableList<String>

}