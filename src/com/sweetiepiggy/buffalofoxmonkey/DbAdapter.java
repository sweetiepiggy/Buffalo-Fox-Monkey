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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


public class DbAdapter
{
	public static final String KEY_WORD = "word";

	private DatabaseHelper mDbHelper;

	private static final String DATABASE_PATH = "/data/data/com.sweetiepiggy.buffalofoxmonkey/databases/";
	private static final String DATABASE_NAME = "wordlist.db";
	private static final String DATABASE_TABLE_B = "b";
	private static final String DATABASE_TABLE_F = "f";
	private static final String DATABASE_TABLE_M = "m";
	private static final int DATABASE_VERSION = 1;

	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		private final Context mCtx;
		public SQLiteDatabase mDb;

		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mCtx = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
		}

		public void create_database() throws IOException
		{
			if (!database_exists()) {
				this.getReadableDatabase();
				try {
					copy_database();
				} catch (IOException e) {
					throw new Error(e);
				}
			}
		}

		private boolean database_exists()
		{
			SQLiteDatabase db = null;
			try {
				String full_path = DATABASE_PATH + DATABASE_NAME;
				db = SQLiteDatabase.openDatabase(full_path,
						null, SQLiteDatabase.OPEN_READONLY);
			} catch (SQLiteException e) {
				/* database does not exist yet */
			}
			if (db != null) {
				db.close();
			}
			return db != null;
		}

		private void copy_database() throws IOException
		{
			InputStream input = mCtx.getAssets().open(DATABASE_NAME);

			String full_path = DATABASE_PATH + DATABASE_NAME;

			OutputStream output = new FileOutputStream(full_path);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0){
				output.write(buffer, 0, length);
			}

			output.flush();
			output.close();
			input.close();
		}

		public void open_database(int perm) throws SQLException
		{
			String full_path = DATABASE_PATH + DATABASE_NAME;
			mDb = SQLiteDatabase.openDatabase(full_path, null, perm);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
		}

		@Override
		public synchronized void close()
		{
			if (mDb != null) {
				mDb.close();
			}
			super.close();
		}
	}

	public DbAdapter()
	{
	}

	public DbAdapter open(Context ctx) throws SQLException
	{
		mDbHelper = new DatabaseHelper(ctx);

		try {
			mDbHelper.create_database();
		} catch (IOException e) {
			throw new Error(e);
		}

		mDbHelper.open_database(SQLiteDatabase.OPEN_READONLY);

		return this;
	}

	public void close()
	{
		mDbHelper.close();
	}

	public String random_word(char first_letter)
	{
		switch (first_letter) {
		case 'b':
			return random_word(DATABASE_TABLE_B);
		case 'f':
			return random_word(DATABASE_TABLE_F);
		case 'm':
			return random_word(DATABASE_TABLE_M);
		default:
			return "";
		}
	}

	private String random_word(String table)
	{
		String ret = "";
		Cursor c = mDbHelper.mDb.query(table, new String[] {KEY_WORD},
				null, null, null, null, "random()", "1");
		if (c.moveToFirst()) {
			ret = c.getString(0);
		}
		return ret;
	}
}

