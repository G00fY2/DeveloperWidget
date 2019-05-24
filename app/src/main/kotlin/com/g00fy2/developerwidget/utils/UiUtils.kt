package com.g00fy2.developerwidget.utils

import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import com.g00fy2.developerwidget.base.BaseActivity

class UiUtils {

  companion object {

    fun hideKeyboard(activity: BaseActivity) {
      activity.getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
  }
}