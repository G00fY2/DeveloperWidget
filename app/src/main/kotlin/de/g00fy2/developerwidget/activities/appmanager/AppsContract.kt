package de.g00fy2.developerwidget.activities.appmanager

import de.g00fy2.developerwidget.base.BaseContract

interface AppsContract {

  interface AppsView : BaseContract.BaseView {

    fun toggleResultView(installedAppPackages: List<AppInfo>)

    fun updateAppFilter(filter: List<String>)

    fun updateFilterIcon(filterActive: Boolean)
  }

  interface AppsPresenter : BaseContract.BasePresenter {

    fun openAppSettingsActivity(packageName: String?)

    fun duplicateFilter(filter: String): Boolean

    fun addAppFilter(filter: String)

    fun removeAppFilter(filter: String)

    fun getCurrentFilter(): List<String>
  }
}

