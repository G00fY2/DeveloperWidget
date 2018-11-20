package de.g00fy2.developerwidget.base

import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

  protected var items: MutableList<T> = ArrayList(); private set

  override fun getItemCount() = items.size

  fun getItem(position: Int): T = items[position]

  open fun clear() {
    if (items.isNotEmpty()) {
      items.clear()
      notifyDataSetChanged()
    }
  }

  open fun addAll(newItems: MutableList<T>) {
    addAll(newItems, null)
  }

  fun addAll(newItems: MutableList<T>, diffResult: DiffResult? = null) {
    val emptyList = items.isEmpty() || newItems.isEmpty()
    items = newItems
    if (diffResult == null || emptyList) notifyDataSetChanged() else diffResult.dispatchUpdatesTo(this)

  }
}