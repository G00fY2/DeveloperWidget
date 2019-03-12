package com.g00fy2.developerwidget.utils

import android.animation.ValueAnimator
import android.view.View
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd

class AnimationUtils {

  companion object {
    fun expandView(v: View) {
      v.apply {
        val params = layoutParams as LinearLayout.LayoutParams
        params.topMargin = -height
        requestLayout()
        visibility = View.VISIBLE

        ValueAnimator.ofInt(-height, 0).apply {
          addUpdateListener {
            params.topMargin = it.animatedValue as Int
            requestLayout()
          }
          duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        }.start()
      }
    }

    fun collapseView(v: View) {
      v.apply {
        val params = layoutParams as LinearLayout.LayoutParams
        params.topMargin = 0
        requestLayout()
        visibility = View.VISIBLE

        ValueAnimator.ofInt(0, -height).apply {
          addUpdateListener {
            params.topMargin = it.animatedValue as Int
            requestLayout()
          }
          duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
          doOnEnd { v.visibility = View.GONE }
        }.start()
      }
    }
  }
}