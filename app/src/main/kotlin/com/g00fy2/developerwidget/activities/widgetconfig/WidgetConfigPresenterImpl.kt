package com.g00fy2.developerwidget.activities.widgetconfig

import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.OnLifecycleEvent
import com.g00fy2.developerwidget.base.BasePresenterImpl
import com.g00fy2.developerwidget.controllers.StringController
import com.g00fy2.developerwidget.data.DeviceDataItem
import com.g00fy2.developerwidget.data.DeviceDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WidgetConfigPresenterImpl @Inject constructor() : BasePresenterImpl(),
  WidgetConfigContract.WidgetConfigPresenter {

  @Inject
  lateinit var view: WidgetConfigContract.WidgetConfigView
  @Inject
  lateinit var deviceDataSource: DeviceDataSource
  @Inject
  lateinit var stringController: StringController

  @OnLifecycleEvent(Event.ON_RESUME)
  override fun loadDeviceData() {
    launch {
      withContext(Dispatchers.IO) {
        getDeviceData()
      }.let {
        view.showDeviceData(it)
      }
    }
  }

  private suspend fun getDeviceData(): List<Pair<String, DeviceDataItem>> {
    return deviceDataSource
      .getStaticDeviceData()
      .plus(deviceDataSource.getHardwareData())
      .plus(deviceDataSource.getSoftwareInfo())
      .plus(deviceDataSource.getHeaderItems())
      .toList()
      .filter { (_, value) -> value.value.isNotBlank() || value.isHeader }
      .sortedWith(
        compareBy(
          { it.second.category.ordinal },
          { !it.second.isHeader },
          { stringController.getString(it.second.title) })
      )
  }
}