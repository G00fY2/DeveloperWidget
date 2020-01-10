package com.g00fy2.developerwidget.controllers

import android.content.Context
import android.widget.Toast
import com.g00fy2.developerwidget.di.annotations.APPLICATION
import javax.inject.Inject
import javax.inject.Named

class ToastControllerImpl @Inject constructor() : ToastController {

  @Inject
  @Named(APPLICATION)
  lateinit var context: Context
  private var toast: Toast? = null

  override fun showToast(message: String) {
    toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
    toast?.show()
  }

  override fun showToast(resId: Int) {
    toast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT)
    toast?.show()
  }

  override fun cancelToast() {
    toast?.cancel()
  }
}