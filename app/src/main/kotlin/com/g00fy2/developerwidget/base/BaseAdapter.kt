package com.g00fy2.developerwidget.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> constructor(diffCallback: DiffUtil.ItemCallback<T> = DefaultItemDiffCallback()) :
  ListAdapter<T, VH>(diffCallback) {

  private var commitCallback: Runnable? = null

  override fun submitList(list: List<T>?) {
    submitList(list, commitCallback)
  }

  open fun clearList() {
    submitList(null)
  }

  fun setCommitCallback(commitCallback: Runnable) {
    this.commitCallback = commitCallback
  }
}