package com.g00fy2.developerwidget.data.device.systemapps

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.webkit.WebView
import io.github.g00fy2.versioncompare.Version

object SystemAppsDataProvider {

  fun getGooglePlayServicesVersion(context: Context): String {
    return try {
      context.packageManager.getPackageInfo("com.google.android.gms", 0).versionName
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
        } ?: ""
      }
      VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP -> {
        try {
          (Class.forName("android.webkit.WebViewFactory")
            .getMethod("getLoadedPackageInfo")
            .invoke(null) as PackageInfo?)?.let {
            context.packageManager.getApplicationLabel(it.applicationInfo).toString() + " " + it.versionName
          }
            ?: ""
        } catch (t: Throwable) {
          ""
        }
      }
      Version(VERSION.RELEASE).isAtLeast("4.4.3") -> "WebView v33.0.0.0"
      VERSION.SDK_INT >= VERSION_CODES.KITKAT -> "WebView v30.0.0.0"
      else -> try {
        context.packageManager.getPackageInfo("com.google.android.webview", 0)
      } catch (e: NameNotFoundException) {
        null
      }?.let { context.packageManager.getApplicationLabel(it.applicationInfo).toString() + " " + it.versionName }
        ?: ""
    }
  }
}