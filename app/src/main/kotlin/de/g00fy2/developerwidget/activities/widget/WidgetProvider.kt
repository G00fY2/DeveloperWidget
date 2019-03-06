package de.g00fy2.developerwidget.activities.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.activities.apkinstall.ApkActivity
import de.g00fy2.developerwidget.activities.appsettings.AppsActivity
import de.g00fy2.developerwidget.data.DeviceDataProvider
import timber.log.Timber

class WidgetProvider : AppWidgetProvider() {

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    for (widgetId in appWidgetIds) {
      Timber.d("onUpdate")
      updateWidget(context, appWidgetManager, widgetId)
    }
  }

  companion object {
    internal fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
      val views = RemoteViews(context.packageName, R.layout.appwidget_layout)

      DeviceDataProvider.getStaticDeviceData().let {
        it[DeviceDataProvider.DEVICE_NAME]?.let { name ->
          views.setTextViewText(R.id.device_info_textview, name.value)
        }
        it[DeviceDataProvider.RELEASE]?.let { release ->
          views.setTextViewText(R.id.release_textview, context.getString(release.title) + " " + release.value)
        }
        it[DeviceDataProvider.SDK]?.let { sdk ->
          views.setTextViewText(R.id.sdk_int_textview, context.getString(sdk.title) + " " + sdk.value)
        }
      }

      val configIntent = Intent(context, WidgetConfigActivity::class.java)
      configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
      configIntent.putExtra(WidgetConfigActivity.EXTRA_APPWIDGET_UPDATE_EXISTING, true)
      val configPendingIntent = PendingIntent.getActivity(context, widgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT)
      views.setOnClickPendingIntent(R.id.device_info_linearlayout, configPendingIntent)

//      val settingsIntent = Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
//      val settingsPendingIntent = PendingIntent.getActivity(context, 0, settingsIntent, 0)
      val appIntent = Intent(context, AppsActivity::class.java)
      appIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
      val appPendingIntent = PendingIntent.getActivity(context, widgetId, appIntent, PendingIntent.FLAG_UPDATE_CURRENT)
      views.setOnClickPendingIntent(R.id.developer_settings_linearlayout, appPendingIntent)

      val apkIntent = Intent(context, ApkActivity::class.java)
      val apkPendingIntent = PendingIntent.getActivity(context, widgetId, apkIntent, 0)
      views.setOnClickPendingIntent(R.id.install_apk_linearlayout, apkPendingIntent)

      appWidgetManager.updateAppWidget(widgetId, views)
    }
  }
}