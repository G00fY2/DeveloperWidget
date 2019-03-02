package de.g00fy2.developerwidget.base

import androidx.recyclerview.widget.DiffUtil

class DefaultItemDiffCallback<T> : DiffUtil.ItemCallback<T>() {
  override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
    return oldItem === newItem
  }

  override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
    return areItemsTheSame(oldItem, newItem)
  }
}