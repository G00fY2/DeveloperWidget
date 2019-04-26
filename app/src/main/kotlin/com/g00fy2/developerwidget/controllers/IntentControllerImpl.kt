package com.g00fy2.developerwidget.controllers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.net.toUri
import com.g00fy2.developerwidget.BuildConfig
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.apkinstall.ApkFile
import com.g00fy2.developerwidget.utils.ACTIVITY
import com.g00fy2.developerwidget.utils.DEVELOPER_EMAIL
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class IntentControllerImpl @Inject constructor() : IntentController {

  @Inject
  @field:Named(ACTIVITY)
  lateinit var context: Context
  @Inject
  lateinit var toastController: ToastController

  override fun startActivity(intent: Intent) {
    if (intent.resolveActivity(context.packageManager) != null) {
      context.startActivity(intent)
    } else {
      Timber.w("Intent could not get resolved.")
    }
  }

  override fun installApk(apkFile: ApkFile) {
    apkFile.fileUri?.let {
      startActivity(Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(it, "application/vnd.android.package-archive")
        flags =
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Intent.FLAG_GRANT_READ_URI_PERMISSION else Intent.FLAG_ACTIVITY_NEW_TASK
      })
    } ?: toastController.showToast(R.string.access_file_fail)
  }

  override fun openAppSettings(packageName: String) {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, "package:$packageName".toUri()))
  }


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

}