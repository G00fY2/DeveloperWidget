package de.g00fy2.developerwidget.data.display

import android.app.Activity
import android.graphics.Point
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.DisplayMetrics

class DisplayDataProvider {

  companion object {

    fun getResolution(activity: Activity): Point {
      val display = activity.windowManager.defaultDisplay
      val size = Point()
      if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
        display.getRealSize(size)
      } else {
        display.getSize(size)
      }
      return size
    }

    fun geDisplayDpi(activity: Activity): Point {
      val display = activity.windowManager.defaultDisplay
      val displayMetric = DisplayMetrics()
      if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
        display.getRealMetrics(displayMetric)
      } else {
        display.getMetrics(displayMetric)
      }
      return Point(Math.round(displayMetric.xdpi), Math.round(displayMetric.ydpi))
    }

    fun getDisplayRatio(resolution: Point): String {
      val gcd = gcd(resolution.x, resolution.y)
      return if (resolution.x < resolution.y) {
        (resolution.y / gcd).toString() + ":" + (resolution.x / gcd)
      } else {
        (resolution.x / gcd).toString() + ":" + (resolution.y / gcd)
      }
    }

    private fun gcd(p: Int, q: Int): Int {
      return if (q == 0) p
      else gcd(q, p % q)
    }
  }
}