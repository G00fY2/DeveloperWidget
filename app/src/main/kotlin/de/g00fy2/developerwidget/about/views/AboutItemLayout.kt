package de.g00fy2.developerwidget.about.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import de.g00fy2.developerwidget.R.layout
import kotlinx.android.synthetic.main.about_item.view.description_textview
import kotlinx.android.synthetic.main.about_item.view.icon_imageview
import kotlinx.android.synthetic.main.about_item.view.title_textview

class AboutItemLayout : ConstraintLayout {

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    LayoutInflater.from(context).inflate(layout.about_item, this, true)
    icon_imageview.visibility = View.INVISIBLE
    title_textview.visibility = View.GONE
    description_textview.visibility = View.GONE
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

  // TODO add onClick actions

}