/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.java1project
 * 
 * @author 	William Saults
 * 
 * date 	Jul 10, 2013
 */
package com.fullsail.java1project;


import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullsail.lib.Connectivity;
import com.fullsail.lib.DataService;
import com.fullsail.lib.FileManager;

import com.parse.Parse;
import com.parse.ParseObject;


// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	
	// Variables
	LinearLayout linearLayout;
	LinearLayout.LayoutParams layoutParams;
	TextView textView;
	Context context = this;
	Boolean connected = false;
	HashMap<String, String> _history;
	
	// Weather textviews
	EditText _name;
	EditText _country;
	EditText _temp;
	EditText _windSpeed;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mainlayout);
		
		Parse.initialize(this, "6WphHpeWQJxN6LcsjSME5SuDJNByUgWcp4HutqIG", "QiICRS6hDy2RqJavJXbZm0n5yFlNYhDOBW8MPKRi"); 
		
		_history = getHistory();
		
		// Test Network Connetion
		connected = Connectivity.getConnectionStatus(context);
		
		// Setup a linear layout
		linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		linearLayout.setLayoutParams(layoutParams);
	
		this.addContentView(linearLayout, layoutParams);
		
		// Add the Get button
		Button button = (Button)findViewById(R.id.searchButton);
		
		// Add the POST button
		Button submit = (Button)findViewById(R.id.submitButton);
		
		_name = (EditText)findViewById(R.id.cityNameEditText);
		_country = (EditText)findViewById(R.id.countryEditText);
		_temp = (EditText)findViewById(R.id.tempEditText);
		_windSpeed = (EditText)findViewById(R.id.windEditText);
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if	(connected) {
					// Send the data to parse
					try {						
						if(_name.getText().toString().equals("")) return;
						
						ParseObject weatherObject = new ParseObject("WeatherObject");
						weatherObject.put("name", _name.getText().toString());
						weatherObject.put("country", _country.getText().toString());
						weatherObject.put("temp", _temp.getText().toString());
						weatherObject.put("windSpeed", _windSpeed.getText().toString());
						weatherObject.saveInBackground();
					} catch(Exception e){ 
						Log.e("Could not send data to prase. Will try again later.", e.toString());
						e.printStackTrace(); 
					}
				} else {
					Log.i("Network Connection", Connectivity.getConnectionType(context));	
				}
			}
		});
		
		// Button event handler
		button.setOnClickListener(new View.OnClickListener() { 

			@Override
			public void onClick(View v) {
				Handler dataServieHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						String response = null;
						if (msg.arg1 == RESULT_OK && msg.obj != null) {
							try {
								response = (String) msg.obj;
							} catch (Exception e) {
								Log.e("", e.getMessage().toString());
							}
							
							// TODO: do something with the response.
						}
					}
				};
				
				Messenger dataMessenger = new Messenger(dataServieHandler);
				Intent startDataServiceIntent = new Intent(context, DataService.class);
				startDataServiceIntent.putExtra(DataService.MESSENGER_KEY, dataMessenger);
				startDataServiceIntent.putExtra(DataService.CITY_KEY, _name.getText().toString());
				startService(startDataServiceIntent);
				
				Log.i("Waiting on servie to end: ", "Waiting...");
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Gets the history.
	 *
	 * @return the history
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, String> getHistory() {
		Object stored = FileManager.readObjectgFile(context, "history", false);
		HashMap<String, String> history;
		if (stored == null) {
			Log.i("History","History is empty");
			history = new HashMap<String, String>();
		} else {
			history = (HashMap<String, String>) stored;
		}
		return history;
	}
}
