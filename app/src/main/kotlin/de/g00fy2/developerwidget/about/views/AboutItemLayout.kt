package de.g00fy2.developerwidget.about.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import de.g00fy2.developerwidget.R.layout
import kotlinx.android.synthetic.main.about_item.view.description_textview
import kotlinx.android.synthetic.main.about_item.view.icon_imageview
import kotlinx.android.synthetic.main.about_item.view.title_textview

class AboutItemLayout : ConstraintLayout {

  private var action: () -> Unit = {}
  private var honorClicking = false
  private var clickCount = 0
  private var clickStart: Long = 0

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    LayoutInflater.from(context).inflate(layout.about_item, this, true)
    icon_imageview.visibility = View.INVISIBLE
    title_textview.visibility = View.GONE
    description_textview.visibility = View.GONE
    setOnClickListener {
      action()
      honorClicking()
    }
  }

  fun setIcon(@DrawableRes iconRes: Int): AboutItemLayout {
    icon_imageview.setImageResource(iconRes)
    icon_imageview.visibility = View.VISIBLE
    return this
  }

  fun setTitle(@StringRes titleRes: Int): AboutItemLayout {
    title_textview.setText(titleRes)
    title_textview.visibility = View.VISIBLE
    return this
  }

  fun setDescription(@StringRes descriptionRes: Int): AboutItemLayout {
    description_textview.setText(descriptionRes)
    description_textview.visibility = View.VISIBLE
    return this
  }

  fun setDescription(description: String): AboutItemLayout {
    if (!description.isBlank()) {
      description_textview.text = description
      description_textview.visibility = View.VISIBLE
    } else {
      description_textview.visibility = View.GONE
    }
    return this
  }

  fun setAction(action: () -> Unit): AboutItemLayout {
    this.action = action
    return this
  }

  fun enableHonorClicking(enable: Boolean) {
    honorClicking = enable
  }

  private fun honorClicking() {
    if (honorClicking) {
      val current = System.currentTimeMillis()
      if (current - clickStart > 2000) {
        clickCount = 0
      }
      clickCount++
      if (clickCount <= 7) {
        clickStart = current
      }
      if (clickCount in 3..6) {
        val missingSteps = (7 - clickCount)
        Toast.makeText(
          context,
          "You are now " + missingSteps + (if (missingSteps > 1) " steps" else " step") + " away from being a developer.",
          Toast.LENGTH_SHORT
        ).show()
      } else if (clickCount == 7) {
        Toast.makeText(context, "You are a real developer!", Toast.LENGTH_SHORT).show()
      }
    }
  }
}