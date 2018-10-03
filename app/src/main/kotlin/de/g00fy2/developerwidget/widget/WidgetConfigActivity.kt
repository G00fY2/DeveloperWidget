package de.g00fy2.developerwidget.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.about.AboutActivity
import kotlinx.android.synthetic.main.activity_widget_config.apply_button
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.Main
import kotlin.coroutines.CoroutineContext

class WidgetConfigActivity : AppCompatActivity(), CoroutineScope {

  private lateinit var job: Job
  private var updateExistingWidget = false
  private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
  private var onApplyClickListener: View.OnClickListener = View.OnClickListener {
    val appWidgetManager = AppWidgetManager.getInstance(this)
    WidgetProvider.updateWidget(this, appWidgetManager, widgetId)

    val resultValue = Intent()
    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
    setResult(Activity.RESULT_OK, resultValue)
    finish()
  }

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    job = Job()
    setResult(Activity.RESULT_CANCELED)
    setContentView(R.layout.activity_widget_config)
    supportActionBar?.elevation = 0f

    val extras = intent.extras
    if (extras != null) {
      widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
      updateExistingWidget = extras.getBoolean(EXTRA_APPWIDGET_UPDATE_EXISTING)
    }

    if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish()
      return
    }

    apply_button.setOnClickListener(onApplyClickListener)
    if (updateExistingWidget) {
      apply_button.setText(R.string.update_widget)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    job.cancel()
  }

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater: MenuInflater = menuInflater
    inflater.inflate(R.menu.configuration_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.about_button -> {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  companion object {
    const val EXTRA_APPWIDGET_UPDATE_EXISTING = "updateExistingWidget"
  }
}
