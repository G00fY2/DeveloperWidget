package de.g00fy2.developerwidget.util

import android.content.Context
import android.util.DisplayMetrics

class ViewUtils {

  companion object {

    fun dpToPx(dp: Float, context: Context): Float {
      val metrics = context.resources.displayMetrics
      return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
  }
}
