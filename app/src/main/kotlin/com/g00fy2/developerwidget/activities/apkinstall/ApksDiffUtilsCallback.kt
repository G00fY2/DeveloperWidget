package com.g00fy2.developerwidget.activities.apkinstall

import androidx.recyclerview.widget.DiffUtil

class ApksDiffUtilsCallback : DiffUtil.ItemCallback<ApkFile>() {

  override fun areItemsTheSame(oldItem: ApkFile, newItem: ApkFile): Boolean {
    return oldItem.fileUri == newItem.fileUri
  }

  override fun areContentsTheSame(oldItem: ApkFile, newItem: ApkFile) = true
}