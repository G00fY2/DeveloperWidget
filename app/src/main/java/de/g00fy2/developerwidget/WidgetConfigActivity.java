package de.g00fy2.developerwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import java.lang.reflect.Field;

public class WidgetConfigActivity extends AppCompatActivity {

  private int widgetId;
  private RemoteViews remoteViews;
  private Context context;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_widget_config);

    setResult(RESULT_CANCELED);
    context = this;
    remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);

    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      widgetId =
          extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish();
    }

    Button apply = (Button) findViewById(R.id.apply);
    apply.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        setWidgetText();

        AppWidgetManager.getInstance(context).updateAppWidget(widgetId, remoteViews);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, resultValue);
        finish();
      }
    });
  }

  private void setWidgetText() {
    remoteViews.setTextViewText(R.id.device_info, getDeviceInfo());
    remoteViews.setTextViewText(R.id.release, getVersionAndCodename());
    remoteViews.setTextViewText(R.id.sdk_int, getSDKVersion());
    remoteViews.setTextViewText(R.id.app_info, getAppInfo());
  }

  private String getDeviceInfo() {
    return Build.MANUFACTURER + " " + Build.MODEL;
  }

  private String getVersionAndCodename() {
    return "Android " + Build.VERSION.RELEASE + " (" + getAndroidCodename() + ")";
  }

  private String getAndroidCodename() {
    Field[] fields = Build.VERSION_CODES.class.getFields();
    return fields[Build.VERSION.SDK_INT + 1].getName();
  }

  private String getSDKVersion() {
    return "API " + Integer.toString(Build.VERSION.SDK_INT);
  }

  private String getAppInfo() {
    PackageInfo pInfo;
    String version = "";
    int verCode = 0;
    try {
      pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
      version = pInfo.versionName;
      verCode = pInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    return version + " (" + Integer.toString(verCode) + ")";
  }
}
