package de.g00fy2.developerwidget.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface BaseContract {

  interface BaseView

  interface BasePresenter : LifecycleObserver, CoroutineScope
}