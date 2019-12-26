package com.g00fy2.developerwidget.activities.about.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import com.g00fy2.developerwidget.databinding.AboutItemBinding
import com.g00fy2.developerwidget.ktx.addRipple
import com.google.android.material.textview.MaterialTextView

class AboutItemLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  ConstraintLayout(context, attrs, defStyleAttr) {

  private val binding = AboutItemBinding.inflate(LayoutInflater.from(context), this)

  override fun setEnabled(enabled: Boolean) {
    super.setEnabled(enabled)
    for (child in children) {
      if (child::class == MaterialTextView::class) {
        child.alpha = if (enabled) 1f else 0.38f
      } else {
        child.isEnabled = enabled
      }
    }
  }

  fun icon(@DrawableRes iconRes: Int): AboutItemLayout {
    binding.iconImageview.setImageResource(iconRes)
    binding.iconImageview.isVisible = true
    return this
  }

  fun title(@StringRes titleRes: Int): AboutItemLayout {
    binding.titleTextview.setText(titleRes)
    binding.titleTextview.isVisible = true
    return this
  }

  fun description(@StringRes descriptionRes: Int): AboutItemLayout {
    binding.descriptionTextview.setText(descriptionRes)
    binding.descriptionTextview.isVisible = true
    return this
  }

  fun description(description: String): AboutItemLayout {
    if (description.isNotBlank()) {
      binding.descriptionTextview.text = description
      binding.descriptionTextview.isVisible = true
    } else {
      binding.descriptionTextview.isVisible = false
    }
    return this
  }

  fun switch(on: Boolean): AboutItemLayout {
    binding.settingSwitch.isChecked = on
    binding.settingSwitch.isVisible = true
    return this
  }

  fun action(action: () -> Unit): AboutItemLayout {
    addRipple()
    setOnClickListener {
      action()
      if (binding.settingSwitch.isVisible) binding.settingSwitch.isChecked = !binding.settingSwitch.isChecked
    }
    return this
  }

  inline fun init(func: AboutItemLayout.() -> Unit) = func()
}