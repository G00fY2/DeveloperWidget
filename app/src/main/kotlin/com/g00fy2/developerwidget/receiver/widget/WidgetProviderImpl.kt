package com.g00fy2.developerwidget.receiver.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.SparseArray
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.g00fy2.developerwidget.BuildConfig
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.apkinstall.ApkActivity
import com.g00fy2.developerwidget.activities.appmanager.AppsActivity
import com.g00fy2.developerwidget.activities.widgetconfig.WidgetConfigActivity
import com.g00fy2.developerwidget.controllers.DayNightController
import com.g00fy2.developerwidget.controllers.DayNightControllerImpl
import com.g00fy2.developerwidget.data.DeviceDataItem
import com.g00fy2.developerwidget.data.DeviceDataSource
import com.g00fy2.developerwidget.data.DeviceDataSourceImpl
import com.g00fy2.developerwidget.data.WidgetsPreferencesDataSource
import dagger.android.AndroidInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class WidgetProviderImpl : AppWidgetProvider() {

  @Inject
  lateinit var deviceDataSource: DeviceDataSource
  @Inject
  lateinit var widgetsPreferencesDataSource: WidgetsPreferencesDataSource
  @Inject
  lateinit var dayNightController: DayNightController

  private lateinit var appWidgetManager: AppWidgetManager
  private lateinit var context: Context

  override fun onReceive(context: Context, intent: Intent) {
    AndroidInjection.inject(this, context)
    this.context = context
    this.appWidgetManager = AppWidgetManager.getInstance(context)
    super.onReceive(context, intent)

    if (intent.extras?.getBoolean(WidgetConfigActivity.EXTRA_APPWIDGET_FROM_PIN_APP) == true) {
      val widgetId = intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID) ?: AppWidgetManager.INVALID_APPWIDGET_ID
      val customDeviceName = intent.extras?.getString(WidgetConfigActivity.EXTRA_APPWIDGET_CUSTOM_DEVICE_NAME) ?: ""
      if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID && customDeviceName.isNotBlank()) {
        widgetsPreferencesDataSource.saveCustomDeviceName(widgetId, customDeviceName)
      }
      context.sendBroadcast(Intent(WidgetConfigActivity.EXTRA_APPWIDGET_CLOSE_CONFIGURE))
    }
    if (intent.action == UPDATE_WIDGET_MANUALLY_ACTION) {
      if (intent.extras?.getBoolean(DayNightControllerImpl.UPDATE_WIDGET_THEME) == true) {
        onUpdate(
          context,
          appWidgetManager,
          appWidgetManager.getAppWidgetIds(ComponentName(context, WidgetProviderImpl::class.java))
        )
      } else {
        intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)?.let {
          onUpdate(context, appWidgetManager, intArrayOf(it))
        }
      }
    }
  }

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    GlobalScope.launch {
      withContext(Dispatchers.IO) {
        val data = deviceDataSource.getStaticDeviceData()
        val customDeviceNames = widgetsPreferencesDataSource.getCustomDeviceNames(appWidgetIds)
        updateWidgetData(appWidgetIds, data, customDeviceNames)
      }
    }
  }

  override fun onDeleted(context: Context?, appWidgetIds: IntArray) {
    Timber.d("onDeleted widget %s", appWidgetIds.first())
    widgetsPreferencesDataSource.clearWidgetPreferences(appWidgetIds.first())
  }

  private fun updateWidgetData(
    appWidgetIds: IntArray,
    data: Map<String, DeviceDataItem>,
    customDeviceNames: SparseArray<String>
  ) {
    val layout = when (dayNightController.getCurrentDefaultMode()) {
      AppCompatDelegate.MODE_NIGHT_NO -> R.layout.appwidget_layout_day
      AppCompatDelegate.MODE_NIGHT_YES -> R.layout.appwidget_layout_night
      else -> R.layout.appwidget_layout
    }
    for (widgetId in appWidgetIds) {
      Timber.d("onUpdate widget $widgetId")
      RemoteViews(context.packageName, R.layout.empty_layout).let {
        appWidgetManager.updateAppWidget(widgetId, it)
      }
      RemoteViews(context.packageName, layout).let {
        updateWidgetDeviceData(data, customDeviceNames.get(widgetId) ?: "", it)
        updateWidgetButtonIntents(widgetId, it)
        appWidgetManager.updateAppWidget(widgetId, it)
      }
    }
  }

  private fun updateWidgetDeviceData(data: Map<String, DeviceDataItem>, customDeviceName: String, views: RemoteViews) {
    if (customDeviceName.isNotBlank()) {
      views.setTextViewText(R.id.device_info_textview, customDeviceName)
    } else {
      data[DeviceDataSourceImpl.DEVICE_NAME]?.let { name ->
        views.setTextViewText(R.id.device_info_textview, name.value)
      }
    }
    data[DeviceDataSourceImpl.RELEASE]?.let { release ->
      views.setTextViewText(R.id.release_textview, context.getString(release.title) + " " + release.value)
    }
    data[DeviceDataSourceImpl.SDK]?.let { sdk ->
      views.setTextViewText(R.id.sdk_int_textview, context.getString(sdk.title) + " " + sdk.value)
    }
    views.setImageViewBitmap(
      R.id.apps_imageview,
      AppCompatResources.getDrawable(context, R.drawable.ic_apps_grid)?.toBitmap()
    )
    views.setImageViewBitmap(
      R.id.apk_imageview,
      AppCompatResources.getDrawable(context, R.drawable.ic_apps)?.toBitmap()
    )
  }

  private fun updateWidgetButtonIntents(widgetId: Int, views: RemoteViews) {
    val configIntent = Intent(context, WidgetConfigActivity::class.java).apply {
      putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
      putExtra(WidgetConfigActivity.EXTRA_APPWIDGET_UPDATE_EXISTING, true)
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val configPendingIntent =
      PendingIntent.getActivity(context, widgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.device_info_linearlayout, configPendingIntent)

    val appIntent =
      Intent(context, AppsActivity::class.java).apply { putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId) }
    val appPendingIntent =
      PendingIntent.getActivity(context, widgetId, appIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.manage_apps_linearlayout, appPendingIntent)

    val apkIntent = Intent(context, ApkActivity::class.java)
    val apkPendingIntent = PendingIntent.getActivity(context, widgetId, apkIntent, 0)
    views.setOnClickPendingIntent(R.id.install_apk_linearlayout, apkPendingIntent)
  }

  companion object {
    const val UPDATE_WIDGET_MANUALLY_ACTION = BuildConfig.APPLICATION_ID + ".APPWIDGET_MANUAL_UPDATE"
  }
}