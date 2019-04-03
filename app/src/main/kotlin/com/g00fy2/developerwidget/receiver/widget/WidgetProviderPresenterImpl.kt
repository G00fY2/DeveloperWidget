package com.g00fy2.developerwidget.receiver.widget

import com.g00fy2.developerwidget.data.DeviceDataSource
import com.g00fy2.developerwidget.receiver.widget.WidgetProviderContract.WidgetProvider
import com.g00fy2.developerwidget.receiver.widget.WidgetProviderContract.WidgetProviderPresenter
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class WidgetProviderPresenterImpl @Inject constructor() : WidgetProviderPresenter, CoroutineScope {

  @Inject lateinit var widgetProvider: WidgetProvider
  @Inject lateinit var deviceDataSource: DeviceDataSource

  private val job: Job by lazy { Job() }

  override val coroutineContext: CoroutineContext = Dispatchers.Main + job

  override fun getDeviceData() {
    launch {
      withContext(Dispatchers.IO) {
        deviceDataSource.getStaticDeviceData()
      }.let {
        widgetProvider.updateWidgetData(it)
      }
    }
  }
}