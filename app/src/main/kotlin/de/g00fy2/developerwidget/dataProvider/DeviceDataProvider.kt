package de.g00fy2.developerwidget.dataProvider

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
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

  val appInfo: String
    get() {
      val pInfo: PackageInfo
      var version = ""
      var verCode = 0
      try {
        pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        version = pInfo.versionName
        verCode = pInfo.versionCode
      } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
      }

      return version + " (" + Integer.toString(verCode) + ")"
    }
}
