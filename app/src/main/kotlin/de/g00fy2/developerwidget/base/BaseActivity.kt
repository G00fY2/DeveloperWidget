package de.g00fy2.developerwidget.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), CoroutineScope {

  protected abstract val layoutRes: Int
  private lateinit var job: Job
  private val Float.px: Float get() = (this * resources.displayMetrics.density)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layoutRes)
    supportActionBar?.elevation = 0f
    job = Job()
  }

  override fun onDestroy() {
    super.onDestroy()
    job.cancel()
  }

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

  fun setActionbarElevationListener(viewGroup: ViewGroup) {
    viewGroup.viewTreeObserver.addOnScrollChangedListener {
      viewGroup.scrollY.toFloat().let {
        if (it <= 0f) {
          supportActionBar?.elevation = 0f
        } else {
          var elevationDp = it / 4 // divide scrollY to increase fade in range
          if (elevationDp > 4f) elevationDp = 4f
          supportActionBar?.elevation = elevationDp.px
        }
      }
    }
  }
}