package de.g00fy2.developerwidget.controllers

import androidx.annotation.StringRes

interface StringController {

  fun getString(@StringRes resId: Int): String
}