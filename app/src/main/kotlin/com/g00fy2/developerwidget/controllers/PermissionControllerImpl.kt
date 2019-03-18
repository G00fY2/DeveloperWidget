package com.g00fy2.developerwidget.controllers

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.content.ContextCompat
import com.g00fy2.developerwidget.utils.ACTIVITY
import javax.inject.Inject
import javax.inject.Named

class PermissionControllerImpl @Inject constructor() : PermissionController {

  @TargetApi(VERSION_CODES.M)
  override fun requestPermission(permission: String) {
    requestPermissions(arrayOf(permission))
  }

  @TargetApi(VERSION_CODES.M)
  override fun requestPermissions(permissions: Array<String>) {
    if (!hasPermissions(permissions)) (context as Activity).requestPermissions(permissions, 1)
  }

  @Inject @field:Named(ACTIVITY) lateinit var context: Context

  override fun hasPermission(permission: String): Boolean {
    return if (VERSION.SDK_INT >= VERSION_CODES.M) {
      ContextCompat.checkSelfPermission(
        context,
        permission
      ) == PackageManager.PERMISSION_GRANTED
    } else {
      true
    }
  }

  override fun hasPermissions(permissions: Array<String>): Boolean {
    var granted = true
    for (permission in permissions) {
      granted = granted && hasPermission(permission)
    }
    return granted
  }
}