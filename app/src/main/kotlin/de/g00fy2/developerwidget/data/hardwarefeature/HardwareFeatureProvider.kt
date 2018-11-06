package de.g00fy2.developerwidget.data.hardwarefeature

import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.location.LocationManager
import android.nfc.NfcManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

class HardwareFeatureProvider {

  companion object {

    fun hasNFC(context: Context): Boolean {
      return (context.getSystemService(Context.NFC_SERVICE) as NfcManager).defaultAdapter?.let { true } ?: false
    }

    fun hasGPS(context: Context): Boolean {
      return (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).allProviders?.contains(
        LocationManager.GPS_PROVIDER
      ) ?: false
    }

    fun hasBluetooth(context: Context): Boolean {
      return if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter?.let { true } ?: false
      } else {
        BluetoothAdapter.getDefaultAdapter()?.let { true } ?: false
      }
    }
  }
}