package de.g00fy2.developerwidget.data.systemapps

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.webkit.WebView

class SystemAppsDataProvider {

  companion object {

    fun getGooglePlayServicesVersion(context: Context): String {
      return try {
        context.packageManager.getPackageInfo("com.google.android.gms", 0).versionName
      } catch (e: NameNotFoundException) {
        "not installed"
      }
    }

    @SuppressLint("PrivateApi")
    fun getWebViewImplementation(context: Context): String {
      return when {
        VERSION.SDK_INT >= VERSION_CODES.O -> WebView.getCurrentWebViewPackage()
        VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP -> {
          try {
            val webViewFactory = Class.forName("android.webkit.WebViewFactory")
            val method = webViewFactory.getMethod("getLoadedPackageInfo")
            method.invoke(null, null) as PackageInfo
          } catch (t: Throwable) {
            null
          }
        }
        else -> context.packageManager.getPackageInfo("com.google.android.webview", 0)
      }?.let { context.packageManager.getApplicationLabel(it.applicationInfo).toString() + " " + it.versionName }
        ?: ""
    }
  }
}