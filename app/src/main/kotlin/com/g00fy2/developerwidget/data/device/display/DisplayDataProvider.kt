package com.g00fy2.developerwidget.data.device.display

import android.content.Context
import android.graphics.Point
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.content.getSystemService
import java.text.NumberFormat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class DisplayDataProvider {

  companion object {

    fun getResolution(context: Context): Point? {
      context.getSystemService<WindowManager>()?.defaultDisplay?.let { windowManager ->
        Point().let { point ->
          if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager.getRealSize(point)
          } else {
            windowManager.getSize(point)
          }
          return point
        }
      }
      return null
    }

    fun geDisplayDpi(context: Context): String {
      context.getSystemService<WindowManager>()?.defaultDisplay?.let { windowManager ->
        DisplayMetrics().let { displayMetrics ->
          if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager.getRealMetrics(displayMetrics)
          } else {
            windowManager.getMetrics(displayMetrics)
          }
          return displayMetrics.xdpi.roundToInt().toString() + " / " + displayMetrics.ydpi.roundToInt() + " dpi"
        }
      }
      return ""
    }

    fun getDisplayRatio(resolution: Point): String {
      val resX = min(resolution.x, resolution.y)
      val resY = max(resolution.x, resolution.y)
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