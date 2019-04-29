package com.g00fy2.developerwidget.base

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.g00fy2.developerwidget.controllers.DayNightController
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId), HasThemeDelegate {

  @Inject
  lateinit var dayNightController: DayNightController

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    Timber.d("Lifecycle: %s onCreate", localClassName)
    super.onCreate(savedInstanceState)
    dayNightController.loadCustomDefaultMode()
    lifecycle.addObserver(providePresenter())
    initCompatNavigationBar()
  }

  override fun onDestroy() {
    Timber.d("Lifecycle: %s onDestroy", localClassName)
    super.onDestroy()
    lifecycle.removeObserver(providePresenter())
  }

  fun setActionbarElevationListener(viewGroup: ViewGroup) {
    supportActionBar?.elevation = 0f
    viewGroup.viewTreeObserver.addOnScrollChangedListener {
      viewGroup.scrollY.toFloat().let {
        if (it <= 0f) {
          supportActionBar?.elevation = 0f
        } else {
          var elevationDp = it / 4 // divide scrollY to increase fade in range
          if (elevationDp > 4f) elevationDp = 4f
          supportActionBar?.elevation = elevationDp * resources.displayMetrics.density
        }
      }
    }
  }

  private fun initCompatNavigationBar() {
    // api 27+ allow applying flag via xml (windowLightNavigationBar)
    if (VERSION.SDK_INT == VERSION_CODES.O && dayNightController.isInNightMode()) {
      window.decorView.let { view ->
        view.systemUiVisibility.let { flags ->
          view.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
      }
    }
  }

  abstract fun providePresenter(): BaseContract.BasePresenter
}