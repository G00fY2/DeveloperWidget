package com.g00fy2.developerwidget.controllers

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.content.ContextCompat
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.annotations.ACTIVITY
import javax.inject.Inject
import javax.inject.Named

class PermissionControllerImpl @Inject constructor() : PermissionController {

  @Inject
  @Named(ACTIVITY)
  lateinit var activity: BaseActivity

  override fun hasPermissions(vararg permissions: String): Boolean {
    return permissions.all { ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED }
  }

  override fun requestPermissions(vararg permissions: String, onGranted: (() -> Unit), onDenied: () -> Unit) {
    if (VERSION.SDK_INT >= VERSION_CODES.R && permissions.any {
        it == Manifest.permission.READ_EXTERNAL_STORAGE || it == Manifest.permission.WRITE_EXTERNAL_STORAGE
      } && !Environment.isExternalStorageManager()) {
      activity.startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
        data = Uri.parse("package:${activity.packageName}")
      })
    } else {
      activity.registerForActivityResult(RequestMultiplePermissions()) { result ->
        if (result.all { it.value }) onGranted() else onDenied()
      }.launch(permissions)
    }
  }
}