package com.g00fy2.developerwidget.data.device.devicebuild

import android.os.Build
import java.util.Locale

object BuildDataProvider {

  fun getCombinedDeviceName(): String {
    return if (Build.MODEL.contains(Build.MANUFACTURER, true)) {
      Build.MODEL.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    } else {
      Build.MANUFACTURER.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
      } + " " + Build.MODEL.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
      }
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