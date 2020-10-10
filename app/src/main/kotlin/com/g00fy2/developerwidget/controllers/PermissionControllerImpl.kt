package com.g00fy2.developerwidget.controllers

import android.content.pm.PackageManager
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
    activity.registerForActivityResult(RequestMultiplePermissions()) { result ->
      if (result.all { it.value }) onGranted() else onDenied()
    }.launch(permissions)
  }
}