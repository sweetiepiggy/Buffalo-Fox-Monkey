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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class BuffaloFoxMonkeyActivity extends Activity
{
	private static final String SOURCE_URL = "https://github.com/sweetiepiggy/Buffalo-Fox-Monkey";
	static final String BFM_TWITTER_ADDR = "@BFMradio";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		random_bfm();
		init_word_button(R.id.b, 'b');
		init_word_button(R.id.f, 'f');
		init_word_button(R.id.m, 'm');
		init_try_again_button();
		init_tweet_button();
	}

	private void init_word_button(int id, final char first_letter)
	{
		TextView v = (TextView) findViewById(id);

		v.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);

				RandomTask task = new RandomTask(first_letter);
				task.execute();
			}
		});
	}

	private void init_try_again_button()
	{
		Button try_again_button = (Button) findViewById(R.id.try_again_button);

		try_again_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				random_bfm();
			}
		});
	}

	private void init_tweet_button()
	{
		Button tweet_button = (Button) findViewById(R.id.tweet_button);

		tweet_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String b = ((TextView) findViewById(R.id.b)).getText().toString();
				String f = ((TextView) findViewById(R.id.f)).getText().toString();
				String m = ((TextView) findViewById(R.id.m)).getText().toString();
				String tweet_msg = b + " " + f + " " + m + " "  + BFM_TWITTER_ADDR;
				send_tweet(tweet_msg);
			}
		});
	}

	private void send_tweet(String tweet_msg)
	{
		Intent tweet_intent = new Intent(Intent.ACTION_SEND);
		tweet_intent.putExtra(Intent.EXTRA_TEXT, tweet_msg);
		tweet_intent.setType("text/plain");
		startActivity(Intent.createChooser(tweet_intent,
					getResources().getString(R.string.send_tweet)));
	}

	private void random_bfm()
	{
		((TextView) findViewById(R.id.b)).setVisibility(View.INVISIBLE);
		((TextView) findViewById(R.id.f)).setVisibility(View.INVISIBLE);
		((TextView) findViewById(R.id.m)).setVisibility(View.INVISIBLE);

		RandomTask task = new RandomTask();
		task.execute();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
			startActivity(intent);
			return true;
		case R.id.source:
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(SOURCE_URL), "text/html");
			startActivity(Intent.createChooser(intent, getResources().getString(R.string.open_browser)));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	class RandomTask extends AsyncTask<Void, Character, Void>
	{
		private String b;
		private String f;
		private String m;
		private boolean update_b;
		private boolean update_f;
		private boolean update_m;

		RandomTask() {
			update_b = true;
			update_f = true;
			update_m = true;
		}

		RandomTask(char first_letter) {
			update_b = false;
			update_f = false;
			update_m = false;

			switch (first_letter) {
			case 'b':
				update_b = true; break;
			case 'f':
				update_f = true; break;
			case 'm':
				update_m = true; break;
			default:
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			DbAdapter dbHelper = new DbAdapter();
			dbHelper.open(getApplicationContext());
			if (update_b) {
				b = dbHelper.random_word('b');
				publishProgress('b');
			}
			if (update_f) {
				f = dbHelper.random_word('f');
				publishProgress('f');
			}
			if (update_m) {
				m = dbHelper.random_word('m');
				publishProgress('m');
				dbHelper.close();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Character... values) {
			TextView view;
			String word;

			char first_letter = values[0];
			switch (first_letter) {
			case 'b':
				view = ((TextView) BuffaloFoxMonkeyActivity.this.findViewById(R.id.b));
				word = b;
				break;
			case 'f':
				view = ((TextView) BuffaloFoxMonkeyActivity.this.findViewById(R.id.f));
				word = f;
				break;
			case 'm':
				view = ((TextView) BuffaloFoxMonkeyActivity.this.findViewById(R.id.m));
				word = m;
				break;
			default:
				return;
			}

			view.setText(word);
			view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
			view.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}

}

