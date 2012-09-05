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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class BuffaloFoxMonkeyActivity extends Activity
{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		random_bfm();
	}

	public void random_bfm() {
		TextView b_view = ((TextView) findViewById(R.id.b));
		TextView f_view = ((TextView) findViewById(R.id.f));
		TextView m_view = ((TextView) findViewById(R.id.m));

		b_view.setVisibility(View.INVISIBLE);
		f_view.setVisibility(View.INVISIBLE);
		m_view.setVisibility(View.INVISIBLE);

		String b = random_word("b");
		String f = random_word("f");
		String m = random_word("m");

		b_view.setText(b);
		f_view.setText(f);
		m_view.setText(m);

		b_view.setVisibility(View.VISIBLE);
		b_view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));

		f_view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
		f_view.setVisibility(View.VISIBLE);

		m_view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
		m_view.setVisibility(View.VISIBLE);
	}

	public String random_word(String table) {
		if (table.equals("b")) {
			return "buffalo";
		} else if (table.equals("f")) {
			return "fox";
		} else if (table.equals("m")) {
			return "monkey";
		} else {
			return table;
		}
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
}

