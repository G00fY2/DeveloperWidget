package com.g00fy2.developerwidget.activities.widgetconfig

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.about.AboutActivity
import com.g00fy2.developerwidget.base.BaseActivity
import com.g00fy2.developerwidget.base.BaseContract.BasePresenter
import com.g00fy2.developerwidget.data.DeviceDataItem
import com.g00fy2.developerwidget.databinding.ActivityWidgetConfigBinding
import com.g00fy2.developerwidget.ktx.doOnApplyWindowInsets
import com.g00fy2.developerwidget.ktx.gesturalNavigationMode
import com.g00fy2.developerwidget.ktx.hideKeyboard
import com.g00fy2.developerwidget.ktx.showKeyboard
import com.g00fy2.developerwidget.receiver.widget.WidgetProviderImpl
import javax.inject.Inject

class WidgetConfigActivity : BaseActivity(), WidgetConfigContract.WidgetConfigView {

  @Inject
  lateinit var presenter: WidgetConfigContract.WidgetConfigPresenter

  private lateinit var binding: ActivityWidgetConfigBinding
  private lateinit var adapter: DeviceDataAdapter
  private var updateExistingWidget = false
  private var launchedFromAppLauncher = true
  private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
  private val editDrawable by lazy { initEditDrawable() }

  private val closeConfigureActivityReceiver by lazy {
    object : BroadcastReceiver() {
      override fun onReceive(context: Context?, intent: Intent?) {
        presenter.showHomescreen()
        this@WidgetConfigActivity.finish()
      }
    }
  }

  override fun providePresenter(): BasePresenter = presenter

  override fun setViewBinding(): ViewBinding {
    binding = ActivityWidgetConfigBinding.inflate(layoutInflater)
    return binding
  }

  override fun onResume() {
    super.onResume()
    resetView()
  }

  override fun onDestroy() {
    super.onDestroy()
    if (VERSION.SDK_INT >= VERSION_CODES.O) unregisterReceiver(closeConfigureActivityReceiver)
  }

  override fun initView() {
    setResult(Activity.RESULT_CANCELED)

    intent.extras?.let {
      widgetId = it.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
      updateExistingWidget = it.getBoolean(EXTRA_APPWIDGET_UPDATE_EXISTING)
      launchedFromAppLauncher = !(widgetId != AppWidgetManager.INVALID_APPWIDGET_ID || updateExistingWidget)
    }

    if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID && !launchedFromAppLauncher) {
      finish()
      return
    }

    setActionbarElevationListener(binding.widgetConfigRootScrollview)
    binding.widgetConfigRootScrollview.apply {
      viewTreeObserver.addOnScrollChangedListener {
        val scrollableRange = getChildAt(0).bottom - height + paddingBottom
        val fabOffset = (binding.shareFab.height / 2) + binding.shareFab.marginBottom
        if (scrollY < scrollableRange - fabOffset) {
          binding.shareFab.hide()
        } else {
          binding.shareFab.show()
        }
        if (VERSION.SDK_INT >= VERSION_CODES.O_MR1 && !gesturalNavigationMode()) {
          clipToPadding = (scrollY >= scrollableRange)
        }
      }
    }

    adapter = DeviceDataAdapter()
    binding.recyclerview.setHasFixedSize(false)
    binding.recyclerview.layoutManager = LinearLayoutManager(this)
    binding.recyclerview.isNestedScrollingEnabled = false
    binding.recyclerview.adapter = adapter

    // register broadcast receiver to finish activity after pin widget
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      registerReceiver(closeConfigureActivityReceiver, IntentFilter(EXTRA_APPWIDGET_CLOSE_CONFIGURE))
    }
    // set up webview pre oreo to get implementation information
    if (VERSION.SDK_INT in VERSION_CODES.LOLLIPOP until VERSION_CODES.O) {
      try {
        WebView(this)
      } catch (e: Resources.NotFoundException) {
        WebView(applicationContext)
      }
    }

    binding.deviceTitleEdittextview.apply {
      onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
          presenter.setCustomDeviceName(binding.deviceTitleEdittextview.text.toString())
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
    binding.shareFab.setOnClickListener { presenter.shareDeviceData() }
    if (VERSION.SDK_INT >= VERSION_CODES.O_MR1) {
      binding.widgetConfigRootScrollview.doOnApplyWindowInsets { view, insets, padding ->
        view.updatePadding(bottom = padding.bottom + insets.systemWindowInsetBottom)
      }
    }
    resetView()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.configuration_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return if (item.itemId == R.id.about_button) {
      startActivity(Intent(this, AboutActivity::class.java))
      true
    } else {
      super.onOptionsItemSelected(item)
    }
  }

  override fun showDeviceData(data: List<Pair<String, DeviceDataItem>>) = adapter.submitList(data)

  override fun setDeviceTitle(title: String) {
    binding.deviceTitleTextview.text = title
    binding.deviceTitleTextview.visibility = View.VISIBLE
    binding.deviceTitleEdittextview.setText(title)
    binding.deviceTitleEdittextview.setSelection(binding.deviceTitleEdittextview.text.length)
  }

  override fun setDeviceTitleHint(hint: String) {
    binding.deviceTitleEdittextview.hint = hint
  }

  override fun setSubtitle(data: Pair<String, String>) {
    binding.deviceSubtitleTextview.text = getString(R.string.subtitle).format(data.first, data.second)
  }

  override fun dispatchTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
      currentFocus.let {
        if (it != null && it == binding.deviceTitleEdittextview) {
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

  private fun resetView() {
    val showAddWidget = (!launchedFromAppLauncher || (widgetCount() < 1 && isPinAppWidgetSupported()))
    if (showAddWidget) {
      binding.applyButton.apply {
        visibility = View.VISIBLE
        when {
          launchedFromAppLauncher -> {
            setOnClickListener { pinAppWidget() }
            setText(R.string.create_widget)
          }
          updateExistingWidget -> {
            setOnClickListener { updateWidgetAndFinish(true) }
            setText(R.string.update_widget)
          }
          else -> {
            setOnClickListener { updateWidgetAndFinish(false) }
            setText(R.string.add_widget)
          }
        }
      }
    } else {
      binding.applyButton.visibility = View.GONE
    }
    binding.deviceTitleTextview.apply {
      setOnClickListener { toggleDeviceNameEdit(true) }
      if (showAddWidget) {
        isClickable = true
        setCompoundDrawables(null, null, editDrawable, null)
        setPadding(
          paddingLeft,
          paddingTop,
          (compoundDrawablePadding * 2) + (editDrawable?.intrinsicWidth ?: 0),
          paddingBottom
        )
      } else {
        isClickable = false
        setCompoundDrawables(null, null, null, null)
        setPadding(paddingLeft, paddingTop, (16 * resources.displayMetrics.density).toInt(), paddingBottom)
      }
    }
  }

  private fun toggleDeviceNameEdit(editable: Boolean) {
    binding.deviceTitleTextview.visibility = if (editable) View.INVISIBLE else View.VISIBLE
    binding.deviceTitleEdittextview.visibility = if (editable) View.VISIBLE else View.INVISIBLE
    if (editable) {
      binding.deviceTitleEdittextview.requestFocus()
      binding.deviceTitleEdittextview.showKeyboard()
    } else {
      binding.deviceTitleEdittextview.hideKeyboard()
    }
  }

  private fun updateWidgetAndFinish(existing: Boolean) {
    presenter.setCustomDeviceName(binding.deviceTitleEdittextview.text.toString(), true)
    if (!existing) {
      setResult(Activity.RESULT_OK, Intent().apply { putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId) })
    }
    sendBroadcast(Intent(applicationContext, WidgetProviderImpl::class.java).apply {
      action = WidgetProviderImpl.UPDATE_WIDGET_MANUALLY_ACTION
      putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
    })
    finish()
  }

  private fun pinAppWidget() {
    var supported = false
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      getSystemService<AppWidgetManager>()?.let { appWidgetManager ->
        if (appWidgetManager.isRequestPinAppWidgetSupported) {
          supported = true
          val successCallback = PendingIntent.getBroadcast(
            this, 0, Intent(applicationContext, WidgetProviderImpl::class.java).apply {
              action = WidgetProviderImpl.UPDATE_WIDGET_MANUALLY_ACTION
              putExtra(EXTRA_APPWIDGET_FROM_PIN_APP, true)
              putExtra(EXTRA_APPWIDGET_CUSTOM_DEVICE_NAME, binding.deviceTitleEdittextview.text.toString())
            },
            PendingIntent.FLAG_UPDATE_CURRENT
          )
          appWidgetManager.requestPinAppWidget(
            ComponentName(applicationContext, WidgetProviderImpl::class.java),
            null,
            successCallback
          )
        }
      }
    }
    if (!supported) presenter.showManuallyAddWidgetNotice()
  }

  private fun isPinAppWidgetSupported() =
    VERSION.SDK_INT >= VERSION_CODES.O && getSystemService<AppWidgetManager>()?.isRequestPinAppWidgetSupported ?: false

  private fun widgetCount() = AppWidgetManager.getInstance(this).getAppWidgetIds(
    ComponentName(
      applicationContext,
      WidgetProviderImpl::class.java
    )
  ).size

  private fun initEditDrawable(): Drawable? {
    return AppCompatResources.getDrawable(this, R.drawable.ic_edit)?.apply {
      if (VERSION.SDK_INT >= VERSION_CODES.Q) {
        colorFilter =
          BlendModeColorFilter(ResourcesCompat.getColor(resources, R.color.iconTintColor, null), BlendMode.SRC_IN)
      } else {
        @Suppress("DEPRECATION")
        setColorFilter(ResourcesCompat.getColor(resources, R.color.iconTintColor, null), PorterDuff.Mode.SRC_IN)
      }
      setBounds(0, 0, intrinsicWidth, intrinsicHeight)
      alpha = 128
    }
  }

  companion object {
    const val EXTRA_APPWIDGET_CLOSE_CONFIGURE = "EXTRA_APPWIDGET_CLOSE_CONFIGURE"
    const val EXTRA_APPWIDGET_UPDATE_EXISTING = "UPDATE_EXISTING_WIDGET"
    const val EXTRA_APPWIDGET_FROM_PIN_APP = "EXTRA_APPWIDGET_FROM_PIN_APP"
    const val EXTRA_APPWIDGET_CUSTOM_DEVICE_NAME = "EXTRA_APPWIDGET_CUSTOM_DEVICE_NAME"
  }
}