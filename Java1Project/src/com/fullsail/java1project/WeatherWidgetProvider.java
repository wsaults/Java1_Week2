package com.fullsail.java1project;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.widget.TableLayout;

public class WeatherWidgetProvider extends AppWidgetProvider {

	TableLayout tableLayout;

	public void onUpadte(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		appWidgetManager.updateAppWidget(appWidgetIds, rv);
	}

	public void onDelete(Context context, int[] appWidgetIds) {

	}
}
