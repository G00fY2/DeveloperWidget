package com.g00fy2.developerwidget.data.device.system

import android.annotation.SuppressLint
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import io.github.g00fy2.versioncompare.Version

object SystemDataProvider {

  fun getVersionAndSDK(): Pair<String, String> = Pair(getAndroidVersion(), getSDKLevel())

  fun getAndroidVersion(): String = VERSION.RELEASE

  fun getCodename(): String = VERSION.CODENAME

  @SuppressLint("DefaultLocale")
  fun getSDKLevel(): String {
    getCodename().let {
      return if (it.isBlank() || it.equals("REL", true)) {
        VERSION.SDK_INT.toString()
      } else {
        it.toUpperCase()
      }
    }
  }

  fun getPreviewSDK(): String {
    return if (VERSION.SDK_INT >= VERSION_CODES.M) {
      VERSION.PREVIEW_SDK_INT.toString()
    } else {
      ""
    }
  }

  fun getPatchLevel(): String {
    return if (VERSION.SDK_INT >= VERSION_CODES.M) {
      VERSION.SECURITY_PATCH
    } else {
      ""
    }
  }

  fun getVMVersion(): String {
    val vmVersion = System.getProperty("java.vm.version")
    val vmName = if (Version(vmVersion).isAtLeast("2", true)) "ART" else "Dalvik"
    return "$vmName $vmVersion"
  }

  fun getKernelVersion(): String = System.getProperty("os.version") ?: ""
}