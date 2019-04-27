package com.g00fy2.developerwidget.activities.about

import com.g00fy2.developerwidget.base.BaseContract
import com.g00fy2.developerwidget.base.HasThemeDelegate

interface AboutContract {

  interface AboutView : BaseContract.BaseView {

    fun updateThemeToggleView()
  }

  interface AboutPresenter : BaseContract.BasePresenter {

    fun openUrl(url: String)

    fun sendFeedbackMail()

    fun toggleDayNightMode(delegate: HasThemeDelegate)

    fun honorClicking()
  }
}