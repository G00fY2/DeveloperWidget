package de.g00fy2.developerwidget.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import de.g00fy2.developerwidget.R

class WidgetConfigActivity : AppCompatActivity() {

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

    setResult(Activity.RESULT_CANCELED)
    setContentView(R.layout.activity_widget_config)
    supportActionBar?.elevation = 0f
    findViewById<View>(R.id.apply).setOnClickListener(onApplyClickListener)

    val intent = intent
    val extras = intent.extras
    if (extras != null) {
      widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    }

    if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish()
      return
    }
  }
}
