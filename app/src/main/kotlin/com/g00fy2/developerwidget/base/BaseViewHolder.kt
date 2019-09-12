package com.g00fy2.developerwidget.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.g00fy2.developerwidget.ktx.addRipple

open class BaseViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

  fun addRipple() = binding.root.addRipple(true)
}