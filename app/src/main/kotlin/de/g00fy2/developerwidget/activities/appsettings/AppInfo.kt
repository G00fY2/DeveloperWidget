package de.g00fy2.developerwidget.activities.appsettings

import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import androidx.core.content.pm.PackageInfoCompat

class AppInfo : Comparable<AppInfo> {

  var appName: String = ""; private set
  var packageName: String = ""; private set
  var appIcon: Drawable? = null; private set
  var versionName: String? = ""; private set
  var versionCode: String? = ""; private set

  override fun compareTo(other: AppInfo) = compareValues(appName, other.appName)

  class Builder(context: Context) {
    private val packageManager = context.packageManager

    fun build(packageInfo: PackageInfo): AppInfo {
      return AppInfo().apply {
        packageInfo.let { packageInfo ->
          packageName = packageInfo.packageName
          versionName = packageInfo.versionName
          versionCode = PackageInfoCompat.getLongVersionCode(packageInfo).toString()
          packageInfo.applicationInfo
        }?.let { appInfo ->
          appName = packageManager.getApplicationLabel(appInfo).toString()
          appIcon = packageManager.getApplicationIcon(appInfo.packageName)
        }
      }
    }
  }
}