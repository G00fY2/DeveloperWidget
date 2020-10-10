package com.g00fy2.developerwidget.ktx

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.g00fy2.developerwidget.R

fun TextView.setClickableText(text: String, linkTarget: String, onClick: () -> Unit) {
  val result = SpannableString(text)
  val clickableSpan = object : ClickableSpan() {
    override fun onClick(widget: View) {
      onClick()
    }

    override fun updateDrawState(ds: TextPaint) {
      super.updateDrawState(ds)
      ds.color = ResourcesCompat.getColor(context.resources, R.color.colorAccent, null)
      ds.isUnderlineText = false
    }
  }
  val linkStart = result.indexOf(linkTarget)
  if (linkStart >= 0) {
    result.setSpan(clickableSpan, linkStart, linkStart + linkTarget.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
  }
  this.text = result
  movementMethod = LinkMovementMethod.getInstance()
}