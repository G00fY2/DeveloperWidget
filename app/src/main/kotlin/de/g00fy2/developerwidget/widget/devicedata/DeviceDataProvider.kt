package de.g00fy2.developerwidget.widget.devicedata

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.Point
import android.location.LocationManager
import android.nfc.NfcManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.util.DisplayMetrics
import android.webkit.WebView
import com.g00fy2.versioncompare.Version
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.widget.devicedata.DeviceDataItem.Category

class DeviceDataProvider {

  companion object {

    fun staticDeviceData(): Map<String, DeviceDataItem> {
      val data = HashMap<String, DeviceDataItem>()

      data[BOARD] = DeviceDataItem(Build.BOARD, R.string.board, Category.DEVICE)
      data[DEVICE] = DeviceDataItem(Build.DEVICE, R.string.device, Category.DEVICE)
      data[HARDWARE] = DeviceDataItem(Build.HARDWARE, R.string.hardware, Category.DEVICE)
      data[PRODUCT] = DeviceDataItem(Build.PRODUCT, R.string.product, Category.DEVICE)

      data[MODEL] = DeviceDataItem(Build.MODEL, R.string.model, Category.DEVICE)
      data[MANUFACTURE] = DeviceDataItem(Build.MANUFACTURER, R.string.manufacture, Category.DEVICE)

      val deviceName = if (Build.MODEL.contains(Build.MANUFACTURER)) {
        Build.MODEL.capitalize()
      } else {
        Build.MANUFACTURER.capitalize() + " " + Build.MODEL.capitalize()
      }
      data[DEVICE_NAME] = DeviceDataItem(deviceName, R.string.device_name, Category.DEVICE)

      data[BOOTLOADER] = DeviceDataItem(Build.BOOTLOADER, R.string.bootloader, Category.DEVICE)
      data[ID] = DeviceDataItem(Build.ID, R.string.id, Category.DEVICE)

      if (SDK_INT >= VERSION_CODES.LOLLIPOP) {
        data[ABIS32] =
            DeviceDataItem(Build.SUPPORTED_32_BIT_ABIS.joinToString(), R.string.abis32, Category.DEVICE)
        data[ABIS64] =
            DeviceDataItem(Build.SUPPORTED_64_BIT_ABIS.joinToString(), R.string.abis64, Category.DEVICE)
        data[ABIS] = DeviceDataItem(Build.SUPPORTED_ABIS.joinToString(), R.string.abis, Category.DEVICE)
      } else {
        @Suppress("DEPRECATION")
        data[ABIS] = DeviceDataItem(Build.CPU_ABI + ", " + Build.CPU_ABI2, R.string.abis, Category.DEVICE)
      }

      data[CODENAME] = DeviceDataItem(Build.VERSION.CODENAME, R.string.codename, Category.SYSTEM)

      if (SDK_INT >= VERSION_CODES.M) {
        data[PREVIEW_SDK] =
            DeviceDataItem(Build.VERSION.PREVIEW_SDK_INT.toString(), R.string.preview_sdk, Category.SYSTEM)
        data[SECURITY_PATCH_LEVEL] =
            DeviceDataItem(Build.VERSION.SECURITY_PATCH, R.string.security_patch_level, Category.SYSTEM)
      }

      data[RELEASE] = DeviceDataItem(Build.VERSION.RELEASE, R.string.release, Category.SYSTEM)
      data[SDK] = DeviceDataItem(Build.VERSION.SDK_INT.toString(), R.string.sdk, Category.SYSTEM)

      val vmVersion = System.getProperty("java.vm.version") // 2.1.0
      val vmName = if (Version(vmVersion).isAtLeast("2")) "ART" else "Dalvik"
      data[VM_VERSION] = DeviceDataItem("$vmName $vmVersion", R.string.vm_version, Category.SYSTEM)

      System.getProperty("os.version")
        ?.let { data[KERNEL] = DeviceDataItem(it, R.string.kernel, Category.SYSTEM) }

      return data
    }

    fun getHardwareData(activity: Activity): Map<String, DeviceDataItem> {
      val data = HashMap<String, DeviceDataItem>()

      val display = activity.windowManager.defaultDisplay
      val size = Point()
      val displayMetric = DisplayMetrics()
      if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
        display.getRealSize(size)
        display.getRealMetrics(displayMetric)
      } else {
        display.getSize(size)
        display.getMetrics(displayMetric)
      }

      data[RESOLUTION] = DeviceDataItem(size.x.toString() + " x " + size.y, R.string.resolution, Category.DISPLAY)

      val gcd = gcd(size.x, size.y)
      val ratio =
        if (size.x < size.y) (size.y / gcd).toString() + ":" + (size.x / gcd) else (size.x / gcd).toString() + ":" + (size.y / gcd)

      data[RATIO] = DeviceDataItem(ratio, R.string.ratio, Category.DISPLAY)
      data[DPI] =
          DeviceDataItem(displayMetric.ydpi.toString() + "/" + displayMetric.ydpi, R.string.dpi, Category.DISPLAY)

      if (SDK_INT >= VERSION_CODES.JELLY_BEAN) {
        val memInfo = ActivityManager.MemoryInfo()
        (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memInfo)
        val ram = 8 * (Math.round(memInfo.totalMem / (1024.0 * 1024.0) / 8))
        data[RAM] = DeviceDataItem(ram.toString(), R.string.ram, Category.MEMORY)
      }

      data[BLUETOOTH] = DeviceDataItem(hasBluetooth(activity).toString(), R.string.bluetooth, Category.FEATURES)
      data[GPS] = DeviceDataItem(hasGPS(activity).toString(), R.string.gps, Category.FEATURES)
      data[NFC] = DeviceDataItem(hasNFC(activity).toString(), R.string.nfc, Category.FEATURES)
      data[LOW_RAM_FLAG] = DeviceDataItem(hasLowRamFlag(activity).toString(), R.string.low_ram_flag, Category.MEMORY)

      return data
    }

    fun getSoftwareInfo(context: Context): Map<String, DeviceDataItem> {
      val data = HashMap<String, DeviceDataItem>()

      data[GOOGLE_PLAY_VERSION] =
          DeviceDataItem(getGooglePlayServicesVersion(context), R.string.google_play_version, Category.SOFTWARE)
      data[WEBVIEW_IMPLEMENTATION] =
          DeviceDataItem(getWebViewImplementation(context), R.string.webview_implementation, Category.SOFTWARE)

      return data
    }

    private fun getGooglePlayServicesVersion(context: Context): String {
      return try {
        context.packageManager.getPackageInfo("com.google.android.gms", 0).versionName
      } catch (e: NameNotFoundException) {
        "not installed"
      }
    }

    @SuppressLint("PrivateApi")
    private fun getWebViewImplementation(context: Context): String {
      return when {
        VERSION.SDK_INT >= VERSION_CODES.O -> WebView.getCurrentWebViewPackage()
        VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP -> {
          try {
            val webViewFactory = Class.forName("android.webkit.WebViewFactory")
            val method = webViewFactory.getMethod("getLoadedPackageInfo")
            method.invoke(null, null) as PackageInfo
          } catch (t: Throwable) {
            null
          }
        }
        else -> context.packageManager.getPackageInfo("com.google.android.webview", 0)
      }?.let { context.packageManager.getApplicationLabel(it.applicationInfo).toString() + " " + it.versionName }
        ?: ""
    }

    private fun hasNFC(context: Context): Boolean {
      return (context.getSystemService(Context.NFC_SERVICE) as NfcManager).defaultAdapter?.let { true } ?: false
    }

    private fun hasGPS(context: Context): Boolean {
      return (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).allProviders?.contains(
        LocationManager.GPS_PROVIDER
      ) ?: false
    }

    private fun hasBluetooth(context: Context): Boolean {
      return if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter?.let { true } ?: false
      } else {
        BluetoothAdapter.getDefaultAdapter()?.let { true } ?: false
      }
    }

    private fun hasLowRamFlag(context: Context): Boolean {
      return if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
        (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).isLowRamDevice
      } else {
        false
      }
    }

    private fun gcd(p: Int, q: Int): Int {
      return if (q == 0)
        p
      else
        gcd(q, p % q)
    }

    val BOARD = "BOARD"
    val DEVICE = "DEVICE"
    val HARDWARE = "HARDWARE"
    val PRODUCT = "PRODUCT"
    val MODEL = "MODEL"
    val MANUFACTURE = "MANUFACTURE"
    val DEVICE_NAME = "DEVICE_NAME"
    val BOOTLOADER = "BOOTLOADER"
    val ID = "ID"
    val ABIS32 = "ABIS32"
    val ABIS64 = "ABIS64"
    val ABIS = "ABIS"
    val CODENAME = "CODENAME"
    val PREVIEW_SDK = "PREVIEW_SDK"
    val SECURITY_PATCH_LEVEL = "SECURITY_PATCH_LEVEL"
    val RELEASE = "RELEASE"
    val SDK = "SDK"
    val VM_VERSION = "VM_VERSION"
    val KERNEL = "KERNEL"
    val RESOLUTION = "RESOLUTION"
    val RATIO = "RATIO"
    val DPI = "DPI"
    val RAM = "RAM"
    val NFC = "NFC"
    val GPS = "GPS"
    val BLUETOOTH = "BLUETOOTH"
    val LOW_RAM_FLAG = "LOW_RAM_FLAG"
    val GOOGLE_PLAY_VERSION = "GOOGLE_PLAY_VERSION"
    val WEBVIEW_IMPLEMENTATION = "WEBVIEW_IMPLEMENTATION"
  }
}