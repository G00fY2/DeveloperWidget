package com.g00fy2.developerwidget.activities.shortcut

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.g00fy2.developerwidget.R

class ShortcutRouter : Activity() {

  override fun onResume() {
    super.onResume()
    when (intent.getStringExtra("shortcut_id")) {
      "devsettings" -> startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
      })
      "langsettings" -> startActivity(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
      })
      else -> Toast.makeText(this, getString(R.string.shortcut_error), Toast.LENGTH_SHORT).show()
    }
    finish()
  }
}