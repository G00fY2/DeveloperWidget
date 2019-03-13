package com.g00fy2.developerwidget.activities.widgetconfig

import com.g00fy2.developerwidget.base.BaseContract
import com.g00fy2.developerwidget.data.DeviceDataItem

interface WidgetConfigContract {

  interface WidgetConfigView : BaseContract.BaseView {

    fun showDeviceData(data: List<Pair<String, DeviceDataItem>>)
  }

  interface WidgetConfigPresenter : BaseContract.BasePresenter {

    fun loadDeviceData()
  }
}