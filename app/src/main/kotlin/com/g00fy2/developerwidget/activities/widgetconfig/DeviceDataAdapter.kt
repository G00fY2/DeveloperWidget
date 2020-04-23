package com.g00fy2.developerwidget.activities.widgetconfig

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.g00fy2.developerwidget.base.BaseAdapter
import com.g00fy2.developerwidget.base.BaseViewHolder
import com.g00fy2.developerwidget.data.DeviceDataItem
import com.g00fy2.developerwidget.databinding.DeviceDataHeaderItemBinding
import com.g00fy2.developerwidget.databinding.DeviceDataValueItemBinding

class DeviceDataAdapter :
  BaseAdapter<Pair<String, DeviceDataItem>, BaseViewHolder<Pair<String, DeviceDataItem>>>(DeviceDataDiffUtilsCallback()) {

  inner class DeviceDataHeaderViewHolder(val binding: DeviceDataHeaderItemBinding) :
    BaseViewHolder<Pair<String, DeviceDataItem>>(binding) {
    override fun onBind(item: Pair<String, DeviceDataItem>) {
      item.run {
        binding.headerDividerView.visibility = if (bindingAdapterPosition == 0) View.INVISIBLE else View.VISIBLE
        binding.headerTitleTextview.text = itemView.context.getString(second.title)
      }
    }
  }

  inner class DeviceDataValueViewHolder(val binding: DeviceDataValueItemBinding) :
    BaseViewHolder<Pair<String, DeviceDataItem>>(binding) {
    override fun onBind(item: Pair<String, DeviceDataItem>) {
      item.run {
        binding.deviceDataTitle.text = itemView.context.getString(second.title)
        binding.deviceData.text = second.value
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Pair<String, DeviceDataItem>> {
    return when (viewType) {
      HEADER_TYPE -> DeviceDataHeaderViewHolder(
        DeviceDataHeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      )
      VALUE_TYPE
      -> DeviceDataValueViewHolder(
        DeviceDataValueItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      )
      else -> {
        throw IllegalStateException("Unknown ViewType")
      }
    }
  }

  override fun getItemViewType(position: Int) = if (getItem(position).second.isHeader) HEADER_TYPE else VALUE_TYPE

  companion object {
    const val HEADER_TYPE = 0
    const val VALUE_TYPE = 1
  }
}