package com.g00fy2.developerwidget.base

import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BasePresenterImpl : BaseContract.BasePresenter {

  private val job: Job by lazy { Job() }

  override val coroutineContext: CoroutineContext = Dispatchers.Main + job

  @OnLifecycleEvent(Event.ON_DESTROY)
  fun cancelJob() = job.cancel()

}