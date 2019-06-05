package com.g00fy2.developerwidget.data

import android.content.Context
import android.util.SparseArray
import com.g00fy2.developerwidget.controllers.WidgetPreferenceControllerImpl
import com.g00fy2.developerwidget.utils.APPLICATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class WidgetsSettingsDataSourceImpl @Inject constructor() : WidgetsSettingsDataSource, CoroutineScope {

  @Inject
  @field:Named(APPLICATION)
  lateinit var context: Context

  private val job: Job by lazy { Job() }

  override val coroutineContext: CoroutineContext = Dispatchers.Main + job

  override suspend fun getCustomDeviceNames(widgetIds: IntArray): SparseArray<String> {
    val customDeviceNames = SparseArray<String>()
    for (widgetId in widgetIds) {
      context.getSharedPreferences(context.packageName + ".preferences_" + widgetId, Context.MODE_PRIVATE)
        .getString(WidgetPreferenceControllerImpl.CUSTOM_DEVICE_NAME, "")?.let {
          if (it.isNotBlank()) customDeviceNames.put(widgetId, it)
        }
    }
    return customDeviceNames
  }

  override fun saveCustomDeviceName(widgetId: Int, customDeviceName: String) =
    context.getSharedPreferences(
      context.packageName + ".preferences_" + widgetId,
      Context.MODE_PRIVATE
    ).edit().putString(WidgetPreferenceControllerImpl.CUSTOM_DEVICE_NAME, customDeviceName.trim()).commit()

  override fun clearWidgetPreferences(widgetId: Int) = context.getSharedPreferences(
    context.packageName + ".preferences_" + widgetId,
    Context.MODE_PRIVATE
  ).edit().clear().apply()
}