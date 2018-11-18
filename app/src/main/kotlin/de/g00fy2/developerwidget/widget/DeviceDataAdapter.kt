package de.g00fy2.developerwidget.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.base.BaseAdapter
import de.g00fy2.developerwidget.base.BaseViewHolder
import de.g00fy2.developerwidget.data.DeviceDataItem
import kotlinx.android.synthetic.main.device_data_item.*

class DeviceDataAdapter : BaseAdapter<Pair<String, DeviceDataItem>, BaseViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    return BaseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.device_data_item, parent, false))
  }

  override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
    holder.apply {
      getItem(position).let {
        device_data_title.text = itemView.context.getString(it.second.title)
        device_data.text = it.second.value
      }
    }
  }
}