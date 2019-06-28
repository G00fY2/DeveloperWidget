package com.g00fy2.developerwidget.activities.widgetconfig

import androidx.recyclerview.widget.DiffUtil
import com.g00fy2.developerwidget.data.DeviceDataItem

class DeviceDataDiffUtilsCallback : DiffUtil.ItemCallback<Pair<String, DeviceDataItem>>() {

  override fun areItemsTheSame(oldItem: Pair<String, DeviceDataItem>, newItem: Pair<String, DeviceDataItem>) =
    oldItem.first == newItem.first

  override fun areContentsTheSame(oldItem: Pair<String, DeviceDataItem>, newItem: Pair<String, DeviceDataItem>) =
    oldItem.second.value == newItem.second.value
}