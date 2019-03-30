package com.g00fy2.developerwidget.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    lifecycle.addObserver(providePresenter())
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

  abstract fun providePresenter(): BaseContract.BasePresenter
}