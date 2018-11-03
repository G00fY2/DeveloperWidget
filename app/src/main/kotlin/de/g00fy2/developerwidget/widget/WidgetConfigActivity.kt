package de.g00fy2.developerwidget.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.about.AboutActivity
import de.g00fy2.developerwidget.base.BaseActivity
import kotlinx.android.synthetic.main.activity_widget_config.*

class WidgetConfigActivity : BaseActivity() {

  override val layoutRes = R.layout.activity_widget_config
  private var updateExistingWidget = false
  private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setResult(Activity.RESULT_CANCELED)
    supportActionBar?.elevation = 0f

    intent.extras?.let {
      widgetId = it.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
      updateExistingWidget = it.getBoolean(EXTRA_APPWIDGET_UPDATE_EXISTING)
    }

    if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish()
      return
    }

    apply_button.setOnClickListener {
      val appWidgetManager = AppWidgetManager.getInstance(this)
      WidgetProvider.updateWidget(this, appWidgetManager, widgetId)

      val resultValue = Intent()
      resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
      setResult(Activity.RESULT_OK, resultValue)
      finish()
    }
    if (updateExistingWidget) apply_button.setText(R.string.update_widget)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.configuration_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.about_button -> {
        val intent = Intent(this, AboutActivity::class.java)
        if (!updateExistingWidget && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          intent.flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        }
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
