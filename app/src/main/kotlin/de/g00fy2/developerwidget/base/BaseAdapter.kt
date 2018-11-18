package de.g00fy2.developerwidget.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

  private var items: MutableList<T> = ArrayList()

  override fun getItemCount() = items.size

  fun getItem(position: Int): T = items[position]

  open fun clear() {
    if (items.size > 0) {
      items.clear()
      notifyDataSetChanged()
    }
  }

  fun addAll(items: Collection<T>) {
    this.items = items.toMutableList()
    notifyDataSetChanged()
  }
}