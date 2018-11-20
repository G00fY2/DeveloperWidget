package de.g00fy2.developerwidget.base

import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

  protected var items: MutableList<T> = ArrayList(); private set

  override fun getItemCount() = items.size

  fun getItem(position: Int): T = items[position]

  open fun clear() {
    if (items.size > 0) {
      items.clear()
      notifyDataSetChanged()
    }
  }

  fun addAll(items: MutableList<T>) {
    this.items = items
    notifyDataSetChanged()
  }

  fun update(newItems: MutableList<T>, diffResult: DiffResult) {
    items = newItems
    diffResult.dispatchUpdatesTo(this)
  }
}