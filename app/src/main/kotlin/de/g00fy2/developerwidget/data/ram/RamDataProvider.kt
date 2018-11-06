package de.g00fy2.developerwidget.data.ram

import android.app.ActivityManager
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import java.io.IOException
import java.io.RandomAccessFile
import java.util.regex.Pattern

class RamDataProvider {

  companion object {

    fun getTotalRam(context: Context): String {
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
  }
}