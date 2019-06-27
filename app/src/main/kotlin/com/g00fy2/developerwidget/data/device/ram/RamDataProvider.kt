package com.g00fy2.developerwidget.data.device.ram

import android.app.ActivityManager
import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.content.getSystemService
import java.io.IOException
import java.io.RandomAccessFile
import java.util.regex.Pattern
import kotlin.math.roundToLong

class RamDataProvider {

  companion object {

    fun getTotalRam(context: Context): String {
      var ramSize = 0L
      if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
        context.getSystemService<ActivityManager>()?.let {
          ActivityManager.MemoryInfo().let { info ->
            it.getMemoryInfo(info)
            ramSize = info.totalMem
          }
        }
      } else {
        try {
          RandomAccessFile("/proc/meminfo", "r").use {
            val matcher = Pattern.compile("(\\d+)").matcher(it.readLine())
            var memTotal: String? = null
            while (matcher.find()) memTotal = matcher.group(1)
            memTotal?.let { stringValue ->
              ramSize = stringValue.toLong()
              return@use
            }
          }
        } catch (e: IOException) {
          // ignore and keep ramSize 0
        }
      }
      return if (ramSize > 0) (2 * ((ramSize / (1024.0 * 1024.0) / 2).roundToLong())).toString() + " MB" else ""
    }

    fun hasLowRamFlag(context: Context): String {
      return if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
        context.getSystemService<ActivityManager>()?.isLowRamDevice?.toString() ?: ""
      } else {
        ""
      }
    }
  }
}