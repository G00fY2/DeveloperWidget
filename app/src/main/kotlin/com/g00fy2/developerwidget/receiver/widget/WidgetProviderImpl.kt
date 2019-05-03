package com.g00fy2.developerwidget.receiver.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatDelegate
import com.g00fy2.developerwidget.BuildConfig
import com.g00fy2.developerwidget.R
import com.g00fy2.developerwidget.activities.apkinstall.ApkActivity
import com.g00fy2.developerwidget.activities.appmanager.AppsActivity
import com.g00fy2.developerwidget.activities.widgetconfig.WidgetConfigActivity
import com.g00fy2.developerwidget.controllers.DayNightController
import com.g00fy2.developerwidget.controllers.DayNightControllerImpl.Companion.UPDATE_WIDGET_THEME
import com.g00fy2.developerwidget.data.DeviceDataItem
import com.g00fy2.developerwidget.data.DeviceDataSourceImpl
import com.g00fy2.developerwidget.receiver.widget.WidgetProviderContract.WidgetProvider
import com.g00fy2.developerwidget.receiver.widget.WidgetProviderContract.WidgetProviderPresenter
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

class WidgetProviderImpl : AppWidgetProvider(), WidgetProvider {

  @Inject
  lateinit var presenter: WidgetProviderPresenter
  @Inject
  lateinit var dayNightController: DayNightController

  private lateinit var appWidgetIds: IntArray
  private lateinit var appWidgetManager: AppWidgetManager
  private lateinit var context: Context

  override fun onReceive(context: Context, intent: Intent) {
    AndroidInjection.inject(this, context)
    super.onReceive(context, intent)

    AppWidgetManager.getInstance(context).let { widgetManager ->
      if (intent.action == UPDATE_WIDGET_ACTION) {
        if (intent.extras?.getBoolean(UPDATE_WIDGET_THEME) == true) {
          onUpdate(
            context,
            widgetManager,
            widgetManager.getAppWidgetIds(ComponentName(context, WidgetProviderImpl::class.java))
          )
        } else {
          intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)?.let {
            onUpdate(context, widgetManager, intArrayOf(it))
          }
        }
      }
    }
  }

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    this.context = context
    this.appWidgetManager = appWidgetManager
    this.appWidgetIds = appWidgetIds
    presenter.getDeviceData()
  }

  override fun updateWidgetData(data: Map<String, DeviceDataItem>) {
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
        updateWidgetDeviceData(data, it)
        updateWidgetButtonIntents(widgetId, it)
        appWidgetManager.updateAppWidget(widgetId, it)
      }
    }
  }

  private fun updateWidgetDeviceData(data: Map<String, DeviceDataItem>, views: RemoteViews) {
    data[DeviceDataSourceImpl.DEVICE_NAME]?.let { name ->
      views.setTextViewText(R.id.device_info_textview, name.value)
    }
    data[DeviceDataSourceImpl.RELEASE]?.let { release ->
      views.setTextViewText(R.id.release_textview, context.getString(release.title) + " " + release.value)
    }
    data[DeviceDataSourceImpl.SDK]?.let { sdk ->
      views.setTextViewText(R.id.sdk_int_textview, context.getString(sdk.title) + " " + sdk.value)
    }
  }

  private fun updateWidgetButtonIntents(widgetId: Int, views: RemoteViews) {
    val configIntent = Intent(context, WidgetConfigActivity::class.java)
    configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
    configIntent.putExtra(WidgetConfigActivity.EXTRA_APPWIDGET_UPDATE_EXISTING, true)
    val configPendingIntent =
      PendingIntent.getActivity(context, widgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.device_info_linearlayout, configPendingIntent)

//    val settingsIntent = Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
//    val settingsPendingIntent = PendingIntent.getActivity(context, 0, settingsIntent, 0)
    val appIntent = Intent(context, AppsActivity::class.java)
    appIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
    val appPendingIntent =
      PendingIntent.getActivity(context, widgetId, appIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.manage_apps_linearlayout, appPendingIntent)

    val apkIntent = Intent(context, ApkActivity::class.java)
    val apkPendingIntent = PendingIntent.getActivity(context, widgetId, apkIntent, 0)
    views.setOnClickPendingIntent(R.id.install_apk_linearlayout, apkPendingIntent)
  }

  companion object {
    const val UPDATE_WIDGET_ACTION = BuildConfig.APPLICATION_ID + ".APPWIDGET_MANUAL_UPDATE"
  }
}
