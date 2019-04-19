package com.g00fy2.developerwidget.controllers

import android.content.Intent

interface IntentController {

  fun startActivity(intent: Intent)

  fun sendMailToDeveloper()
}