package de.g00fy2.developerwidget.activities.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.base.BaseAdapter
import de.g00fy2.developerwidget.base.BaseViewHolder
import de.g00fy2.developerwidget.data.DeviceDataItem
import kotlinx.android.synthetic.main.device_data_header_item.*
import kotlinx.android.synthetic.main.device_data_value_item.*

class DeviceDataAdapter : BaseAdapter<Pair<String, DeviceDataItem>, BaseViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    return when (viewType) {
      HEADER_TYPE -> BaseViewHolder(
        LayoutInflater.from(parent.context).inflate(
          R.layout.device_data_header_item,
          parent,
          false
        )
      )
      VALUE_TYPE -> BaseViewHolder(
        LayoutInflater.from(parent.context).inflate(
          R.layout.device_data_value_item,
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
    holder.apply {
      getItem(position).let {
        when (holder.itemViewType) {
          HEADER_TYPE -> {
            header_divider_view.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
            header_title_textview.text = itemView.context.getString(it.second.title)
          }
          VALUE_TYPE -> {
            device_data_title.text = itemView.context.getString(it.second.title)
            device_data.text = it.second.value
          }
        }
      }
    }
  }

  override fun setItems(newItems: MutableList<Pair<String, DeviceDataItem>>) {
    super.setItems(newItems, DiffUtil.calculateDiff((DeviceDataDiffUtilsCallback(items, newItems))))
  }

  override fun getItemViewType(position: Int) = if (getItem(position).second.isHeader) HEADER_TYPE else VALUE_TYPE

  companion object {
    const val HEADER_TYPE = 0
    const val VALUE_TYPE = 1
  }
}