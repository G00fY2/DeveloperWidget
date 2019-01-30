package de.g00fy2.developerwidget.util

class FilterUtil {

  companion object {

    fun valueContainsAllFilters(value: String, filterInputs: List<String>): Boolean {
      var result = false
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
  }
}