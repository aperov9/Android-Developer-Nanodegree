package com.example.android.finalapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.android.finalapp.R;
import com.example.android.finalapp.models.Post;

public class PostWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Post post) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.post_widget);

        views.setTextViewText(R.id.widget_subreddit,post.getmSubreddit());
        views.setTextViewText(R.id.widget_title,post.getmTitle());
        views.setTextViewText(R.id.widget_comments,post.getmCommentsCount()+"");
        views.setTextViewText(R.id.widget_ups,post.getmScore()+"");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PostIntentService.startPostIntentService(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateRecipe(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Post post){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, post);
        }
    }
}
