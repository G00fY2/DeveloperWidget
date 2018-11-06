package de.g00fy2.developerwidget.data

import androidx.annotation.StringRes

class DeviceDataItem(value: String, @StringRes title: Int, category: Category, userEditable: Boolean = false) {

  var value = value; private set
  var title = title; private set
  var category = category; private set
  var userEditable = userEditable; private set

  enum class Category(val title: String) {
    CPU("CPU"),
    DEVICE("Device"),
    DISPLAY("Display"),
    FEATURES("Features"),
    MEMORY("Memory"),
    SOFTWARE("Software"),
    SYSTEM("System")
  }
}