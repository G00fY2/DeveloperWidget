package com.g00fy2.developerwidget.ktx

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.getSystemService

fun EditText.showKeyboard() =
  context.getSystemService<InputMethodManager>()?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)

fun EditText.hideKeyboard() = context.getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(windowToken, 0)