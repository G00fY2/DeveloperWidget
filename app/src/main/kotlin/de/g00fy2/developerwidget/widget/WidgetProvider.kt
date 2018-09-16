package de.g00fy2.developerwidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import de.g00fy2.developerwidget.apkinstall.ApkActivity
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.dataprovider.DeviceDataProvider


class WidgetProvider : AppWidgetProvider() {

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    for (widgetId in appWidgetIds) {
      updateWidget(context, appWidgetManager, widgetId)
    }
  }

  companion object {
    internal fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
      val deviceDataProvider = DeviceDataProvider(context)
      val views = RemoteViews(context.packageName, R.layout.appwidget_layout)

      views.setTextViewText(R.id.device_info, deviceDataProvider.deviceInfo)
      views.setTextViewText(R.id.release, deviceDataProvider.versionAndCodename)
      views.setTextViewText(R.id.sdk_int, deviceDataProvider.sdkVersion)

      val settingsIntent = Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
      val settingsPendingIntent = PendingIntent.getActivity(context, 0, settingsIntent, 0)
      views.setOnClickPendingIntent(R.id.developer_settings, settingsPendingIntent)

      val apkIntent = Intent(context, ApkActivity::class.java)
      val apkPendingIntent = PendingIntent.getActivity(context, 0, apkIntent, 0)
      views.setOnClickPendingIntent(R.id.install_apk, apkPendingIntent)

      appWidgetManager.updateAppWidget(widgetId, views)
    }
  }
}
