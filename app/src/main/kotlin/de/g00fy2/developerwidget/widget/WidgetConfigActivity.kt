package de.g00fy2.developerwidget.widget

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RemoteViews

import androidx.appcompat.app.AppCompatActivity
import de.g00fy2.developerwidget.ApkActivity
import de.g00fy2.developerwidget.R
import de.g00fy2.developerwidget.dataProvider.DeviceDataProvider

class WidgetConfigActivity : AppCompatActivity() {

  private var widgetId: Int = 0
  private var remoteViews: RemoteViews? = null
  private var context: Context? = null
  private var deviceDataProvider: DeviceDataProvider? = null

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_widget_config)

    setResult(Activity.RESULT_CANCELED)
    context = this
    remoteViews = RemoteViews(context!!.packageName, R.layout.appwidget_layout)

    val intent = intent
    val extras = intent.extras
    if (extras != null) {
      widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    }

    if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish()
    }

    val apply = findViewById<View>(R.id.apply) as Button
    apply.setOnClickListener {
      initWidget()

      AppWidgetManager.getInstance(context).updateAppWidget(widgetId, remoteViews)

      val resultValue = Intent()
      resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
      setResult(Activity.RESULT_OK, resultValue)
      finish()
    }
  }

  private fun initWidget() {
    if (deviceDataProvider == null) deviceDataProvider = DeviceDataProvider(this.context!!)

    remoteViews!!.setTextViewText(R.id.device_info, deviceDataProvider!!.deviceInfo)
    remoteViews!!.setTextViewText(R.id.release, deviceDataProvider!!.versionAndCodename)
    remoteViews!!.setTextViewText(R.id.sdk_int, deviceDataProvider!!.sdkVersion)

    val settingsIntent = Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
    val settingsPendingIntent = PendingIntent.getActivity(context, 0, settingsIntent, 0)
    remoteViews!!.setOnClickPendingIntent(R.id.developer_settings, settingsPendingIntent)

    val apkIntent = Intent(context, ApkActivity::class.java)
    val apkPendingIntent = PendingIntent.getActivity(context, 0, apkIntent, 0)
    remoteViews!!.setOnClickPendingIntent(R.id.install_apk, apkPendingIntent)
  }


}
