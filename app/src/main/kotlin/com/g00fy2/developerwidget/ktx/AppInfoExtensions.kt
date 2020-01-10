package com.g00fy2.developerwidget.ktx

import com.g00fy2.developerwidget.activities.appmanager.AppInfo

fun AppInfo.filterPackageName(filter: String): Boolean {
  var result = false
  val filterInputs = filter.split("*")
  var tempValue = packageName
  for (i in filterInputs) {
    if (tempValue.contains(i, true)) {
      tempValue = tempValue.substringAfter(i)
      result = true
    } else {
      return false
    }
  }
  return result
}

fun AppInfo.filterPackageName(filterEntries: Collection<String>): Boolean {
  for (i in filterEntries) {
    if (filterPackageName(i)) return true
  }
  return false
}