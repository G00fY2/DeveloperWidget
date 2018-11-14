package de.g00fy2.developerwidget.data.cpu

import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import java.io.File
import java.io.RandomAccessFile
import java.util.regex.Pattern

class CPUDataProvider {

  companion object {

    fun getPrimaryABI(): String {
      return if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        Build.SUPPORTED_ABIS.firstOrNull() ?: ""
      } else {
        @Suppress("DEPRECATION")
        Build.CPU_ABI
      }
    }

    fun getCPUCoreNum(): Int {
      val pattern = Pattern.compile("cpu[0-9]+")
      return Math.max(
        File("/sys/devices/system/cpu/")
          .walk()
          .count { pattern.matcher(it.name).matches() },
        Runtime.getRuntime().availableProcessors()
      )
    }

    fun getGroupedCPUCoreFrequencies(): String {
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
        .groupingBy { it.first.toString() + "MHz - " + it.second + "MHz" }
        .eachCount()
        .map { it.value.toString() + " x " + it.key }
        .joinToString(separator = "\n")
    }
  }
}