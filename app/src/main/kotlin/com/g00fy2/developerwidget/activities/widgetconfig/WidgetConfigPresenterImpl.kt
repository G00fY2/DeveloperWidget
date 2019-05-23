package com.g00fy2.developerwidget.activities.widgetconfig

import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.OnLifecycleEvent
import com.g00fy2.developerwidget.base.BasePresenterImpl
import com.g00fy2.developerwidget.controllers.StringController
import com.g00fy2.developerwidget.controllers.WidgetPreferenceController
import com.g00fy2.developerwidget.data.DeviceDataItem
import com.g00fy2.developerwidget.data.DeviceDataSource
import com.g00fy2.developerwidget.data.devicebuild.BuildDataProvider
import com.g00fy2.developerwidget.data.system.SystemDataProvider
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
  @Inject
  lateinit var widgetPreferenceController: WidgetPreferenceController
  private val defaultDeviceName by lazy { BuildDataProvider.getCombinedDeviceName() }

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

  @OnLifecycleEvent(Event.ON_RESUME)
  override fun loadCustomDeviceName() {
    launch {
      withContext(Dispatchers.IO) {
        widgetPreferenceController.getCustomDeviceName()
      }.let {
        if (it.isNotEmpty()) {
          view.setDeviceTitle(it)
        } else {
          view.setDeviceTitle(defaultDeviceName)
        }
        view.setSubtitle(SystemDataProvider.getVersionAndSDK())
        view.setDeviceTitleHint(defaultDeviceName)
      }
    }
  }

  override fun setCustomDeviceName(deviceName: String, persistent: Boolean) {
    if (deviceName.isNotEmpty()) {
      view.setDeviceTitle(deviceName)
    } else {
      view.setDeviceTitle(defaultDeviceName)
    }
    if (persistent) {
      if (deviceName == defaultDeviceName) {
        widgetPreferenceController.saveCustomDeviceName("")
      } else {
        widgetPreferenceController.saveCustomDeviceName(deviceName)
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
      .filter { (_, value) -> (value.value.isNotBlank() || value.isHeader) && !value.value.equals("unknown", true) }
      .sortedWith(
        compareBy(
          { it.second.category.ordinal },
          { !it.second.isHeader },
          { stringController.getString(it.second.title) })
      )
  }
}