package com.g00fy2.developerwidget.base

import android.content.res.Configuration
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.g00fy2.developerwidget.controllers.DayNightController
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity(@LayoutRes contentLayoutId: Int, private val isDialogActivity: Boolean = false) :
  AppCompatActivity(contentLayoutId) {

  @Inject
  lateinit var dayNightController: DayNightController

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    Timber.d("Lifecycle: %s1 onCreate %s2", localClassName, hashCode())

    if (isDialogActivity) {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      super.onCreate(savedInstanceState)

      val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
      val height = (resources.displayMetrics.heightPixels * 0.80).toInt()
      window.setLayout(width, height)
    } else {
      super.onCreate(savedInstanceState)
    }

    dayNightController.loadCustomDefaultMode()
    lifecycle.addObserver(providePresenter())
    initCompatNavigationBar()
    initView()
  }

  override fun onDestroy() {
    Timber.d("Lifecycle: %s1 onDestroy %s2", localClassName, hashCode())
    super.onDestroy()
    lifecycle.removeObserver(providePresenter())
  }

  protected fun setActionbarElevationListener(viewGroup: ViewGroup) {
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
    if (VERSION.SDK_INT == VERSION_CODES.O && isInNightMode()) {
      window.decorView.let { view ->
        view.systemUiVisibility.let { flags ->
          view.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
      }
    }
  }

  // TODO move back to controller if https://issuetracker.google.com/issues/134379747 should get fixed
  private fun isInNightMode() =
    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO

  abstract fun providePresenter(): BaseContract.BasePresenter

  abstract fun initView()
}