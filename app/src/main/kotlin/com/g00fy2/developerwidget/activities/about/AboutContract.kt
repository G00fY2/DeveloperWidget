package com.g00fy2.developerwidget.activities.about

import com.g00fy2.developerwidget.base.BaseContract

interface AboutContract {

  interface AboutView : BaseContract.BaseView {

    fun updateThemeToggleView()

    fun updateSearchDepthUi(depth: Int)
  }

  interface AboutPresenter : BaseContract.BasePresenter {

    fun openUrl(url: String)

    fun sendFeedbackMail()

    fun toggleDayNightMode()

    fun honorClicking()

    fun showRebootNotice()

    fun updateSearchDepth()

    fun saveSearchDepth(depth: Int)
  }
}