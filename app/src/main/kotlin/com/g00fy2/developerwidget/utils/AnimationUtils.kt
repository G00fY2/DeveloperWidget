package com.g00fy2.developerwidget.utils

import android.animation.ValueAnimator
import android.view.View
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd

class AnimationUtils {

  companion object {

    fun expandView(v: View, fade: Boolean = false) {
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
          duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        }.start()
      }
    }

    fun collapseView(v: View, fade: Boolean = false) {
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
          duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
          doOnEnd { visibility = View.GONE }
        }.start()
      }
    }
  }
}