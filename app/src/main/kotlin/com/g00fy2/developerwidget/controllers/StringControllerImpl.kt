package com.g00fy2.developerwidget.controllers

import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.di.annotations.ACTIVITY
import javax.inject.Inject
import javax.inject.Named

class StringControllerImpl @Inject constructor() : StringController {

  @Inject
  @Named(ACTIVITY)
  lateinit var activity: BaseActivity

  override fun getString(resId: Int): String = activity.getString(resId)
}