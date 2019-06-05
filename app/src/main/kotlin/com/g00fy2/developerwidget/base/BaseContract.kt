package com.g00fy2.developerwidget.base

import androidx.lifecycle.LifecycleObserver
import kotlinx.coroutines.CoroutineScope

interface BaseContract {

  interface BaseView

  interface BasePresenter : LifecycleObserver, CoroutineScope
}