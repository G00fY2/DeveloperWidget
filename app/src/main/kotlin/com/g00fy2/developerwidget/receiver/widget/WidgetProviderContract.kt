package com.g00fy2.developerwidget.receiver.widget

import android.util.SparseArray
import com.g00fy2.developerwidget.data.DeviceDataItem

interface WidgetProviderContract {

  interface WidgetProvider {

    fun updateWidgetData(data: Map<String, DeviceDataItem>, customDeviceNames: SparseArray<String>)
  }

  interface WidgetProviderPresenter {

    fun getDeviceData(widgetIDs: IntArray)
  }
}