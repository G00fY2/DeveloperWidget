package de.g00fy2.developerwidget.widget

import androidx.recyclerview.widget.DiffUtil
import de.g00fy2.developerwidget.data.DeviceDataItem

class DeviceDataDiffUtilsCallback(
  private var oldItems: List<Pair<String, DeviceDataItem>>,
  private var newItems: List<Pair<String, DeviceDataItem>>
) : DiffUtil.Callback() {

  override fun getOldListSize() = oldItems.size

  override fun getNewListSize() = newItems.size

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
    oldItems[oldItemPosition].first === newItems[newItemPosition].first

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
    oldItems[oldItemPosition].second.value == newItems[newItemPosition].second.value
}