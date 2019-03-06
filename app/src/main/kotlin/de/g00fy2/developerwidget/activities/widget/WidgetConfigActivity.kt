package de.g00fy2.developerwidget.activities.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.annotation.ContentView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.activities.about.AboutActivity
import de.g00fy2.developerwidget.base.BaseActivity
import de.g00fy2.developerwidget.data.DeviceDataItem
import de.g00fy2.developerwidget.data.DeviceDataProvider
import kotlinx.android.synthetic.main.activity_widget_config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ContentView(R.layout.activity_widget_config)
class WidgetConfigActivity : BaseActivity(), WidgetConfigContract.WidgetConfigView {

  private var updateExistingWidget = false
  private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
  private lateinit var adapter: DeviceDataAdapter

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setResult(Activity.RESULT_CANCELED)

    intent.extras?.let {
      widgetId = it.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
      updateExistingWidget = it.getBoolean(EXTRA_APPWIDGET_UPDATE_EXISTING)
    }

    setActionbarElevationListener(widget_config_root_scrollview)

    adapter = DeviceDataAdapter()
    recyclerview.setHasFixedSize(true)
    recyclerview.layoutManager = LinearLayoutManager(this)
    recyclerview.isNestedScrollingEnabled = false
    recyclerview.adapter = adapter

    // set up webview pre oreo to get implementation information
    if (VERSION.SDK_INT in VERSION_CODES.LOLLIPOP until VERSION_CODES.O) {
      WebView(this)
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

  override fun onResume() {
    super.onResume()
    launch {
      getDeviceData().let {
        setWidgetFields(it.toMap())
        adapter.notifyDataSetChanged() // workaround to show items
        adapter.submitList(it)
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.configuration_menu, menu)
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

  private fun setWidgetFields(data: Map<String, DeviceDataItem>) {
    device_title_textview.text = data[DeviceDataProvider.DEVICE_NAME]?.value ?: ""
    var subtitle = data[DeviceDataProvider.RELEASE]?.let { getString(it.title) + " " + it.value + " | " } ?: ""
    subtitle += data[DeviceDataProvider.SDK]?.let { getString(it.title) + " " + it.value }
    device_subtitle_textview.text = subtitle
  }

  private suspend fun getDeviceData(): List<Pair<String, DeviceDataItem>> {
    return withContext(Dispatchers.IO) {
      DeviceDataProvider
        .getStaticDeviceData()
        .plus(DeviceDataProvider.getHardwareData(this@WidgetConfigActivity))
        .plus(DeviceDataProvider.getSoftwareInfo(this@WidgetConfigActivity))
        .plus(DeviceDataProvider.getHeaderItems())
        .toList()
        .filter { (_, value) -> value.value.isNotBlank() || value.isHeader }
        .sortedWith(
          compareBy(
            { it.second.category.ordinal },
            { !it.second.isHeader },
            { getString(it.second.title) })
        )
    }
  }

  companion object {
    const val EXTRA_APPWIDGET_UPDATE_EXISTING = "updateExistingWidget"
  }
}