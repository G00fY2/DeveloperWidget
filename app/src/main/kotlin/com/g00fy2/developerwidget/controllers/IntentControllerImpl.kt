package com.g00fy2.developerwidget.controllers

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.g00fy2.developerwidget.BuildConfig
import com.g00fy2.developerwidget.utils.ACTIVITY
import com.g00fy2.developerwidget.utils.DEVELOPER_EMAIL
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class IntentControllerImpl @Inject constructor() : IntentController {

  @Inject
  @field:Named(ACTIVITY)
  lateinit var context: Context

  override fun startActivity(intent: Intent) {
    if (intent.resolveActivity(context.packageManager) != null) {
      context.startActivity(intent)
    } else {
      Timber.w("Intent could not get resolved.")
    }
  }

  override fun sendMailToDeveloper() {
    startActivity(Intent(Intent.ACTION_SENDTO).apply {
      data = Uri.parse("mailto:")
      putExtra(Intent.EXTRA_EMAIL, arrayOf(DEVELOPER_EMAIL))
      putExtra(
        Intent.EXTRA_SUBJECT,
        "Feedback [" + BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE + "]"
      )
    })
  }

}