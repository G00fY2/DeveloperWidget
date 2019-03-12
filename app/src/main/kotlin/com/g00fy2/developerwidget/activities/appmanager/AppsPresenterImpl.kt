package com.g00fy2.developerwidget.activities.appmanager

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.provider.Settings
import android.text.Editable
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.OnLifecycleEvent
import com.g00fy2.developerwidget.base.BasePresenterImpl
import com.g00fy2.developerwidget.controllers.IntentController
import com.g00fy2.developerwidget.controllers.WidgetPreferenceController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppsPresenterImpl @Inject constructor() : BasePresenterImpl(), AppsContract.AppsPresenter {

  @Inject lateinit var view: AppsContract.AppsView
  @Inject lateinit var appInfoBuilder: AppInfo.AppInfoBuilder
  @Inject lateinit var intentController: IntentController
  @Inject lateinit var widgetPreferenceController: WidgetPreferenceController

  private val appFilter by lazy { widgetPreferenceController.getAppFilters() }

  @OnLifecycleEvent(Event.ON_RESUME)
  fun setFilterIconState() {
    view.updateFilterIcon(appFilter.isNotEmpty())
  }

  @OnLifecycleEvent(Event.ON_RESUME)
  fun scanApps() {
    launch {
      withContext(Dispatchers.IO) {
        getInstalledUserApps()
      }.let {
        view.toggleResultView(it, appFilter)
      }
    }
  }

  override fun openAppSettingsActivity(packageName: String?) {
    packageName?.let {
      Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, "package:$it".toUri()).apply {
        flags = flags or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_SINGLE_TOP
      }.let { intent -> intentController.startActivity(intent) }
    }
  }

  override fun duplicateFilter(filter: String) = appFilter.contains(filter)

  override fun addAppFilter(filter: String) {
    appFilter.add(filter)
    widgetPreferenceController.saveAppFilters(appFilter)
    view.updateAppFilter(appFilter)
    view.updateFilterIcon(appFilter.isNotEmpty())
  }

  override fun removeAppFilter(filter: String) {
    appFilter.remove(filter)
    widgetPreferenceController.saveAppFilters(appFilter)
    view.updateAppFilter(appFilter)
    view.updateFilterIcon(appFilter.isNotEmpty())
  }

  override fun getCurrentFilter(): List<String> = appFilter

  override fun updateFilter(s: Editable?) {
    if (s.isNullOrEmpty()) {
      view.updateAppFilter(appFilter)
      view.updateFilterIcon(appFilter.isNotEmpty())
    } else {
      view.updateAppFilter(s.toString())
      view.updateFilterIcon(true)
    }
  }

  private fun getInstalledUserApps(): List<AppInfo> {
    return appInfoBuilder.getInstalledPackages()
      .filter { it.applicationInfo.flags.let { flags -> flags and ApplicationInfo.FLAG_SYSTEM == 0 || flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0 } }
      .map { appInfoBuilder.build(it) }
      .sorted()
      .toList()
  }
}