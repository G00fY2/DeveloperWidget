package com.g00fy2.developerwidget.data

import android.content.Context
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.data.DeviceDataItem.Category
import com.g00fy2.developerwidget.data.device.cpu.CPUDataProvider
import com.g00fy2.developerwidget.data.device.devicebuild.BuildDataProvider
import com.g00fy2.developerwidget.data.device.display.DisplayDataProvider
import com.g00fy2.developerwidget.data.device.hardwarefeature.HardwareFeatureProvider
import com.g00fy2.developerwidget.data.device.ram.RamDataProvider
import com.g00fy2.developerwidget.data.device.system.SystemDataProvider
import com.g00fy2.developerwidget.data.device.systemapps.SystemAppsDataProvider
import com.g00fy2.developerwidget.di.annotations.APPLICATION
import javax.inject.Inject
import javax.inject.Named

class DeviceDataSourceImpl @Inject constructor() : DeviceDataSource {

  @Inject
  @Named(APPLICATION)
  lateinit var context: Context

  override suspend fun getStaticDeviceData(): Map<String, DeviceDataItem> {
    val data = HashMap<String, DeviceDataItem>()

    data[BOARD] = DeviceDataItem(BuildDataProvider.getBoard(), R.string.board, Category.DEVICE)
    data[DEVICE] = DeviceDataItem(BuildDataProvider.getDevice(), R.string.device, Category.DEVICE)
    data[HARDWARE] = DeviceDataItem(BuildDataProvider.getHardware(), R.string.hardware, Category.DEVICE)
    data[PRODUCT] = DeviceDataItem(BuildDataProvider.getProduct(), R.string.product, Category.DEVICE)

    data[MODEL] = DeviceDataItem(BuildDataProvider.getModel(), R.string.model, Category.DEVICE)
    data[MANUFACTURE] = DeviceDataItem(BuildDataProvider.getManufacture(), R.string.manufacture, Category.DEVICE)
    data[DEVICE_NAME] = DeviceDataItem(BuildDataProvider.getCombinedDeviceName(), R.string.device_name, Category.DEVICE)

    data[BOOTLOADER] = DeviceDataItem(BuildDataProvider.getBootloader(), R.string.bootloader, Category.DEVICE)
    data[ID] = DeviceDataItem(BuildDataProvider.getID(), R.string.id, Category.DEVICE)

    data[CODENAME] = DeviceDataItem(SystemDataProvider.getCodename(), R.string.codename, Category.DEVICE)

    data[RELEASE] = DeviceDataItem(SystemDataProvider.getAndroidVersion(), R.string.release, Category.SYSTEM)
    data[SDK] = DeviceDataItem(SystemDataProvider.getSDKLevel(), R.string.sdk, Category.SYSTEM)

    data[PREVIEW_SDK] = DeviceDataItem(SystemDataProvider.getPreviewSDK(), R.string.preview_sdk, Category.SYSTEM)
    data[SECURITY_PATCH_LEVEL] =
      DeviceDataItem(SystemDataProvider.getPatchLevel(), R.string.security_patch_level, Category.SYSTEM)

    data[KERNEL] = DeviceDataItem(SystemDataProvider.getKernelVersion(), R.string.kernel, Category.SYSTEM)
    data[VM_VERSION] = DeviceDataItem(SystemDataProvider.getVMVersion(), R.string.vm_version, Category.SYSTEM)

    return data
  }

  override suspend fun getHardwareData(): Map<String, DeviceDataItem> {
    val data = HashMap<String, DeviceDataItem>()

    DisplayDataProvider.getResolution(context)?.let { res ->
      data[RESOLUTION] = DeviceDataItem(res.x.toString() + " x " + res.y, R.string.resolution, Category.DISPLAY)
      data[RATIO] = DeviceDataItem(DisplayDataProvider.getDisplayRatio(res), R.string.ratio, Category.DISPLAY)
    }

    data[DPI] = DeviceDataItem(DisplayDataProvider.geDisplayDpi(context), R.string.dpi, Category.DISPLAY)

    data[RAM] = DeviceDataItem(RamDataProvider.getTotalRam(context), R.string.ram, Category.MEMORY)
    data[LOW_RAM_FLAG] = DeviceDataItem(RamDataProvider.hasLowRamFlag(context), R.string.low_ram_flag, Category.MEMORY)

    data[ABI] = DeviceDataItem(CPUDataProvider.getPrimaryABI(), R.string.abi, Category.CPU)
    data[CPU_CORES] = DeviceDataItem(CPUDataProvider.getCPUCoreNum().toString(), R.string.cpu_cores, Category.CPU)
    data[CPU_FREQUENCIES] =
      DeviceDataItem(CPUDataProvider.getGroupedCPUCoreFrequencies(), R.string.cpu_frequencies, Category.CPU)

    data[BLUETOOTH] =
      DeviceDataItem(HardwareFeatureProvider.hasBluetooth(context).toString(), R.string.bluetooth, Category.FEATURES)
    data[GPS] = DeviceDataItem(HardwareFeatureProvider.hasGPS(context).toString(), R.string.gps, Category.FEATURES)
    data[NFC] = DeviceDataItem(HardwareFeatureProvider.hasNFC(context).toString(), R.string.nfc, Category.FEATURES)

    return data
  }

  override suspend fun getSoftwareInfo(): Map<String, DeviceDataItem> {
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

  override suspend fun getHeaderItems(): Map<String, DeviceDataItem> {
    val data = HashMap<String, DeviceDataItem>()
    Category.values().forEach {
      data[it.name] = DeviceDataItem(it, true)
    }
    return data
  }

  override fun getCombinedDeviceName() = BuildDataProvider.getCombinedDeviceName()

  override fun getVersionAndSDK() = SystemDataProvider.getVersionAndSDK()

  companion object {
    const val BOARD = "BOARD"
    const val DEVICE = "DEVICE"
    const val HARDWARE = "HARDWARE"
    const val PRODUCT = "PRODUCT"
    const val MODEL = "MODEL"
    const val MANUFACTURE = "MANUFACTURE"
    const val DEVICE_NAME = "DEVICE_NAME"
    const val BOOTLOADER = "BOOTLOADER"
    const val ID = "ID"
    const val ABI = "ABI"
    const val CODENAME = "CODENAME"
    const val PREVIEW_SDK = "PREVIEW_SDK"
    const val SECURITY_PATCH_LEVEL = "SECURITY_PATCH_LEVEL"
    const val RELEASE = "RELEASE"
    const val SDK = "SDK"
    const val VM_VERSION = "VM_VERSION"
    const val KERNEL = "KERNEL"
    const val RESOLUTION = "RESOLUTION"
    const val RATIO = "RATIO"
    const val DPI = "DPI"
    const val RAM = "RAM"
    const val CPU_CORES = "CPU_CORES"
    const val CPU_FREQUENCIES = "CPU_FREQUENCIES"
    const val NFC = "NFC"
    const val GPS = "GPS"
    const val BLUETOOTH = "BLUETOOTH"
    const val LOW_RAM_FLAG = "LOW_RAM_FLAG"
    const val GOOGLE_PLAY_VERSION = "GOOGLE_PLAY_VERSION"
    const val WEBVIEW_IMPLEMENTATION = "WEBVIEW_IMPLEMENTATION"
  }
}