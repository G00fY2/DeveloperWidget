package com.g00fy2.developerwidget.activities.appmanager

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.content.pm.PackageInfoCompat
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.annotations.ACTIVITY
import javax.inject.Inject
import javax.inject.Named

class AppInfo private constructor() : Comparable<AppInfo> {

  var appName: String = ""
    private set
  var packageName: String = ""
    private set
  var appIcon: Drawable? = null
    private set
  var versionName: String = ""
    private set
  var versionCode: String = ""
    private set

  override fun compareTo(other: AppInfo) = compareValues(appName, other.appName)

  interface AppInfoBuilder {

    fun getInstalledPackages(): List<PackageInfo>

    fun build(packageInfo: PackageInfo): AppInfo
  }

  fun filterPackageName(filter: String): Boolean {
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

  fun filterPackageName(filterEntries: Collection<String>): Boolean {
    for (i in filterEntries) {
      if (filterPackageName(i)) return true
    }
    return false
  }

  class AppInfoBuilderImpl @Inject constructor(@Named(ACTIVITY) activity: BaseActivity) : AppInfoBuilder {
    private val packageManager = activity.packageManager

    override fun getInstalledPackages(): List<PackageInfo> {
      return if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
        packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
      } else {
        @Suppress("DEPRECATION")
        packageManager.getInstalledPackages(0)
      }
    }

    override fun build(packageInfo: PackageInfo): AppInfo {
      return AppInfo().apply {
        packageInfo.let { packageInfo ->
          packageInfo.packageName?.let { packageName = it }
          packageInfo.versionName?.let { versionName = it }
          versionCode = PackageInfoCompat.getLongVersionCode(packageInfo).toString()
          packageInfo.applicationInfo
        }?.let { appInfo ->
          appName = packageManager.getApplicationLabel(appInfo).toString()
          packageManager.getApplicationIcon(appInfo.packageName).let {
            appIcon = if (VERSION.SDK_INT >= VERSION_CODES.O && it is AdaptiveIconDrawable) {
              InsetDrawable(it, 0.025f, 0.01f, 0.025f, 0.04f)
            } else {
              it
            }
          }
        }
      }
    }
  }
}