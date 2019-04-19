package com.g00fy2.developerwidget.activities.apkinstall

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Environment
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.OnLifecycleEvent
import com.g00fy2.developerwidget.base.BasePresenterImpl
import com.g00fy2.developerwidget.controllers.IntentController
import com.g00fy2.developerwidget.controllers.PermissionController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ApkPresenterImpl @Inject constructor() : BasePresenterImpl(), ApkContract.ApkPresenter {

  @Inject
  lateinit var view: ApkContract.ApkView
  @Inject
  lateinit var intentController: IntentController
  @Inject
  lateinit var permissionController: PermissionController
  @Inject
  lateinit var apkFileBuilder: ApkFile.ApkFileBuilder

  @OnLifecycleEvent(Event.ON_CREATE)
  @TargetApi(VERSION_CODES.JELLY_BEAN)
  fun requestPermission() {
    permissionController.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
  }

  @OnLifecycleEvent(Event.ON_RESUME)
  fun scanStorageForApks() {
    if (permissionController.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    ) {
      launch {
        withContext(Dispatchers.IO) {
          searchAPKs(Environment.getExternalStorageDirectory())
        }.let {
          view.toggleResultView(it, false)
        }
      }
    } else {
      view.toggleResultView(ArrayList(), missingPermissions = true)
    }
  }

  private fun searchAPKs(dir: File): List<ApkFile> {
    return dir.walk()
      .filter { !it.isDirectory }
      .filter { it.extension.equals(APK_EXTENSION, true) }
      .map { apkFileBuilder.build(it) }
      .filter { it.valid }
      .sorted()
      .toList()

  }

  override fun installApk(apkFile: ApkFile?) {
    apkFile?.fileUri?.let {
      Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(it, APK_MIME_TYPE)
        flags =
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Intent.FLAG_GRANT_READ_URI_PERMISSION else Intent.FLAG_ACTIVITY_NEW_TASK
      }.let { intent -> intentController.startActivity(intent) }
    }
  }

  override fun deleteApkFiles(apkFiles: List<ApkFile>?) {
    if (permissionController.hasPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
    ) {
      launch {
        withContext(Dispatchers.IO) {
          apkFiles?.let { files ->
            for (apkFile in files) {
              File(apkFile.filePath).delete()
            }
          }
        }
        scanStorageForApks()
      }
    }
  }

  companion object {
    const val APK_EXTENSION = "apk"
    const val APK_MIME_TYPE = "application/vnd.android.package-archive"
  }
}