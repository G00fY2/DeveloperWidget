package de.g00fy2.developerwidget.util

import android.os.Build

class DeviceDataUtils {

  companion object {
    val deviceName: String = if (Build.MODEL.contains(Build.MANUFACTURER, true)) {
      Build.MODEL.capitalize()
    } else {
      Build.MANUFACTURER.capitalize() + " " + Build.MODEL.capitalize()
    }

    val androidVersion: String = "Android " + Build.VERSION.RELEASE

    val androidApiLevel: String = "API Level " + Integer.toString(Build.VERSION.SDK_INT)
  }
}