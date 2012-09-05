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
	static final String BFM_TWITTER_ADDR = "@BFMradio";
	private AsyncTask mTask;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		random_bfm();
		init_try_again_button();
		init_tweet_button();
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
				String tweet_msg = BFM_TWITTER_ADDR + " " + b + " " + f + " " + m;
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	class RandomTask extends AsyncTask<Void, Character, Void>
	{
		private String b;
		private String f;
		private String m;

		@Override
		protected Void doInBackground(Void... params) {
			DbAdapter dbHelper = new DbAdapter();
			dbHelper.open(getApplicationContext());
			b = dbHelper.random_b();
			publishProgress('b');
			f = dbHelper.random_f();
			publishProgress('f');
			m = dbHelper.random_m();
			publishProgress('m');
			dbHelper.close();

			return null;
		}

		@Override
		protected void onProgressUpdate(Character... values) {
			TextView view;
			String word;
			if (values[0].equals('b')) {
				view = ((TextView) BuffaloFoxMonkeyActivity.this.findViewById(R.id.b));
				word = b;
			} else if (values[0].equals('f')) {
				view = ((TextView) BuffaloFoxMonkeyActivity.this.findViewById(R.id.f));
				word = f;
			} else if (values[0].equals('m')) {
				view = ((TextView) BuffaloFoxMonkeyActivity.this.findViewById(R.id.m));
				word = m;
			} else {
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

