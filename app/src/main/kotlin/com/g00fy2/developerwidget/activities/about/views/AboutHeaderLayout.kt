package com.g00fy2.developerwidget.activities.about.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.StringRes
import com.g00fy2.developerwidget.databinding.AboutItemHeaderBinding

class AboutHeaderLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private val binding = AboutItemHeaderBinding.inflate(LayoutInflater.from(context), this)

  fun title(@StringRes titleRes: Int): AboutHeaderLayout {
    binding.headerTextview.setText(titleRes)
    return this
  }

  inline fun init(func: AboutHeaderLayout.() -> Unit) = func()
}
