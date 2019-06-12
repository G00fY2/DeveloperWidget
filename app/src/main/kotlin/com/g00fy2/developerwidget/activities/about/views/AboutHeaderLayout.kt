package com.g00fy2.developerwidget.activities.about.views

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.StringRes
import com.g00fy2.developerwidget.R
import kotlinx.android.synthetic.main.about_item_header.view.*

class AboutHeaderLayout : FrameLayout {

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    LayoutInflater.from(context).inflate(R.layout.about_item_header, this, true)
  }

  fun title(@StringRes titleRes: Int): AboutHeaderLayout {
    header_textview.setText(titleRes)
    if (VERSION.SDK_INT >= VERSION_CODES.Q) {
      header_textview.isAllCaps = true
      header_textview.textSize = 12f
    }
    return this
  }

  inline fun init(func: AboutHeaderLayout.() -> Unit) = func()
}
