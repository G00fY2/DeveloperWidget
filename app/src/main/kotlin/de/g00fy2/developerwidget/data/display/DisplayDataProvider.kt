package de.g00fy2.developerwidget.data.display

import android.app.Activity
import android.graphics.Point
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.DisplayMetrics
import java.text.NumberFormat

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
      val resX = Math.min(resolution.x, resolution.y)
      val resY = Math.max(resolution.x, resolution.y)
      val gcd = gcd(resX, resY)

      val result = (resY / gcd).toString() + ":" + (resX / gcd)
      val altResult =
        if ((resolution.x / gcd) > 9 && ((resolution.y / gcd / 2.0f) % 1.0f) == 0.5f && ((resolution.x / gcd / 2.0f) % 1.0f) == 0.0f) {
          NumberFormat.getInstance().let {
            it.format((resolution.y / gcd / 2.0f)).toString() + ":" + it.format((resolution.x / gcd / 2.0f))
          }
        } else {
          ""
        }

      return result + if (altResult.isBlank()) "" else " ($altResult)"
    }

    private fun gcd(p: Int, q: Int): Int {
      return if (q == 0) p
      else gcd(q, p % q)
    }
  }
}