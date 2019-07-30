package com.g00fy2.developerwidget.controllers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.Settings
import androidx.core.net.toUri
import com.g00fy2.developerwidget.BuildConfig
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.apkinstall.ApkFile
import com.g00fy2.developerwidget.activities.about.DEVELOPER_EMAIL
import com.g00fy2.developerwidget.di.annotations.ACTIVITY
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class IntentControllerImpl @Inject constructor() : IntentController {

  @Inject
  @field:Named(ACTIVITY)
  lateinit var context: Context
  @Inject
  lateinit var toastController: ToastController

  override fun openWebsite(url: String, useCustomTabs: Boolean) {
    Intent(Intent.ACTION_VIEW, url.toUri()).let { intent ->
      if (useCustomTabs && VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
        intent.putExtras(Bundle().apply {
          putBinder("android.support.customtabs.extra.SESSION", null)
        })
      }
      startActivity(intent)
    }
  }

  override fun installApk(apkFile: ApkFile) {
    apkFile.fileUri?.let {
      startActivity(Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(it, "application/vnd.android.package-archive")
        flags =
          if (VERSION.SDK_INT >= VERSION_CODES.N) Intent.FLAG_GRANT_READ_URI_PERMISSION else Intent.FLAG_ACTIVITY_NEW_TASK
      })
    } ?: toastController.showToast(R.string.access_file_fail)
  }

  override fun openAppSettings(packageName: String) =
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, "package:$packageName".toUri()))

  override fun sendMailToDeveloper() {
    startActivity(Intent(Intent.ACTION_SENDTO).apply {
      data = Uri.parse("mailto:")
      putExtra(Intent.EXTRA_EMAIL, arrayOf(DEVELOPER_EMAIL))
      putExtra(
        Intent.EXTRA_SUBJECT,
        "Feedback [" + BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE + "]"
      )
    })
  }

  override fun showHomescreen() {
    startActivity(Intent(Intent.ACTION_MAIN).apply {
      addCategory(Intent.CATEGORY_HOME)
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
  }

  override fun shareDeviceData(data: String) {
    startActivity(Intent.createChooser(Intent().apply {
      action = Intent.ACTION_SEND
      putExtra(Intent.EXTRA_TEXT, data)
      type = "text/plain"
    }, context.getString(R.string.share_device_data)))
  }

  private fun startActivity(intent: Intent) {
    if (intent.resolveActivity(context.packageManager) != null) {
      context.startActivity(intent)
    } else {
      Timber.w("Intent could not get resolved.")
    }
  }
}