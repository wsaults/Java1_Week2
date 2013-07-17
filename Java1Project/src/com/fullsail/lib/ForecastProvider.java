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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import com.fullsail.lib.FileManager;
import com.fullsail.lib.DataService;

// TODO: Auto-generated Javadoc
/**
 * The Class ForecastProvider.
 */
public class ForecastProvider extends ContentProvider {
	
	public static final String AUTHORITY = "com.fullsail.lib.forecastprovider";
	
	public static class ForecastData implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/items");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.fullsail.lib.item";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.fullsail.lib.item";
		
		// Columns
		public static final String DATE_COLUMN = "dt";
		
		// Projection
		public static final String[] PROJETION = {"_id", DATE_COLUMN};
		
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
		
		MatrixCursor result = new MatrixCursor(ForecastData.PROJETION);
		
		// Make sure there is something to return.
		String JSONString = FileManager.readStringFile(getContext(), DataService.FORECAST_TEXT_FILENAME, false);
		
		switch (uriMatcher.match(uri)) {
		case ITEMS:
			return ForecastData.CONTENT_TYPE;

		case ITEMS_ID:
			return ForecastData.CONTENT_ITEM_TYPE;
		}
		return null;
	}
	
	
	
	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
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
