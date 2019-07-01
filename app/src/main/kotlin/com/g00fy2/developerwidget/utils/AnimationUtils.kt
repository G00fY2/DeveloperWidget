package com.g00fy2.developerwidget.utils

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.view.View
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd
import com.g00fy2.developerwidget.R

class AnimationUtils {

  companion object {

    fun expandView(v: View, fade: Boolean = false, easing: TimeInterpolator? = null) {
      v.apply {
        val params = layoutParams as LinearLayout.LayoutParams
        params.topMargin = -height
        requestLayout()
        if (fade) alpha = 0f
        visibility = View.VISIBLE

        val viewHeight = height
        ValueAnimator.ofInt(-viewHeight, 0).apply {
          addUpdateListener {
            (it.animatedValue as Int).let { value ->
              params.topMargin = value
              if (fade) alpha = (viewHeight + value) / viewHeight.toFloat()
            }
            requestLayout()
          }
          easing?.let { easing -> interpolator = easing }
          duration = resources.getInteger(R.integer.animation_duration).toLong()
        }.start()
      }
    }

    fun collapseView(v: View, fade: Boolean = false, easing: TimeInterpolator? = null) {
      v.apply {
        val params = layoutParams as LinearLayout.LayoutParams
        params.topMargin = 0
        requestLayout()
        if (fade) alpha = 1f
        visibility = View.VISIBLE

        val viewHeight = height
        ValueAnimator.ofInt(0, -viewHeight).apply {
          addUpdateListener {
            (it.animatedValue as Int).let { value ->
              params.topMargin = value
              if (fade) alpha = (viewHeight + value) / viewHeight.toFloat()
            }
            requestLayout()
          }
          easing?.let { easing -> interpolator = easing }
          duration = resources.getInteger(R.integer.animation_duration).toLong()
          doOnEnd { visibility = View.GONE }
        }.start()
      }
    }
  }
}