package com.g00fy2.developerwidget.ktx

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

fun View.addRipple(asForeground: Boolean = false) {
  TypedValue().apply { context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true) }
    .resourceId.let {
    if (VERSION.SDK_INT < VERSION_CODES.M || !asForeground) setBackgroundResource(it) else foreground =
      context.getDrawable(it)
  }
}

fun View.updateMargin(
  @Px left: Int = marginLeft,
  @Px top: Int = marginTop,
  @Px right: Int = marginRight,
  @Px bottom: Int = marginBottom
) {
  val params = layoutParams as ViewGroup.MarginLayoutParams
  params.leftMargin = left
  params.topMargin = top
  params.rightMargin = right
  params.bottomMargin = bottom
  layoutParams = params
}

@RequiresApi(VERSION_CODES.KITKAT_WATCH)
fun View.doOnApplyWindowInsets(f: (View, WindowInsets, InitialPadding, InitialMargin) -> Unit) {
  val initialPadding = recordInitialPaddingForView(this)
  val initialMargin = recordInitialMarginForView(this)
  setOnApplyWindowInsetsListener { v, insets ->
    f(v, insets, initialPadding, initialMargin)
    insets
  }
}

data class InitialPadding(
  val left: Int, val top: Int,
  val right: Int, val bottom: Int
)

data class InitialMargin(
  val left: Int, val top: Int,
  val right: Int, val bottom: Int
)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
  view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)

private fun recordInitialMarginForView(view: View) = InitialMargin(
  view.marginLeft, view.marginTop, view.marginRight, view.marginBottom
)