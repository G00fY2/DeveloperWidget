package com.g00fy2.developerwidget.activities.apkinstall

import com.g00fy2.developerwidget.base.BaseContract

interface ApkContract {

  interface ApkView : BaseContract.BaseView {

    fun toggleResultView(apkFiles: List<ApkFile>, missingPermissions: Boolean, searchDepth: Int = 0)

    fun showPermissionWarning(apkFile: ApkFile)
  }

  interface ApkPresenter : BaseContract.BasePresenter {

    fun installOrShowPermissionWarning(apkFile: ApkFile?)

    fun installApk(apkFile: ApkFile?)

    fun deleteApkFiles(apkFiles: List<ApkFile>?)
  }
}