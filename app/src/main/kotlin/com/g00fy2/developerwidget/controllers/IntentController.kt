package com.g00fy2.developerwidget.controllers

import android.content.Intent
import com.g00fy2.developerwidget.activities.apkinstall.ApkFile

interface IntentController {

  fun startActivity(intent: Intent)

  fun installApk(apkFile: ApkFile)

  fun openAppSettings(packageName: String)

  fun sendMailToDeveloper()
}