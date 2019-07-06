package com.g00fy2.developerwidget.data.device.display

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.content.getSystemService
import java.text.NumberFormat
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class DisplayDataProvider {

  companion object {

    @SuppressLint("NewApi")
    fun getResolution(context: Context): Point? {
      context.getSystemService<WindowManager>()?.defaultDisplay?.let { windowManager ->
        Point().let { point ->
          try {
            windowManager.getRealSize(point)
          } catch (e: NoSuchMethodError) {
            windowManager.getSize(point)
          }
          return point
        }
      }
      return null
    }

    @SuppressLint("NewApi")
    fun geDisplayDpi(context: Context): String {
      context.getSystemService<WindowManager>()?.defaultDisplay?.let { windowManager ->
        DisplayMetrics().let { displayMetrics ->
          try {
            windowManager.getRealMetrics(displayMetrics)
          } catch (e: NoSuchMethodError) {
            windowManager.getMetrics(displayMetrics)
          }
          return displayMetrics.xdpi.roundToInt().toString() + " / " + displayMetrics.ydpi.roundToInt() + " dpi"
        }
      }
      return ""
    }

    fun getDisplayRatio(resolution: Point): String {
      if (resolution.x > resolution.y) {
        val temp = resolution.x
        resolution.x = resolution.y
        resolution.y = temp
      }
      val gcd = gcd(resolution.x, resolution.y)

      val result = if (resolution.y / gcd == 8) {
        (resolution.y / gcd * 2).toString() + ":" + (resolution.x / gcd * 2)
      } else {
        (resolution.y / gcd).toString() + ":" + (resolution.x / gcd)
      }

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