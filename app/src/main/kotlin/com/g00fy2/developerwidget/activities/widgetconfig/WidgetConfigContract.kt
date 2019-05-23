package com.g00fy2.developerwidget.activities.widgetconfig

import com.g00fy2.developerwidget.base.BaseContract
import com.g00fy2.developerwidget.data.DeviceDataItem

interface WidgetConfigContract {

  interface WidgetConfigView : BaseContract.BaseView {

    fun showDeviceData(data: List<Pair<String, DeviceDataItem>>)

    fun setDeviceTitle(title: String)

    fun setDeviceTitleHint(hint: String)

    fun setSubtitle(data: Pair<String, String>)
  }

  interface WidgetConfigPresenter : BaseContract.BasePresenter {

    fun loadDeviceData()

    fun loadCustomDeviceName()

    fun setCustomDeviceName(deviceName: String, persistent: Boolean = false)
  }
}