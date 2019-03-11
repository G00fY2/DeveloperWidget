package de.g00fy2.developerwidget.activities.appmanager

import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import androidx.core.content.pm.PackageInfoCompat
import de.g00fy2.developerwidget.utils.ACTIVITY
import javax.inject.Inject
import javax.inject.Named

class AppInfo : Comparable<AppInfo> {

  var appName: String = ""; private set
  var packageName: String = ""; private set
  var appIcon: Drawable? = null; private set
  var versionName: String? = ""; private set
  var versionCode: String? = ""; private set

  override fun compareTo(other: AppInfo) = compareValues(appName, other.appName)

  // TODO split builder from data object
  interface AppInfoBuilder {

    fun getInstalledPackages(): List<PackageInfo>

    fun build(packageInfo: PackageInfo): AppInfo
  }

  class AppInfoBuilderImpl @Inject constructor(@Named(ACTIVITY) context: Context) : AppInfoBuilder {
    private val packageManager = context.packageManager

    override fun getInstalledPackages(): List<PackageInfo> {
      return packageManager.getInstalledPackages(0)
    }

    override fun build(packageInfo: PackageInfo): AppInfo {
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