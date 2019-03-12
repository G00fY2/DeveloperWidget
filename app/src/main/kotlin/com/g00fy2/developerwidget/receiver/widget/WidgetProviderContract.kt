package com.g00fy2.developerwidget.receiver.widget

import com.g00fy2.developerwidget.data.DeviceDataItem

interface WidgetProviderContract {

  interface WidgetProvider {

    fun updateWidgetData(data: Map<String, DeviceDataItem>)
  }

  interface WidgetProviderPresenter {

    fun getDeviceData()
  }
}