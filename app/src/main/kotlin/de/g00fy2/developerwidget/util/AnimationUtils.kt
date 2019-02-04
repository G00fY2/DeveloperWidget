package de.g00fy2.developerwidget.util

import android.animation.ValueAnimator
import android.view.View
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd
import timber.log.Timber

class AnimationUtils {

  companion object {
    fun expandView(v: View): ValueAnimator {
      v.apply {
        measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val params = layoutParams as LinearLayout.LayoutParams
        params.topMargin = -height
        Timber.d("height$height")
        requestLayout()
        visibility = View.VISIBLE

        return ValueAnimator.ofInt(params.topMargin, 0).apply {
          addUpdateListener { valueAnimator ->
            params.topMargin = valueAnimator.animatedValue as Int
            requestLayout()
            duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
          }
        }
      }
    }

    fun collapseView(v: View): ValueAnimator {
      v.apply {
        measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val params = layoutParams as LinearLayout.LayoutParams
        params.topMargin = 0
        Timber.d("height$height")
        requestLayout()
        visibility = View.VISIBLE

        return ValueAnimator.ofInt(0, -height).apply {
          addUpdateListener { valueAnimator ->
            params.topMargin = valueAnimator.animatedValue as Int
            requestLayout()
            duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
          }.apply { doOnEnd { v.visibility = View.GONE } }
        }
      }
    }
  }
}