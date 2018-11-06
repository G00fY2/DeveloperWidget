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
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.util.regex.Pattern

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
      data[DEVICE_NAME] = DeviceDataItem(getCombinedDeviceName(), R.string.device_name, Category.DEVICE)

      data[BOOTLOADER] = DeviceDataItem(Build.BOOTLOADER, R.string.bootloader, Category.DEVICE)
      data[ID] = DeviceDataItem(Build.ID, R.string.id, Category.DEVICE)

      data[ABIS] = DeviceDataItem(getPrimaryABI(), R.string.abis, Category.DEVICE)
      data[CODENAME] = DeviceDataItem(Build.VERSION.CODENAME, R.string.codename, Category.SYSTEM)

      data[RELEASE] = DeviceDataItem(Build.VERSION.RELEASE, R.string.release, Category.SYSTEM)
      data[SDK] = DeviceDataItem(Build.VERSION.SDK_INT.toString(), R.string.sdk, Category.SYSTEM)

      data[VM_VERSION] = DeviceDataItem(getVMVersion(), R.string.vm_version, Category.SYSTEM)

      if (SDK_INT >= VERSION_CODES.M) {
        data[PREVIEW_SDK] =
            DeviceDataItem(Build.VERSION.PREVIEW_SDK_INT.toString(), R.string.preview_sdk, Category.SYSTEM)
        data[SECURITY_PATCH_LEVEL] =
            DeviceDataItem(Build.VERSION.SECURITY_PATCH, R.string.security_patch_level, Category.SYSTEM)
      }

      System.getProperty("os.version")
        ?.let { data[KERNEL] = DeviceDataItem(it, R.string.kernel, Category.SYSTEM) }

      return data
    }

    fun getHardwareData(activity: Activity): Map<String, DeviceDataItem> {
      val data = HashMap<String, DeviceDataItem>()

      val res = getResolution(activity)
      data[RESOLUTION] = DeviceDataItem(res.x.toString() + " x " + res.y, R.string.resolution, Category.DISPLAY)
      data[RATIO] = DeviceDataItem(getDisplayRatio(res), R.string.ratio, Category.DISPLAY)

      val dpi = geDisplayDpi(activity)
      data[DPI] = DeviceDataItem(dpi.x.toString() + "/" + dpi.y, R.string.dpi, Category.DISPLAY)

      data[RAM] = DeviceDataItem(getTotalRam(activity), R.string.ram, Category.MEMORY)
      data[CPU_CORES] = DeviceDataItem(getCPUCoreNum().toString(), R.string.cpu_cores, Category.DEVICE)

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

    private fun getVMVersion(): String {
      val vmVersion = System.getProperty("java.vm.version")
      val vmName = if (Version(vmVersion).isAtLeast("2")) "ART" else "Dalvik"
      return "$vmName $vmVersion"
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

    private fun getResolution(activity: Activity): Point {
      val display = activity.windowManager.defaultDisplay
      val size = Point()
      if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
        display.getRealSize(size)
      } else {
        display.getSize(size)
      }
      return size
    }

    private fun geDisplayDpi(activity: Activity): Point {
      val display = activity.windowManager.defaultDisplay
      val displayMetric = DisplayMetrics()
      if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
        display.getRealMetrics(displayMetric)
      } else {
        display.getMetrics(displayMetric)
      }
      return Point(Math.round(displayMetric.xdpi), Math.round(displayMetric.ydpi))
    }

    private fun getDisplayRatio(resolution: Point): String {
      val gcd = gcd(resolution.x, resolution.y)
      return if (resolution.x < resolution.y) {
        (resolution.y / gcd).toString() + ":" + (resolution.x / gcd)
      } else {
        (resolution.x / gcd).toString() + ":" + (resolution.y / gcd)
      }
    }

    private fun getTotalRam(context: Context): String {
      if (SDK_INT >= VERSION_CODES.JELLY_BEAN) {
        val memInfo = ActivityManager.MemoryInfo()
        (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memInfo)
        return (8 * (Math.round(memInfo.totalMem / (1024.0 * 1024.0) / 8))).toString()
      } else {
        try {
          RandomAccessFile("/proc/meminfo", "r").use {
            val matcher = Pattern.compile("(\\d+)").matcher(it.readLine())
            var memTotal: String? = null
            while (matcher.find()) memTotal = matcher.group(1)
            memTotal?.let { value ->
              return (8 * (Math.round(value.toLong() / 1024.0 / 8))).toString()
            }
          }
        } catch (e: IOException) {
          e.printStackTrace()
        }
        return ""
      }
    }

    private fun getPrimaryABI(): String {
      return if (SDK_INT >= VERSION_CODES.LOLLIPOP) {
        Build.SUPPORTED_ABIS.firstOrNull() ?: ""
      } else {
        @Suppress("DEPRECATION")
        Build.CPU_ABI
      }
    }

    private fun getCPUCoreNum(): Int {
      val pattern = Pattern.compile("cpu[0-9]+")
      return Math.max(
        File("/sys/devices/system/cpu/")
          .walk()
          .count { pattern.matcher(it.name).matches() },
        Runtime.getRuntime().availableProcessors()
      )
    }

    // Todo make use of
    fun getGroupedCPUCoreFrequencies(): Map<String, Int> {
      val pattern = Pattern.compile("cpu[0-9]+/cpufreq/(cpuinfo_max_freq|cpuinfo_min_freq)")
      return File("/sys/devices/system/cpu/")
        .walk()
        .sorted()
        .filter { pattern.matcher(it.path).find() }
        .map { RandomAccessFile(it.path, "r").use { reader -> (reader.readLine().toLong() / 1000).toInt() } }
        .windowed(2, 2)
        .map { Pair(it[0], it[1]) }
        .toList()
        .sortedByDescending { it.first }
        .groupingBy { it.second.toString() + "MHz - " + it.first + "MHz" }
        .eachCount()
    }

    private fun getCombinedDeviceName(): String {
      return if (Build.MODEL.contains(Build.MANUFACTURER, true)) {
        Build.MODEL.capitalize()
      } else {
        Build.MANUFACTURER.capitalize() + " " + Build.MODEL.capitalize()
      }
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
    val CPU_CORES = "CPU_CORES"
    val NFC = "NFC"
    val GPS = "GPS"
    val BLUETOOTH = "BLUETOOTH"
    val LOW_RAM_FLAG = "LOW_RAM_FLAG"
    val GOOGLE_PLAY_VERSION = "GOOGLE_PLAY_VERSION"
    val WEBVIEW_IMPLEMENTATION = "WEBVIEW_IMPLEMENTATION"
  }
}