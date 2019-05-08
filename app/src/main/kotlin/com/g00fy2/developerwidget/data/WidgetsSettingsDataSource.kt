package com.g00fy2.developerwidget.data

import android.util.SparseArray

interface WidgetsSettingsDataSource {

  suspend fun getCustomDeviceNames(widgetIds: IntArray): SparseArray<String>
}