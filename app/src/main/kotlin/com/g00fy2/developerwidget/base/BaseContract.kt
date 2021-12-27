package com.g00fy2.developerwidget.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

interface BaseContract {

  interface BaseView : LifecycleOwner

  interface BasePresenter : DefaultLifecycleObserver
}