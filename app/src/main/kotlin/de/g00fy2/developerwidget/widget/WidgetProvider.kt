package de.g00fy2.developerwidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import de.g00fy2.developerwidget.ApkActivity
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.dataProvider.DeviceDataProvider


class WidgetProvider : AppWidgetProvider() {

  private lateinit var context: Context

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

    this.context = context
    val deviceDataProvider = DeviceDataProvider(context)

    for (widgetId in appWidgetIds) {
      val remoteViews = RemoteViews(context.packageName, R.layout.appwidget_layout)

      remoteViews.setTextViewText(R.id.device_info, deviceDataProvider.deviceInfo)
      remoteViews.setTextViewText(R.id.release, deviceDataProvider.versionAndCodename)
      remoteViews.setTextViewText(R.id.sdk_int, deviceDataProvider.sdkVersion)

      val settingsIntent = Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
      val settingsPendingIntent = PendingIntent.getActivity(context, 0, settingsIntent, 0)
      remoteViews.setOnClickPendingIntent(R.id.developer_settings, settingsPendingIntent)

      val apkIntent = Intent(context, ApkActivity::class.java)
      val apkPendingIntent = PendingIntent.getActivity(context, 0, apkIntent, 0)
      remoteViews.setOnClickPendingIntent(R.id.install_apk, apkPendingIntent)

      val intent = Intent(context, WidgetProvider::class.java)
      intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
      appWidgetManager.updateAppWidget(widgetId, remoteViews)
    }
  }
}
