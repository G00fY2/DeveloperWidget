package de.g00fy2.developerwidget.data

import androidx.annotation.StringRes

class DeviceDataItem(value: String, @StringRes title: Int, category: Category, userEditable: Boolean = false) :
  Comparable<DeviceDataItem> {

  override fun compareTo(other: DeviceDataItem) = compareValuesBy(this, other, { it.category.ordinal }, { it.title })

  var value = value; private set
  var title = title; private set
  var category = category; private set
  var userEditable = userEditable; private set

  enum class Category(val title: String) {
    DEVICE("Device"),
    SYSTEM("System"),
    CPU("CPU"),
    MEMORY("Memory"),
    DISPLAY("Display"),
    FEATURES("Features"),
    SOFTWARE("Software")
  }
}