package com.g00fy2.developerwidget.activities.about.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import com.g00fy2.developerwidget.R
import kotlinx.android.synthetic.main.about_item.view.*

class AboutItemLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  ConstraintLayout(context, attrs, defStyleAttr) {

  init {
    inflate(context, R.layout.about_item, this)
  }

  override fun setEnabled(enabled: Boolean) {
    super.setEnabled(enabled)
    for (child in children) child.isEnabled = enabled
  }

  fun icon(@DrawableRes iconRes: Int): AboutItemLayout {
    icon_imageview.setImageResource(iconRes)
    icon_imageview.isVisible = true
    return this
  }

  fun title(@StringRes titleRes: Int): AboutItemLayout {
    title_textview.setText(titleRes)
    title_textview.isVisible = true
    return this
  }

  fun description(@StringRes descriptionRes: Int): AboutItemLayout {
    description_textview.setText(descriptionRes)
    description_textview.isVisible = true
    return this
  }

  fun description(description: String): AboutItemLayout {
    if (description.isNotBlank()) {
      description_textview.text = description
      description_textview.isVisible = true
    } else {
      description_textview.isVisible = false
    }
    return this
  }

  fun switch(on: Boolean): AboutItemLayout {
    setting_switch.isChecked = on
    setting_switch.isVisible = true
    return this
  }

  fun action(action: () -> Unit): AboutItemLayout {
    setOnClickListener {
      action()
      if (setting_switch.isVisible) setting_switch.isChecked = !setting_switch.isChecked
    }
    return this
  }

  inline fun init(func: AboutItemLayout.() -> Unit) = func()
}