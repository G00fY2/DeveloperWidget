package com.g00fy2.developerwidget.data

import androidx.annotation.StringRes
import com.g00fy2.developerwidget.R

class DeviceDataItem(value: String, @StringRes title: Int, category: Category, isHeader: Boolean = false) {

  constructor(category: Category, isHeader: Boolean) : this("", category.titleRes, category, isHeader)

  var value = value; private set
  var title = title; private set
  var category = category; private set
  var isHeader = isHeader; private set

  enum class Category(val title: String, @StringRes val titleRes: Int) {
    DEVICE("Device", R.string.device_title),
    SYSTEM("System", R.string.system_title),
    CPU("CPU", R.string.cpu_title),
    MEMORY("Memory", R.string.memory_title),
    DISPLAY("Display", R.string.display_title),
    FEATURES("Features", R.string.features_title),
    SOFTWARE("Software", R.string.software_title)
  }
}