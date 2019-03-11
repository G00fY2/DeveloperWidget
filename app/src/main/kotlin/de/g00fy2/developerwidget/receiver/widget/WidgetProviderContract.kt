package de.g00fy2.developerwidget.receiver.widget

import de.g00fy2.developerwidget.data.DeviceDataItem

interface WidgetProviderContract {

  interface WidgetProvider {

    fun updateWidgetData(data: Map<String, DeviceDataItem>)
  }

  interface WidgetProviderPresenter {

    fun getDeviceData()
  }
}