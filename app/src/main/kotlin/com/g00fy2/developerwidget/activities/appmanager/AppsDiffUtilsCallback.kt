package com.g00fy2.developerwidget.activities.appmanager

import androidx.recyclerview.widget.DiffUtil

class AppsDiffUtilsCallback : DiffUtil.ItemCallback<AppInfo>() {

  override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo) = oldItem.packageName == newItem.packageName

  override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo) = true
}