package de.g00fy2.developerwidget.activities.about

import de.g00fy2.developerwidget.base.BasePresenterImpl
import javax.inject.Inject

class AboutPresenterImpl @Inject constructor() : BasePresenterImpl(), AboutContract.AboutPresenter {

  @Inject lateinit var view: AboutContract.AboutView

}