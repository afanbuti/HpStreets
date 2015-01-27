package com.limon.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.limon.task.NewsService;

public class NewsWidgetProvider extends AppWidgetProvider {

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		NewsService.updateAppWidgetIds(appWidgetIds);
		context.startService(new Intent(context, NewsService.class));
	}

	// public static RemoteViews updateAppWidget(Context context,List<RssNews>
	// list) {
	// RemoteViews views = new
	// RemoteViews(context.getPackageName(),R.layout.appwidget_layout);
	// if (list.size() > 3) {
	// views.setTextViewText(R.id.textView01, list.get(0).title);
	// views.setTextViewText(R.id.textView02, list.get(1).title);
	// views.setTextViewText(R.id.textView03, list.get(2).title);
	// }
	//
	// Intent detailIntent=new Intent(context,NewsSiteList.class);
	//		
	// PendingIntent pending=PendingIntent.getActivity(context, 0, detailIntent,
	// 0);
	// views.setOnClickPendingIntent(R.id.textView01, pending);
	// views.setOnClickPendingIntent(R.id.textView02, pending);
	// views.setOnClickPendingIntent(R.id.textView03, pending);
	// return views;
	// }

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

}