package com.g00fy2.developerwidget.ktx

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import androidx.annotation.RequiresApi

fun View.addRipple(asForeground: Boolean = false) {
  TypedValue().apply { context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true) }
    .resourceId.let {
    if (VERSION.SDK_INT < VERSION_CODES.M || !asForeground) setBackgroundResource(it) else foreground =
      context.getDrawable(it)
  }
}

@RequiresApi(VERSION_CODES.KITKAT_WATCH)
fun View.doOnApplyWindowInsets(f: (View, WindowInsets, InitialPadding) -> Unit) {
  val initialPadding = recordInitialPaddingForView(this)
  setOnApplyWindowInsetsListener { v, insets ->
    f(v, insets, initialPadding)
    insets
  }
}


data class InitialPadding(
  val left: Int, val top: Int,
  val right: Int, val bottom: Int
)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
  view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)