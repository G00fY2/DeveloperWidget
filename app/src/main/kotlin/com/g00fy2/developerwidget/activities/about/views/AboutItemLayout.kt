package com.g00fy2.developerwidget.activities.about.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import com.g00fy2.developerwidget.R
import kotlinx.android.synthetic.main.about_item.view.*

class AboutItemLayout : ConstraintLayout {

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    LayoutInflater.from(context).inflate(R.layout.about_item, this, true)
  }

  override fun setEnabled(enabled: Boolean) {
    super.setEnabled(enabled)
    for (child in constraint_layout.children) child.isEnabled = enabled
  }

  fun icon(@DrawableRes iconRes: Int): AboutItemLayout {
    icon_imageview.setImageResource(iconRes)
    icon_imageview.visibility = View.VISIBLE
    return this
  }

  fun title(@StringRes titleRes: Int): AboutItemLayout {
    title_textview.setText(titleRes)
    title_textview.visibility = View.VISIBLE
    return this
  }

  fun description(@StringRes descriptionRes: Int): AboutItemLayout {
    description_textview.setText(descriptionRes)
    description_textview.visibility = View.VISIBLE
    return this
  }

  fun description(description: String): AboutItemLayout {
    if (description.isNotBlank()) {
      description_textview.text = description
      description_textview.visibility = View.VISIBLE
    } else {
      description_textview.visibility = View.GONE
    }
    return this
  }

  fun switch(on: Boolean): AboutItemLayout {
    setting_switch.isChecked = on
    setting_switch.visibility = View.VISIBLE
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