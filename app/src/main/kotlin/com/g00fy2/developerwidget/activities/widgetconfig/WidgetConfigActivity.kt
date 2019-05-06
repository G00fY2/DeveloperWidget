package com.g00fy2.developerwidget.activities.widgetconfig

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.Rect
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
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

  @Inject
  lateinit var presenter: WidgetConfigContract.WidgetConfigPresenter

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
          presenter.setCustomDeviceName(device_title_edittextview.text.toString(), true)
          sendBroadcast(Intent(applicationContext, WidgetProviderImpl::class.java).apply {
            action = WidgetProviderImpl.UPDATE_WIDGET_ACTION
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
          })
          finish()
        }
      } else {
        setOnClickListener {
          presenter.setCustomDeviceName(device_title_edittextview.text.toString(), true)
          setResult(Activity.RESULT_OK, Intent().apply { putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId) })
          finish()
        }
      }
    }
    device_title_textview.apply {
      setOnClickListener {
        device_title_textview.visibility = View.INVISIBLE
        device_title_edittextview.visibility = View.VISIBLE
        device_title_edittextview.requestFocus()
        showKeyboard(device_title_edittextview)
      }
    }
    device_title_edittextview.apply {
      setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
          hideKeyboard(this)
          device_title_textview.visibility = View.VISIBLE
          device_title_edittextview.visibility = View.INVISIBLE
          presenter.setCustomDeviceName(device_title_edittextview.text.toString())
        }
      }
      setOnEditorActionListener { v, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          v.clearFocus()
        }
        true
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
        startActivity(Intent(this, AboutActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun showDeviceData(data: List<Pair<String, DeviceDataItem>>) {
    data.toMap().let {
      if (device_title_textview.text.isEmpty()) {
        setDeviceTitle(it[DeviceDataSourceImpl.DEVICE_NAME]?.value ?: "")
      }
      setWidgetFields(it)
    }
    adapter.submitList(data)
  }

  override fun setDeviceTitle(title: String) {
    device_title_textview.text = title
    device_title_edittextview.setText(title)
    device_title_edittextview.setSelection(device_title_edittextview.text.length)
  }

  override fun dispatchTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
      currentFocus.let {
        if (it != null && it == device_title_edittextview) {
          Rect().let { rect ->
            it.getGlobalVisibleRect(rect)
            if (!rect.contains(event.rawX.toInt(), event.rawY.toInt())) {
              it.clearFocus()
            }
          }
        }
      }
    }
    return super.dispatchTouchEvent(event)
  }

  private fun setWidgetFields(data: Map<String, DeviceDataItem>) {
    device_title_edittextview.hint = data[DeviceDataSourceImpl.DEVICE_NAME]?.value ?: ""
    var subtitle = data[DeviceDataSourceImpl.RELEASE]?.let { getString(it.title) + " " + it.value + " | " } ?: ""
    subtitle += data[DeviceDataSourceImpl.SDK]?.let { getString(it.title) + " " + it.value }
    device_subtitle_textview.text = subtitle
  }

  companion object {
    const val EXTRA_APPWIDGET_UPDATE_EXISTING = "UPDATE_EXISTING_WIDGET"
  }
}
