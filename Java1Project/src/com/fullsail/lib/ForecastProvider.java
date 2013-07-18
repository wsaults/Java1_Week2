/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.lib
 * 
 * @author 	William Saults
 * 
 * date 	Jul 16, 2013
 */
package com.fullsail.lib;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.TableRow;
import android.widget.TextView;

import com.fullsail.lib.FileManager;
import com.fullsail.lib.DataService;

// TODO: Auto-generated Javadoc
/**
 * The Class ForecastProvider.
 */
public class ForecastProvider extends ContentProvider {
	
	public static final String AUTHORITY = "com.fullsail.lib.forecastprovider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/items");
	
	// Columns
			public static final String DATE_COLUMN = "date";
			public static final String WEATHER_COLUMN = "weather";
			public static final String MAX_COLUMN = "max";
			public static final String MIN_COLUMN = "min";
	
	// Projection
			public static final String[] PROJETION = {"_id", DATE_COLUMN, MAX_COLUMN, MIN_COLUMN};
	
	public static class ForecastData implements BaseColumns {
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.fullsail.lib.item";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.fullsail.lib.item";
		
		private ForecastData() {};
	}
	
	// return all the items from the JSON string
	public static final int ITEMS = 1;
	// return items by ID
	public static final int ITEMS_ID = 2;
	
	// URI matcher
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		uriMatcher.addURI(AUTHORITY, "items/", ITEMS);
		uriMatcher.addURI(AUTHORITY, "items/#", ITEMS_ID);
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		
		switch (uriMatcher.match(uri)) {
		case ITEMS:
			return ForecastData.CONTENT_TYPE;

		case ITEMS_ID:
			return ForecastData.CONTENT_ITEM_TYPE;
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		MatrixCursor result = new MatrixCursor(PROJETION);
		
		// Make sure there is something to return.
		String JSONString = null;
		try {
			JSONString = FileManager.readStringFile(DataService.FORECAST_TEXT_FILENAME);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject obj = null;
		JSONArray list = null;
		String date = null;
		String weather = null;
		String max = null;
		String min = null;
		try {
			obj = new JSONObject(JSONString);
			if (obj != null) {
				list = obj.getJSONArray("list");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// If the list is null then just return the empty result
		if (list == null) {
			return result;
		}
		
		switch (uriMatcher.match(uri)) {
		case ITEMS:
			for (int i = 0; i < list.length(); i++) {
				try {
//					Log.i("list obj", list.getJSONObject(i).toString());

					JSONObject json = list.getJSONObject(i);
					date = json.getString("dt");
					JSONObject temp = json.getJSONObject("temp");
					max = temp.getString("max");
					min = temp.getString("min");
					// see 8:20 in video 2
					result.addRow(new Object[] { i + 1, date, max, min});
					/*
					JSONObject temp = json.getJSONObject("temp");
					JSONArray weather = json.getJSONArray("weather");
					JSONObject weatherObj = weather.getJSONObject(0);
					String dateHighLow = " Date: " + json.getString("dt") + " High: " + temp.getString("max") + "\n Low: " + temp.getString("min") + "\n";
					String desc = " | Weather: " + weatherObj.getString("description");
					*/
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			
		case ITEMS_ID:
			// Leaving this here for future use.
			break;
			
		default:
			break;
		}
		
		return result;
	}
	
	
	
	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		Cursor cursor = query(ForecastProvider.CONTENT_URI, ForecastProvider.PROJETION, null, null, "ASC");
		return cursor.getCount();
	}
	
	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
