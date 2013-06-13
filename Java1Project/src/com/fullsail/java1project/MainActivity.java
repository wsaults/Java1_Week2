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
import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fullsail.lib.FetchJsonData;
import com.fullsail.lib.Venue;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Setup the linear layout
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		linearLayout.setLayoutParams(layoutParams);
		
		// Add the button
		Button button = new Button(this);
		button.setText("Button");
		linearLayout.addView(button);
		
		// Button event handler
		button.setOnClickListener(new View.OnClickListener() { 

			@Override
			public void onClick(View v) {
				try {
					// Create venues from the resouces
					Log.i("Venue1 JSON string: ", getString(R.string.Venue1));
					Log.i("Venue2 JSON string: ", getString(R.string.Venue2));
					Log.i("Venue3 JSON string: ", getString(R.string.Venue2));
					
					JSONObject jsonObject = new JSONObject(getString(R.string.Venue1));
					Log.i("jsonObject: ", jsonObject.toString());
					Venue venue = new Venue(jsonObject);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					Log.i("Error: ", e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		
		// Fetch the json data from the data file
		try {
			String json = FetchJsonData.jsonToStringFromAssetFolder("data.json", getBaseContext());
			Log.i("JSON string: ", json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setContentView(linearLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
