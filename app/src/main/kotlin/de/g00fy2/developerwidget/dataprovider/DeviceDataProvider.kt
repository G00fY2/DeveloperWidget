package de.g00fy2.developerwidget.dataprovider

import android.content.Context
import android.os.Build

class DeviceDataProvider(private val context: Context) {

  val deviceInfo: String
    get() {
      if (Build.MODEL.contains(Build.MANUFACTURER)) {
        return Build.MODEL
      }
      return Build.MANUFACTURER + " " + Build.MODEL
    }

  val versionAndCodename: String
    get() = "Android " + Build.VERSION.RELEASE

  val sdkVersion: String
    get() = "API Level " + Integer.toString(Build.VERSION.SDK_INT)
}
