package de.g00fy2.developerwidget.widget.devicedata

import androidx.annotation.StringRes

class DeviceDataItem(value: String, @StringRes title: Int, category: Category, userEditable: Boolean = false) {

  var value = value; private set
  var title = title; private set
  var category = category; private set
  var userEditable = userEditable; private set

  enum class Category(val title: String) {
    DEVICE("Device"),
    SYSTEM("System"),
    DISPLAY("Display"),
    MEMORY("Memory"),
    FEATURES("Features"),
    SOFTWARE("Software")
  }
}