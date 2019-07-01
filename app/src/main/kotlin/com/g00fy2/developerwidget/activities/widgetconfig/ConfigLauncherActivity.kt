package com.g00fy2.developerwidget.activities.widgetconfig

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class ConfigLauncherActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    startActivity(Intent(this, WidgetConfigActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    })
    finish()
  }
}