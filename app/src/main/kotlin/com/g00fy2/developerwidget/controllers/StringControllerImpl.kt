package com.g00fy2.developerwidget.controllers

import android.content.Context
import com.g00fy2.developerwidget.utils.ACTIVITY
import javax.inject.Inject
import javax.inject.Named

class StringControllerImpl @Inject constructor() : StringController {

  @Inject
  @field:Named(ACTIVITY)
  lateinit var context: Context

  override fun getString(resId: Int): String = context.getString(resId)

}