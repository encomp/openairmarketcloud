package com.toolinc.openairmarket.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.toolinc.openairmarket.OpenAirMarketApplication;
import com.toolinc.openairmarket.R;

/** Implementation of App Widget functionality. */
public class SaleWidget extends AppWidgetProvider {

  private Context context;

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
    CharSequence widgetText = context.getString(R.string.appwidget_text);
    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sale_widget);
    views.setTextViewText(R.id.appwidget_text, widgetText);
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    this.context = context;
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  private void inject(Context context) {
    ((OpenAirMarketApplication) context.getApplicationContext())
        .getOpenAirMarketInjector()
        .inject(this);
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}
