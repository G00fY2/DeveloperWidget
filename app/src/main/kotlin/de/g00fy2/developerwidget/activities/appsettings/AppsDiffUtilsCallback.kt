package de.g00fy2.developerwidget.activities.appsettings

import androidx.recyclerview.widget.DiffUtil

class AppsDiffUtilsCallback : DiffUtil.ItemCallback<AppInfo>() {
  override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
    return oldItem === newItem
  }

  override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
    return oldItem.packageName == newItem.packageName
  }
}