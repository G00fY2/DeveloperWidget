package com.g00fy2.developerwidget.data.cpu

import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.util.regex.Pattern

class CPUDataProvider {

  companion object {

    private const val CPU_SYS_FOLDER = "/sys/devices/system/cpu/"
    private val CPU_PATTERN = Pattern.compile("cpu[0-9]+")

    fun getPrimaryABI(): String {
      return if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        Build.SUPPORTED_ABIS.firstOrNull() ?: ""
      } else {
        @Suppress("DEPRECATION")
        Build.CPU_ABI
      }
    }

    fun getCPUCoreNum(): Int {
      return Math.max(
        File(CPU_SYS_FOLDER)
          .walk()
          .maxDepth(1)
          .count { CPU_PATTERN.matcher(it.name).matches() },
        Runtime.getRuntime().availableProcessors()
      )
    }

    fun getGroupedCPUCoreFrequencies(): String {
      val frequencies = mutableListOf<Pair<Int, Int>>()
      val cores = getCPUCoreNum()
      for (i in 0 until cores) {
        getCPUFrequenciesPerCore(i)?.let { frequencies.add(it) }
      }

      return frequencies.sortedByDescending { it.second }
        .groupingBy { it.second.toString() + "MHz - " + it.first + "MHz" }
        .eachCount()
        .map { it.value.toString() + " x " + it.key }
        .plus(if (frequencies.size < cores) (cores - frequencies.size).toString() + " x offline" else "")
        .filter { it.isNotEmpty() }
        .joinToString(separator = "\n")
    }

    private fun getCPUFrequenciesPerCore(core: Int): Pair<Int, Int>? {
      return try {
        val max = RandomAccessFile(CPU_SYS_FOLDER + "cpu$core/cpufreq/cpuinfo_max_freq", "r").use { reader ->
          (reader.readLine().toLong() / 1000).toInt()
        }
        val min = RandomAccessFile(CPU_SYS_FOLDER + "cpu$core/cpufreq/cpuinfo_min_freq", "r").use { reader ->
          (reader.readLine().toLong() / 1000).toInt()
        }
        Pair(min, max)
      } catch (e: FileNotFoundException) {
        null
      }
    }
  }
}