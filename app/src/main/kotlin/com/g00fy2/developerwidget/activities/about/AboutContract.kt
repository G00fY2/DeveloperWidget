package com.g00fy2.developerwidget.activities.about

import com.g00fy2.developerwidget.base.BaseContract

interface AboutContract {

  interface AboutView : BaseContract.BaseView

  interface AboutPresenter : BaseContract.BasePresenter {

    fun openUrl(url: String)

    fun showFeedbackOptions()

    fun honorClicking()
  }
}