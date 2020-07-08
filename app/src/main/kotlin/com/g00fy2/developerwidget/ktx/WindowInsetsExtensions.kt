package com.g00fy2.developerwidget.ktx

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.WindowInsets
import androidx.annotation.RequiresApi

val WindowInsets.systemWindowInsetVisibleTopCompat: Int
  @RequiresApi(VERSION_CODES.Q)
  get() = if (VERSION.SDK_INT >= VERSION_CODES.R) {
    getInsets(WindowInsets.Type.systemBars()).top
  } else {
    @Suppress("DEPRECATION")
    systemWindowInsetTop
  }

val WindowInsets.systemWindowInsetIgnoringVisibilityBottomCompat: Int
  @RequiresApi(VERSION_CODES.Q)
  get() = if (VERSION.SDK_INT >= VERSION_CODES.R) {
    getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).bottom
  } else {
    @Suppress("DEPRECATION")
    systemWindowInsetBottom
  }