package com.g00fy2.developerwidget.ktx

import android.graphics.Insets
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.WindowInsets
import androidx.annotation.RequiresApi

@Suppress("DEPRECATION")
val WindowInsets.systemGestureInsetsCompat: Insets
  @RequiresApi(VERSION_CODES.Q)
  get() = if (VERSION.SDK_INT >= VERSION_CODES.R) {
    getInsetsIgnoringVisibility(WindowInsets.Type.systemGestures())
  } else {
    systemGestureInsets
  }

@Suppress("DEPRECATION")
val WindowInsets.systemWindowInsetTopVisibleCompat: Int
  @RequiresApi(VERSION_CODES.Q)
  get() = if (VERSION.SDK_INT >= VERSION_CODES.R) {
    getInsets(WindowInsets.Type.systemBars()).top
  } else {
    systemWindowInsetTop
  }

@Suppress("DEPRECATION")
val WindowInsets.systemWindowInsetBottomCompat: Int
  @RequiresApi(VERSION_CODES.Q)
  get() = if (VERSION.SDK_INT >= VERSION_CODES.R) {
    getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).bottom
  } else {
    systemWindowInsetBottom
  }