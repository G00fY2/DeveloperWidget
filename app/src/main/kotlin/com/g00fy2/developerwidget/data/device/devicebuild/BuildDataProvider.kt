package com.g00fy2.developerwidget.data.device.devicebuild

import android.annotation.SuppressLint
import android.os.Build

object BuildDataProvider {

  @SuppressLint("DefaultLocale")
  fun getCombinedDeviceName(): String {
    return if (Build.MODEL.contains(Build.MANUFACTURER, true)) {
      Build.MODEL.capitalize()
    } else {
      Build.MANUFACTURER.capitalize() + " " + Build.MODEL.capitalize()
    }
  }

  fun getBoard(): String = Build.BOARD

  fun getDevice(): String = Build.DEVICE

  fun getHardware(): String = Build.HARDWARE

  fun getProduct(): String = Build.PRODUCT

  fun getModel(): String = Build.MODEL

  fun getManufacture(): String = Build.MANUFACTURER

  fun getBootloader(): String = Build.BOOTLOADER

  fun getID(): String = Build.ID
}