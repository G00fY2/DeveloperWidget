package com.g00fy2.developerwidget.data

import android.util.SparseArray

interface WidgetsPreferencesDataSource {

  suspend fun getCustomDeviceNames(widgetIds: IntArray): SparseArray<String>

  fun saveCustomDeviceName(widgetId: Int, customDeviceName: String): Boolean

  fun clearWidgetPreferences(widgetId: Int)
}