package com.g00fy2.developerwidget.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.g00fy2.developerwidget.ktx.addRipple

abstract class BaseViewHolder<T>(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

  abstract fun onBind(item: T)

  fun addRipple() = itemView.addRipple(true)
}