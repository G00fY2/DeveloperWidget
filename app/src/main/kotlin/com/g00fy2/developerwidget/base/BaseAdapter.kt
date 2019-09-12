package com.g00fy2.developerwidget.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseAdapter<T, VH : BaseViewHolder<T>> constructor(diffCallback: DiffUtil.ItemCallback<T>) :
  ListAdapter<T, VH>(diffCallback) {

  private var commitCallback: Runnable? = null

  override fun submitList(list: List<T>?) {
    submitList(list, commitCallback)
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    holder.onBind(getItem(position))
  }

  open fun clearList() {
    submitList(null)
  }

  fun setCommitCallback(commitCallback: Runnable) {
    this.commitCallback = commitCallback
  }
}