package com.g00fy2.developerwidget.utils

class FilterUtils {

  companion object {
    fun filterValue(value: String, filter: String): Boolean {
      var result = false
      val filterInputs = filter.split("*")
      var tempValue = value
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

    fun filterValueByCollection(value: String, filterEntries: Collection<String>): Boolean {
      for (i in filterEntries) {
        if (filterValue(value, i)) return true
      }
      return false
    }
  }
}