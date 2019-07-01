package com.g00fy2.developerwidget.data

interface DeviceDataSource {

  suspend fun getStaticDeviceData(): Map<String, DeviceDataItem>

  suspend fun getHardwareData(): Map<String, DeviceDataItem>

  suspend fun getSoftwareInfo(): Map<String, DeviceDataItem>

  suspend fun getHeaderItems(): Map<String, DeviceDataItem>

  fun getCombinedDeviceName(): String

  fun getVersionAndSDK(): Pair<String, String>
}