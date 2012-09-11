/*
    Copyright (C) 2012 Sweetie Piggy Apps <sweetiepiggyapps@gmail.com>

    This file is part of Buffalo Fox Monkey.

    Buffalo Fox Monkey is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    Buffalo Fox Monkey is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Buffalo Fox Monkey; if not, see <http://www.gnu.org/licenses/>.
*/

package com.sweetiepiggy.buffalofoxmonkey;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider
{
	@Override
	public void onUpdate(Context context, AppWidgetManager app_widget_mgr, int[] app_widget_ids)
	{
		final int N = app_widget_ids.length;

		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int i=0; i < N; i++) {
			int app_widget_id = app_widget_ids[i];

			// Create an Intent to launch ExampleActivity
			Intent intent = new Intent(context, BuffaloFoxMonkeyActivity.class);
			PendingIntent pending_intent = PendingIntent.getActivity(context, 0, intent, 0);

			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
			views.setOnClickPendingIntent(R.id.layout, pending_intent);

			DbAdapter dbHelper = new DbAdapter();
			dbHelper.open(context);
			String b = dbHelper.random_word('b');
			String f = dbHelper.random_word('f');
			String m = dbHelper.random_word('m');
			dbHelper.close();

			views.setTextViewText(R.id.b, b);
			views.setTextViewText(R.id.f, f);
			views.setTextViewText(R.id.m, m);

			// Tell the AppWidgetManager to perform an update on the current app widget
			app_widget_mgr.updateAppWidget(app_widget_id, views);
		}
		super.onUpdate(context, app_widget_mgr, app_widget_ids);
	}
}

