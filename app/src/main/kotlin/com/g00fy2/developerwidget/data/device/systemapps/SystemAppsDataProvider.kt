package com.g00fy2.developerwidget.data.device.systemapps

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.webkit.WebView
import io.github.g00fy2.versioncompare.Version

object SystemAppsDataProvider {

  fun getGooglePlayServicesVersion(context: Context): String {
    return try {
      context.getPackageInfo("com.google.android.gms").versionName
    } catch (e: NameNotFoundException) {
      ""
    }
  }

  @SuppressLint("PrivateApi", "WebViewApiAvailability")
  fun getWebViewImplementation(context: Context): String {
    return when {
      VERSION.SDK_INT >= VERSION_CODES.O -> {
        WebView.getCurrentWebViewPackage()?.let {
          context.packageManager.getApplicationLabel(it.applicationInfo).toString() + " " + it.versionName
        }.orEmpty()
      }
      VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP -> {
        try {
          (Class.forName("android.webkit.WebViewFactory")
            .getMethod("getLoadedPackageInfo")
            .invoke(null) as PackageInfo?)?.let {
            context.packageManager.getApplicationLabel(it.applicationInfo).toString() + " " + it.versionName
          }.orEmpty()
        } catch (t: Throwable) {
          ""
        }
      }
      Version(VERSION.RELEASE).isAtLeast("4.4.3") -> "WebView v33.0.0.0"
      VERSION.SDK_INT >= VERSION_CODES.KITKAT -> "WebView v30.0.0.0"
      else -> try {
        context.getPackageInfo("com.google.android.webview")
      } catch (e: NameNotFoundException) {
        null
      }?.let { context.packageManager.getApplicationLabel(it.applicationInfo).toString() + " " + it.versionName }
        .orEmpty()
    }
  }

  private fun Context.getPackageInfo(packageName: String): PackageInfo {
    return packageManager.run {
      if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
      } else {
        @Suppress("DEPRECATION")
        getPackageInfo(packageName, 0)
      }
    }
  }
}