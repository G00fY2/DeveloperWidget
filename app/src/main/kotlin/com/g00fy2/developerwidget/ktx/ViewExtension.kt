package com.g00fy2.developerwidget.ktx

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.TypedValue
import android.view.View

fun View.addRipple(asForeground: Boolean = false) {
  TypedValue().apply { context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true) }
    .resourceId.let {
    if (VERSION.SDK_INT < VERSION_CODES.M || !asForeground) setBackgroundResource(it) else foreground =
      context.getDrawable(it)
  }
}