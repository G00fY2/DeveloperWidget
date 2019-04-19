package com.g00fy2.developerwidget.controllers

import android.content.Context
import android.content.Intent
import com.g00fy2.developerwidget.utils.ACTIVITY
import javax.inject.Inject
import javax.inject.Named

class IntentControllerImpl @Inject constructor() : IntentController {

  @Inject
  @field:Named(ACTIVITY)
  lateinit var context: Context

  override fun startActivity(intent: Intent) = context.startActivity(intent)

}