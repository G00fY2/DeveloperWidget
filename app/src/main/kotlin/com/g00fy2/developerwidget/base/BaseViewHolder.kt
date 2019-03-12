package com.g00fy2.developerwidget.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

open class BaseViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer