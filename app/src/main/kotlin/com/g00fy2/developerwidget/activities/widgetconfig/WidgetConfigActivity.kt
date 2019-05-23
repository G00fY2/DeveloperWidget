package com.g00fy2.developerwidget.activities.widgetconfig

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.PorterDuff
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
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.about.AboutActivity
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.data.DeviceDataItem
import com.g00fy2.developerwidget.receiver.widget.WidgetProviderImpl
import kotlinx.android.synthetic.main.activity_widget_config.*
import javax.inject.Inject

class WidgetConfigActivity : BaseActivity(R.layout.activity_widget_config), WidgetConfigContract.WidgetConfigView {

  @Inject
  lateinit var presenter: WidgetConfigContract.WidgetConfigPresenter

  private var updateExistingWidget = false
  private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
  private lateinit var adapter: DeviceDataAdapter
  private val editDrawable by lazy {
    ResourcesCompat.getDrawable(resources, R.drawable.ic_edit, null)?.apply {
      setColorFilter(ResourcesCompat.getColor(resources, R.color.dividerGrey, null), PorterDuff.Mode.SRC_IN)
      setBounds(0, 0, this.intrinsicWidth, this.intrinsicHeight)
    }
  }

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
      setOnClickListener { toggleDeviceNameEdit(true) }
      setCompoundDrawables(null, null, editDrawable, null)
      setPadding(
        paddingLeft,
        paddingTop,
        (compoundDrawablePadding * 2) + (editDrawable?.intrinsicWidth ?: 0),
        paddingBottom
      )
    }
    device_title_edittextview.apply {
      setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
          presenter.setCustomDeviceName(device_title_edittextview.text.toString())
          toggleDeviceNameEdit(false)
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

  override fun showDeviceData(data: List<Pair<String, DeviceDataItem>>) = adapter.submitList(data)

  override fun setDeviceTitle(title: String) {
    device_title_textview.text = title
    device_title_textview.visibility = View.VISIBLE
    device_title_edittextview.setText(title)
    device_title_edittextview.setSelection(device_title_edittextview.text.length)
  }

  override fun setDeviceTitleHint(hint: String) {
    device_title_edittextview.hint = hint
  }

  override fun setSubtitle(data: Pair<String, String>) {
    device_subtitle_textview.text = getString(R.string.subtitle).format(data.first, data.second)
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

  private fun toggleDeviceNameEdit(editable: Boolean) {
    device_title_textview.visibility = if (editable) View.INVISIBLE else View.VISIBLE
    device_title_edittextview.visibility = if (editable) View.VISIBLE else View.INVISIBLE
    if (editable) {
      device_title_edittextview.requestFocus()
      showKeyboard(device_title_edittextview)
    } else {
      hideKeyboard(device_title_edittextview)
    }
  }

  companion object {
    const val EXTRA_APPWIDGET_UPDATE_EXISTING = "UPDATE_EXISTING_WIDGET"
  }
}
