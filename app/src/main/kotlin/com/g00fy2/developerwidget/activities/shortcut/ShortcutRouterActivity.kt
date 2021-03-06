package com.g00fy2.developerwidget.activities.shortcut

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.g00fy2.developerwidget.R

class ShortcutRouterActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    when (intent.getStringExtra("extra_shortcut_id")) {
      "devsettings" -> launchIntent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
      "langsettings" -> launchIntent(android.provider.Settings.ACTION_LOCALE_SETTINGS)
      else -> showShortcutError()
    }
    finish()
  }

  private fun launchIntent(action: String) {
    Intent(action).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }.let {
      if (it.resolveActivity(packageManager) != null) {
        startActivity(it)
      } else {
        showShortcutError()
      }
    }
  }

  private fun showShortcutError() = Toast.makeText(this, getString(R.string.shortcut_error), Toast.LENGTH_SHORT).show()
}