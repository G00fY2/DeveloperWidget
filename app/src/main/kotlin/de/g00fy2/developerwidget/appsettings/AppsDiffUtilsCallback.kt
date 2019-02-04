package de.g00fy2.developerwidget.appsettings

import androidx.recyclerview.widget.DiffUtil

class AppsDiffUtilsCallback(
  private var oldItems: List<AppInfo>,
  private var newItems: List<AppInfo>
) : DiffUtil.Callback() {

  override fun getOldListSize() = oldItems.size

  override fun getNewListSize() = newItems.size

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
    oldItems[oldItemPosition] === newItems[newItemPosition]

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
    oldItems[oldItemPosition].packageName == newItems[newItemPosition].packageName
}