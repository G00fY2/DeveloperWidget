package com.g00fy2.developerwidget.activities.widgetconfig

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.g00fy2.developerwidget.base.BaseAdapter
import com.g00fy2.developerwidget.base.BaseViewHolder
import com.g00fy2.developerwidget.data.DeviceDataItem
import com.g00fy2.developerwidget.databinding.DeviceDataHeaderItemBinding
import com.g00fy2.developerwidget.databinding.DeviceDataValueItemBinding

class DeviceDataAdapter : BaseAdapter<Pair<String, DeviceDataItem>, BaseViewHolder>(DeviceDataDiffUtilsCallback()) {

  inner class DeviceDataHeaderViewHolder(val binding: DeviceDataHeaderItemBinding) : BaseViewHolder(binding)
  inner class DeviceDataValueViewHolder(val binding: DeviceDataValueItemBinding) : BaseViewHolder(binding)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    return when (viewType) {
      HEADER_TYPE -> DeviceDataHeaderViewHolder(
        DeviceDataHeaderItemBinding.inflate(
          LayoutInflater.from(parent.context),
          parent,
          false
        )
      )
      VALUE_TYPE
      -> DeviceDataValueViewHolder(
        DeviceDataValueItemBinding.inflate(
          LayoutInflater.from(parent.context),
          parent,
          false
        )
      )
      else -> {
        throw IllegalStateException("Unknown ViewType")
      }
    }
  }

  override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
    if (holder is DeviceDataHeaderViewHolder) {
      holder.apply {
        getItem(position).let {
          binding.headerDividerView.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
          binding.headerTitleTextview.text = itemView.context.getString(it.second.title)
        }
      }
    } else if (holder is DeviceDataValueViewHolder) {
      holder.apply {
        getItem(position).let {
          binding.deviceDataTitle.text = itemView.context.getString(it.second.title)
          binding.deviceData.text = it.second.value

        }
      }
    }
  }

  override fun getItemViewType(position: Int) = if (getItem(position).second.isHeader) HEADER_TYPE else VALUE_TYPE

  companion object {
    const val HEADER_TYPE = 0
    const val VALUE_TYPE = 1
  }
}