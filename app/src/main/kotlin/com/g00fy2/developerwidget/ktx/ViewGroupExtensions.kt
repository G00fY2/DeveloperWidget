package com.g00fy2.developerwidget.ktx

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import com.g00fy2.developerwidget.R

fun ViewGroup.expand(fade: Boolean = false, easing: TimeInterpolator? = null) = this.apply {
  val params = layoutParams as ViewGroup.MarginLayoutParams
  params.topMargin = -height
  layoutParams = params
  if (fade) alpha = 0f
  visibility = View.VISIBLE

  val viewHeight = height
  ValueAnimator.ofInt(-viewHeight, 0).apply {
    addUpdateListener {
      (it.animatedValue as Int).let { value ->
        params.topMargin = value
        layoutParams = params
        if (fade) alpha = (viewHeight + value) / viewHeight.toFloat()
      }
    }
    easing?.let { easing -> interpolator = easing }
    duration = resources.getInteger(R.integer.animation_duration).toLong()
  }.start()
}

fun ViewGroup.collapse(fade: Boolean = false, easing: TimeInterpolator? = null) = this.apply {
  val params = layoutParams as ViewGroup.MarginLayoutParams
  params.topMargin = 0
  layoutParams = params
  if (fade) alpha = 1f
  visibility = View.VISIBLE

  val viewHeight = height
  ValueAnimator.ofInt(0, -viewHeight).apply {
    addUpdateListener {
      (it.animatedValue as Int).let { value ->
        params.topMargin = value
        layoutParams = params
        if (fade) alpha = (viewHeight + value) / viewHeight.toFloat()
      }
    }
    easing?.let { easing -> interpolator = easing }
    duration = resources.getInteger(R.integer.animation_duration).toLong()
    doOnEnd { visibility = View.GONE }
  }.start()
}