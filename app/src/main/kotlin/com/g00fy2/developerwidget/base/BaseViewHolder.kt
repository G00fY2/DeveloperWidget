package com.g00fy2.developerwidget.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.g00fy2.developerwidget.ktx.addRipple

abstract class BaseViewHolder<T>(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

  abstract fun onBind(item: T)

  fun addRipple() = binding.root.addRipple(true)
}