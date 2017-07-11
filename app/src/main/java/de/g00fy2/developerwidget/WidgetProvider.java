package de.g00fy2.developerwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

/**
 * Created by Thomas Wirth on 11.07.2017.
 */

public class WidgetProvider extends AppWidgetProvider {

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    String apiVersion = "API " + Build.VERSION.SDK_INT;

    for (int widgetId : appWidgetIds) {
      RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);
      remoteViews.setTextViewText(R.id.sdk_int, apiVersion);

      Intent intent = new Intent(context, WidgetProvider.class);
      intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
      appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }
  }
}
