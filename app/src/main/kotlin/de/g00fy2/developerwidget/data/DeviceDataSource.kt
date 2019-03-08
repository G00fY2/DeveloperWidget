package de.g00fy2.developerwidget.data

interface DeviceDataSource {

  fun getStaticDeviceData(): Map<String, DeviceDataItem>

  fun getHardwareData(): Map<String, DeviceDataItem>

  fun getSoftwareInfo(): Map<String, DeviceDataItem>

  fun getHeaderItems(): Map<String, DeviceDataItem>
}