package com.g00fy2.developerwidget.activities.apkinstall

import android.Manifest
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import com.g00fy2.developerwidget.activities.apkinstall.controllers.StorageDirsController
import com.g00fy2.developerwidget.base.BasePresenterImpl
import com.g00fy2.developerwidget.controllers.IntentController
import com.g00fy2.developerwidget.controllers.PermissionController
import com.g00fy2.developerwidget.controllers.PreferenceController
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
  lateinit var storageDirsController: StorageDirsController

  @Inject
  lateinit var apkFileBuilder: ApkFile.ApkFileBuilder

  @Inject
  lateinit var preferenceController: PreferenceController

  @OnLifecycleEvent(Event.ON_CREATE)
  fun scanStoreageIfPermissionGranted() {
    permissionController.requestPermissions(
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      onGranted = { scanStorageForApks() },
      onDenied = { view.toggleResultView(emptyList(), true) })
  }

  private fun scanStorageForApks() {
    var depth: Int
    view.lifecycleScope.launch {
      withContext(Dispatchers.IO) {
        depth = preferenceController.get(PreferenceController.SEARCH_DEPTH, 2)
        mutableSetOf<ApkFile>().apply {
          for (dir in storageDirsController.getStorageDirectories()) {
            addAll(searchAPKs(dir, depth))
          }
        }.sorted()
      }.let {
        view.toggleResultView(it, false, depth)
      }
    }
  }

  private fun searchAPKs(dir: File, depth: Int): Collection<ApkFile> {
    return dir.walk()
      .maxDepth(depth)
      .filter { it.extension.equals("apk", true) }
      .filterNot { it.isDirectory }
      .map { apkFileBuilder.build(it) }
      .filter { it.valid }
      .toList()
  }

  override fun installOrShowPermissionWarning(apkFile: ApkFile?) {
    apkFile?.let {
      if (VERSION.SDK_INT >= VERSION_CODES.M && it.targetSdkVersion < VERSION_CODES.M
        && it.dangerousPermissions.isNotEmpty()
      ) {
        view.showPermissionWarning(it)
      } else {
        installApk(it)
      }
    }
  }

  override fun installApk(apkFile: ApkFile?) {
    apkFile?.let { intentController.installApk(it) }
  }

  override fun deleteApkFiles(apkFiles: List<ApkFile>?) {
    if (permissionController.hasPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
      view.lifecycleScope.launch {
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
}