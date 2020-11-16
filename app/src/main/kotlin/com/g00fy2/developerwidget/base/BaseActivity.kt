package com.g00fy2.developerwidget.base

import android.content.res.Configuration
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.controllers.DayNightController
import com.g00fy2.developerwidget.ktx.doOnApplyWindowInsets
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity(private val isDialogActivity: Boolean = false) : DaggerAppCompatActivity() {

  @Inject
  lateinit var dayNightController: DayNightController

  protected abstract val binding: ViewBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.d("Lifecycle: %s1 onCreate %s2", localClassName, hashCode())

    if (isDialogActivity) {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      super.onCreate(savedInstanceState)
      setContentView(binding.root)

      val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
      val height = (resources.displayMetrics.heightPixels * 0.80).toInt()
      window.setLayout(width, height)
    } else {
      super.onCreate(savedInstanceState)
      setContentView(binding.root)
    }

    dayNightController.loadCustomDefaultMode()
    lifecycle.addObserver(providePresenter())
    initCompatNavigationBar()
    initView()
    if (!isDialogActivity) initGestureNavigation()
  }

  override fun onDestroy() {
    Timber.d("Lifecycle: %s1 onDestroy %s2", localClassName, hashCode())
    super.onDestroy()
    lifecycle.removeObserver(providePresenter())
  }

  inline fun <T : ViewBinding> DaggerAppCompatActivity.viewBinding(crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) { bindingInflater.invoke(layoutInflater) }

  @RequiresApi(VERSION_CODES.Q)
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    if (isGesturalNavMode()) {
      getColor(R.color.transparent).let {
        window.navigationBarColor = it
        window.navigationBarDividerColor = it
      }
    }
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

  protected fun isGesturalNavMode(): Boolean {
    return resources.getIdentifier("config_navBarInteractionMode", "integer", "android")
      .takeIf { it != 0 }?.let { resources.getInteger(it) == 2 } ?: false
  }

  private fun initGestureNavigation() {
    if (VERSION.SDK_INT >= VERSION_CODES.O_MR1) {
      // TODO check how to use API 30 features
      @Suppress("DEPRECATION")
      window.decorView.let {
        it.systemUiVisibility.let { flags ->
          it.systemUiVisibility =
            flags or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
      }

      findViewById<View>(Window.ID_ANDROID_CONTENT)?.let {
        it.doOnApplyWindowInsets { view, insets, padding, _ ->
          view.updatePadding(top = padding.top + insets.getInsets(WindowInsetsCompat.Type.systemBars()).top)
        }
      }
    }
  }

  @Suppress("DEPRECATION")
  private fun initCompatNavigationBar() {
    // api 27+ allow applying flag via xml (windowLightNavigationBar)
    if (VERSION.SDK_INT == VERSION_CODES.O && !isNightMode()) {
      window.decorView.let {
        it.systemUiVisibility.let { flags ->
          it.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
      }
    }
  }

  private fun isNightMode() =
    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

  abstract fun providePresenter(): BaseContract.BasePresenter

  abstract fun initView()
}