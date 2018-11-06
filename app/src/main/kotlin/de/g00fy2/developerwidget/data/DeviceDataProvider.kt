package de.g00fy2.developerwidget.data

import android.app.Activity
import android.content.Context
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.data.DeviceDataItem.Category
import de.g00fy2.developerwidget.data.build.BuildDataProvider
import de.g00fy2.developerwidget.data.cpu.CPUDataProvider
import de.g00fy2.developerwidget.data.display.DisplayDataProvider
import de.g00fy2.developerwidget.data.hardwarefeature.HardwareFeatureProvider
import de.g00fy2.developerwidget.data.ram.RamDataProvider
import de.g00fy2.developerwidget.data.system.SystemDataProvider
import de.g00fy2.developerwidget.data.systemapps.SystemAppsDataProvider

class DeviceDataProvider {

  companion object {

    fun staticDeviceData(): Map<String, DeviceDataItem> {
      val data = HashMap<String, DeviceDataItem>()

      data[BOARD] = DeviceDataItem(BuildDataProvider.getBoard(), R.string.board, Category.DEVICE)
      data[DEVICE] = DeviceDataItem(BuildDataProvider.getDevice(), R.string.device, Category.DEVICE)
      data[HARDWARE] = DeviceDataItem(BuildDataProvider.getHardware(), R.string.hardware, Category.DEVICE)
      data[PRODUCT] = DeviceDataItem(BuildDataProvider.getProduct(), R.string.product, Category.DEVICE)

      data[MODEL] = DeviceDataItem(BuildDataProvider.getModel(), R.string.model, Category.DEVICE)
      data[MANUFACTURE] = DeviceDataItem(BuildDataProvider.getManufacture(), R.string.manufacture, Category.DEVICE)
      data[DEVICE_NAME] =
          DeviceDataItem(BuildDataProvider.getCombinedDeviceName(), R.string.device_name, Category.DEVICE)

      data[BOOTLOADER] = DeviceDataItem(BuildDataProvider.getBootloader(), R.string.bootloader, Category.DEVICE)
      data[ID] = DeviceDataItem(BuildDataProvider.getID(), R.string.id, Category.DEVICE)

      data[CODENAME] = DeviceDataItem(BuildDataProvider.getCodename(), R.string.codename, Category.DEVICE)

      data[RELEASE] = DeviceDataItem(SystemDataProvider.getAndroidVersion(), R.string.release, Category.SYSTEM)
      data[SDK] = DeviceDataItem(SystemDataProvider.getSDKLevel(), R.string.sdk, Category.SYSTEM)

      data[PREVIEW_SDK] = DeviceDataItem(SystemDataProvider.getPreviewSDK(), R.string.preview_sdk, Category.SYSTEM)
      data[SECURITY_PATCH_LEVEL] =
          DeviceDataItem(SystemDataProvider.getPatchLevel(), R.string.security_patch_level, Category.SYSTEM)

      data[KERNEL] = DeviceDataItem(SystemDataProvider.getKernelVersion(), R.string.kernel, Category.SYSTEM)
      data[VM_VERSION] = DeviceDataItem(SystemDataProvider.getVMVersion(), R.string.vm_version, Category.SYSTEM)

      return data
    }

    fun getHardwareData(activity: Activity): Map<String, DeviceDataItem> {
      val data = HashMap<String, DeviceDataItem>()

      val res = DisplayDataProvider.getResolution(activity)
      data[RESOLUTION] = DeviceDataItem(res.x.toString() + " x " + res.y, R.string.resolution, Category.DISPLAY)
      data[RATIO] = DeviceDataItem(DisplayDataProvider.getDisplayRatio(res), R.string.ratio, Category.DISPLAY)

      val dpi = DisplayDataProvider.geDisplayDpi(activity)
      data[DPI] = DeviceDataItem(dpi.x.toString() + "/" + dpi.y, R.string.dpi, Category.DISPLAY)

      data[RAM] = DeviceDataItem(RamDataProvider.getTotalRam(activity), R.string.ram, Category.MEMORY)
      data[LOW_RAM_FLAG] =
          DeviceDataItem(RamDataProvider.hasLowRamFlag(activity).toString(), R.string.low_ram_flag, Category.MEMORY)

      data[ABI] = DeviceDataItem(CPUDataProvider.getPrimaryABI(), R.string.abi, Category.CPU)
      data[CPU_CORES] = DeviceDataItem(CPUDataProvider.getCPUCoreNum().toString(), R.string.cpu_cores, Category.CPU)

      CPUDataProvider.getGroupedCPUCoreFrequencies().forEach {
        data[CPU_FREQUENCIES] =
            DeviceDataItem(it.value.toString() + " x " + it.key, R.string.cpu_frequencies, Category.CPU)
      }

      data[BLUETOOTH] = DeviceDataItem(
        HardwareFeatureProvider.hasBluetooth(activity).toString(),
        R.string.bluetooth,
        Category.FEATURES
      )
      data[GPS] = DeviceDataItem(HardwareFeatureProvider.hasGPS(activity).toString(), R.string.gps, Category.FEATURES)
      data[NFC] = DeviceDataItem(HardwareFeatureProvider.hasNFC(activity).toString(), R.string.nfc, Category.FEATURES)

      return data
    }

    fun getSoftwareInfo(context: Context): Map<String, DeviceDataItem> {
      val data = HashMap<String, DeviceDataItem>()

      data[GOOGLE_PLAY_VERSION] =
          DeviceDataItem(
            SystemAppsDataProvider.getGooglePlayServicesVersion(context),
            R.string.google_play_version,
            Category.SOFTWARE
          )
      data[WEBVIEW_IMPLEMENTATION] =
          DeviceDataItem(
            SystemAppsDataProvider.getWebViewImplementation(context),
            R.string.webview_implementation,
            Category.SOFTWARE
          )

      return data
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
    val ABI = "ABI"
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
    val CPU_FREQUENCIES = "CPU_FREQUENCIES"
    val NFC = "NFC"
    val GPS = "GPS"
    val BLUETOOTH = "BLUETOOTH"
    val LOW_RAM_FLAG = "LOW_RAM_FLAG"
    val GOOGLE_PLAY_VERSION = "GOOGLE_PLAY_VERSION"
    val WEBVIEW_IMPLEMENTATION = "WEBVIEW_IMPLEMENTATION"
  }
}