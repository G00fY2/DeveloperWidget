package de.g00fy2.developerwidget.data.devicebuild

import android.os.Build

class BuildDataProvider {

  companion object {

    fun getCombinedDeviceName(): String {
      return if (Build.MODEL.contains(Build.MANUFACTURER, true)) {
        Build.MODEL.capitalize()
      } else {
        Build.MANUFACTURER.capitalize() + " " + Build.MODEL.capitalize()
      }
    }

    fun getBoard() = Build.BOARD

    fun getDevice() = Build.DEVICE

    fun getHardware() = Build.HARDWARE

    fun getProduct() = Build.PRODUCT

    fun getModel() = Build.MODEL

    fun getManufacture() = Build.MANUFACTURER

    fun getBootloader() = Build.BOOTLOADER

    fun getID() = Build.ID

    fun getCodename() = Build.VERSION.CODENAME
  }
}