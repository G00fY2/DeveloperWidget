package de.g00fy2.developerwidget.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

interface BaseContract {

  interface BaseView : LifecycleOwner

  interface BasePresenter : LifecycleObserver

}