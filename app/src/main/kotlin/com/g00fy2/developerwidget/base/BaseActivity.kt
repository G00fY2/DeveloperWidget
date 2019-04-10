package com.g00fy2.developerwidget.base

import android.content.res.Configuration
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import dagger.android.AndroidInjection

abstract class BaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    lifecycle.addObserver(providePresenter())
    initCompatNavigationBar()
  }

  override fun onDestroy() {
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
    if (VERSION.SDK_INT == VERSION_CODES.O) {
      if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO) {
        window.decorView.let { view ->
          view.systemUiVisibility.let {
            view.systemUiVisibility = it or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
          }
        }
      }
    }
  }

  abstract fun providePresenter(): BaseContract.BasePresenter
}