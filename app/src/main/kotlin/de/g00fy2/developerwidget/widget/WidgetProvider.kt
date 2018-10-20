package de.g00fy2.developerwidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.apkinstall.ApkActivity
import de.g00fy2.developerwidget.util.DeviceDataUtils
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

      views.setTextViewText(R.id.device_info_textview, DeviceDataUtils.deviceName)
      views.setTextViewText(R.id.release_textview, DeviceDataUtils.androidVersion)
      views.setTextViewText(R.id.sdk_int_textview, DeviceDataUtils.androidApiLevel)

      val configIntent = Intent(context, WidgetConfigActivity::class.java)
      configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
      configIntent.putExtra(WidgetConfigActivity.EXTRA_APPWIDGET_UPDATE_EXISTING, true)
      val configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, PendingIntent.FLAG_UPDATE_CURRENT)
      views.setOnClickPendingIntent(R.id.device_info_linearlayout, configPendingIntent)

      val settingsIntent = Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
      val settingsPendingIntent = PendingIntent.getActivity(context, 0, settingsIntent, 0)
      views.setOnClickPendingIntent(R.id.developer_settings_linearlayout, settingsPendingIntent)

      val apkIntent = Intent(context, ApkActivity::class.java)
      apkIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      val apkPendingIntent = PendingIntent.getActivity(context, 0, apkIntent, 0)
      views.setOnClickPendingIntent(R.id.install_apk_linearlayout, apkPendingIntent)

      appWidgetManager.updateAppWidget(widgetId, views)
    }
  }
}
