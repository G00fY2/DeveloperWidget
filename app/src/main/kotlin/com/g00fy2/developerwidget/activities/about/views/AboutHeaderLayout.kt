package com.g00fy2.developerwidget.activities.about.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.StringRes
import com.g00fy2.developerwidget.R
import kotlinx.android.synthetic.main.about_item_header.view.*

class AboutHeaderLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  init {
    inflate(context, R.layout.about_item_header, this)
  }

  fun title(@StringRes titleRes: Int): AboutHeaderLayout {
    header_textview.setText(titleRes)
    return this
  }

  inline fun init(func: AboutHeaderLayout.() -> Unit) = func()
}
