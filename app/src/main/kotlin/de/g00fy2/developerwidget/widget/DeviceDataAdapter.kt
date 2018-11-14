package de.g00fy2.developerwidget.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.data.DeviceDataItem
import de.g00fy2.developerwidget.widget.DeviceDataAdapter.ViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.device_data_item.*

class DeviceDataAdapter : RecyclerView.Adapter<ViewHolder>() {

  private var deviceDataItems: MutableList<Pair<String, DeviceDataItem>> = ArrayList()

  class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.device_data_item, parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.apply {
      deviceDataItems[position].let {
        device_data_title.text = itemView.context.getString(it.second.title)
        device_data.text = it.second.value
      }
    }
  }

  override fun getItemCount() = deviceDataItems.size

  fun clear() {
    deviceDataItems.clear()
    notifyDataSetChanged()
  }

  fun addAll(deviceDataItems: Collection<Pair<String, DeviceDataItem>>) {
    this.deviceDataItems = deviceDataItems.toMutableList()
    notifyDataSetChanged()
  }
}