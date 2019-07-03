package com.g00fy2.developerwidget.controllers

import com.g00fy2.developerwidget.activities.apkinstall.ApkFile

interface IntentController {

  fun openWebsite(url: String, useCustomTabs: Boolean = false)

  fun installApk(apkFile: ApkFile)

  fun openAppSettings(packageName: String)

  fun sendMailToDeveloper()

  fun showHomescreen()

  fun shareDeviceData(data: String)
}