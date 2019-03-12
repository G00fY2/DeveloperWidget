package com.g00fy2.developerwidget.activities.appmanager

import android.text.Editable
import com.g00fy2.developerwidget.base.BaseContract

interface AppsContract {

  interface AppsView : BaseContract.BaseView {

    fun toggleResultView(installedAppPackages: List<AppInfo>, filters: List<String>)

    fun updateAppFilter(filters: List<String>)

    fun updateAppFilter(filter: String)

    fun updateFilterIcon(filterActive: Boolean)
  }

  interface AppsPresenter : BaseContract.BasePresenter {

    fun openAppSettingsActivity(packageName: String?)

    fun duplicateFilter(filter: String): Boolean

    fun addAppFilter(filter: String)

    fun removeAppFilter(filter: String)

    fun getCurrentFilter(): List<String>

    fun updateFilter(s: Editable?)
  }
}

