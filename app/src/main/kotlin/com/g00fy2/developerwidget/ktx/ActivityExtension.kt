package com.g00fy2.developerwidget.ktx

import android.app.Activity
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

fun Activity.gesturalNavigationMode(): Boolean {
  return if (VERSION.SDK_INT >= VERSION_CODES.Q) {
    window.decorView.rootWindowInsets.systemGestureInsets.left > 0
  } else {
    false
  }
}