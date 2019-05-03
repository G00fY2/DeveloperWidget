package com.g00fy2.developerwidget.base

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

open class BaseViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

  fun addRipple() {
    TypedValue().apply {
      containerView.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    }.resourceId.let {
      if (VERSION.SDK_INT < VERSION_CODES.M) itemView.setBackgroundResource(it) else itemView.foreground =
        containerView.context.getDrawable(it)
    }
  }
}