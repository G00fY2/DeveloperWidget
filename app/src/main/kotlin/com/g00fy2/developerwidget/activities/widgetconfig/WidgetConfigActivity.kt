package com.g00fy2.developerwidget.activities.widgetconfig

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.about.AboutActivity
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.data.DeviceDataItem
import com.g00fy2.developerwidget.data.DeviceDataSourceImpl
import com.g00fy2.developerwidget.receiver.widget.WidgetProviderImpl
import kotlinx.android.synthetic.main.activity_widget_config.*
import javax.inject.Inject

class WidgetConfigActivity : BaseActivity(R.layout.activity_widget_config), WidgetConfigContract.WidgetConfigView {

  @Inject lateinit var presenter: WidgetConfigContract.WidgetConfigPresenter

  private var updateExistingWidget = false
  private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
  private lateinit var adapter: DeviceDataAdapter

  override fun providePresenter(): BasePresenter = presenter

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setResult(Activity.RESULT_CANCELED)

    intent.extras?.let {
      widgetId = it.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
      updateExistingWidget = it.getBoolean(EXTRA_APPWIDGET_UPDATE_EXISTING)
    }

    if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish()
      return
    }

    setActionbarElevationListener(widget_config_root_scrollview)

    adapter = DeviceDataAdapter()
    recyclerview.setHasFixedSize(false)
    recyclerview.layoutManager = LinearLayoutManager(this)
    recyclerview.isNestedScrollingEnabled = false
    recyclerview.adapter = adapter

    // set up webview pre oreo to get implementation information
    if (VERSION.SDK_INT in VERSION_CODES.LOLLIPOP until VERSION_CODES.O) {
      WebView(this)
    }

    apply_button.apply {
      if (updateExistingWidget) {
        setText(R.string.update_widget)
        setOnClickListener {
          sendBroadcast(Intent(applicationContext, WidgetProviderImpl::class.java).apply {
            action = WidgetProviderImpl.UPDATE_WIDGET_ACTION
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
          })
          finish()
        }
      } else {
        setOnClickListener {
          setResult(Activity.RESULT_OK, Intent().apply { putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId) })
          finish()
        }
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.configuration_menu, menu)
    menu.findItem(R.id.toggle_mode).let {
      when (dayNightController.getCurrentDefaultMode()) {
        AppCompatDelegate.MODE_NIGHT_YES -> {
          it.icon = ContextCompat.getDrawable(this, R.drawable.ic_mode_night)
          it.title = resources.getString(R.string.night_mode)
        }
        AppCompatDelegate.MODE_NIGHT_NO -> {
          it.icon = ContextCompat.getDrawable(this, R.drawable.ic_mode_day)
          it.title = resources.getString(R.string.day_mode)
        }
        else -> {
          it.icon = ContextCompat.getDrawable(this, R.drawable.ic_mode_auto)
          it.title = resources.getString(R.string.auto_mode)
        }
      }
    }
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.toggle_mode -> {
        dayNightController.toggleMode(this)
        updateWidgetTheme()
        invalidateOptionsMenu()
        true
      }
      R.id.about_button -> {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun updateWidgetTheme() {
    sendBroadcast(Intent(applicationContext, WidgetProviderImpl::class.java).apply {
      action = WidgetProviderImpl.UPDATE_WIDGET_ACTION
      putExtra(UPDATE_WIDGET_THEME, true)
    })
  }

  override fun showDeviceData(data: List<Pair<String, DeviceDataItem>>) {
    setWidgetFields(data.toMap())
    adapter.submitList(data)
  }

  private fun setWidgetFields(data: Map<String, DeviceDataItem>) {
    device_title_textview.text = data[DeviceDataSourceImpl.DEVICE_NAME]?.value ?: ""
    var subtitle = data[DeviceDataSourceImpl.RELEASE]?.let { getString(it.title) + " " + it.value + " | " } ?: ""
    subtitle += data[DeviceDataSourceImpl.SDK]?.let { getString(it.title) + " " + it.value }
    device_subtitle_textview.text = subtitle
  }

  companion object {
    const val EXTRA_APPWIDGET_UPDATE_EXISTING = "UPDATE_EXISTING_WIDGET"
    const val UPDATE_WIDGET_THEME = "UPDATE_WIDGET_THEME"
  }
}
