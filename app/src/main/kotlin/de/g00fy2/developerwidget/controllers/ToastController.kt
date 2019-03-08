package de.g00fy2.developerwidget.controllers

import androidx.annotation.StringRes

interface ToastController {

  fun showToast(message: String)

  fun showToast(@StringRes resId: Int)

  fun cancelToast()
}