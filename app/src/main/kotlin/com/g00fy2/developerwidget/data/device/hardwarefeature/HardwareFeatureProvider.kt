package com.g00fy2.developerwidget.data.device.hardwarefeature

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.location.LocationManager
import android.nfc.NfcManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.content.getSystemService

object HardwareFeatureProvider {

  fun hasNFC(context: Context): Boolean =
    context.getSystemService<NfcManager>()?.defaultAdapter?.let { true } ?: false

  fun hasGPS(context: Context): Boolean {
    return context.getSystemService<LocationManager>()?.allProviders?.contains(
      LocationManager.GPS_PROVIDER
    ) ?: false
  }

  fun hasBluetooth(context: Context): Boolean {
    return if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
      context.getSystemService<BluetoothManager>()?.adapter?.let { true } ?: false
    } else {
      @Suppress("DEPRECATION")
      BluetoothAdapter.getDefaultAdapter()?.let { true } ?: false
    }
  }
}