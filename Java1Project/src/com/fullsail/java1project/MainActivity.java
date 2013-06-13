/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.java1project
 * 
 * @author 	William Saults
 * 
 * date 	Jun 12, 2013
 */
package com.fullsail.java1project;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import com.fullsail.lib.FetchJsonData;
import com.fullsail.lib.Venue;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create venues from the resouces
		Log.i("Venue1 JSON string: ", getString(R.string.Venue1));
		Log.i("Venue2 JSON string: ", getString(R.string.Venue2));
		Log.i("Venue3 JSON string: ", getString(R.string.Venue2));
		
		// Fetch the json data from the data file
		try {
			String json = FetchJsonData.jsonToStringFromAssetFolder("data", getBaseContext());
			Log.i("JSON string: ", json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
