package com.g00fy2.developerwidget.activities.about

import android.content.Intent
import androidx.core.net.toUri
import com.g00fy2.developerwidget.base.BasePresenterImpl
import com.g00fy2.developerwidget.base.HasThemeDelegate
import com.g00fy2.developerwidget.controllers.DayNightController
import com.g00fy2.developerwidget.controllers.IntentController
import com.g00fy2.developerwidget.controllers.ToastController
import timber.log.Timber
import javax.inject.Inject

class AboutPresenterImpl @Inject constructor() : BasePresenterImpl(), AboutContract.AboutPresenter {

  @Inject
  lateinit var view: AboutContract.AboutView
  @Inject
  lateinit var intentController: IntentController
  @Inject
  lateinit var toastController: ToastController
  @Inject
  lateinit var dayNightController: DayNightController

  private var clickCount = 0
  private var clickStart: Long = 0

  override fun openUrl(url: String) {
    if (url.startsWith("http", true)) {
      try {
        val uri = url.toUri()
        intentController.startActivity(Intent(Intent.ACTION_VIEW, uri))
      } catch (e: Exception) {
        Timber.d(e)
      }
    }
  }

  override fun sendFeedbackMail() {
    intentController.sendMailToDeveloper()
  }

  override fun toggleDayNightMode(delegate: HasThemeDelegate) {
    dayNightController.toggleMode(delegate)
    view.updateThemeToggleView()
  }

  override fun honorClicking() {
    val current = System.currentTimeMillis()
    if (current - clickStart > 3000) clickCount = 0
    clickCount++
    if (clickCount <= 7) {
      clickStart = current
      toastController.cancelToast()
    }
    if (clickCount in 3..6) {
      val missingSteps = (7 - clickCount)
      toastController.showToast("You are now " + missingSteps + (if (missingSteps > 1) " steps" else " step") + " away from being a developer.")
    } else if (clickCount == 7) {
      toastController.showToast(("\ud83d\udc68\u200d\ud83d\udcbb") + " You are a real developer!")
    }
  }
}