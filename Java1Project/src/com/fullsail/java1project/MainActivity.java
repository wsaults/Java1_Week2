/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.java1project
 * 
 * @author 	William Saults
 * 
 * date 	Jul 15, 2013
 */
package com.fullsail.java1project;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fullsail.lib.Connectivity;
import com.fullsail.lib.DataService;
import com.fullsail.lib.FileManager;
import com.fullsail.lib.ForecastProvider;

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
	Context context = this;
	Boolean connected = false;
	HashMap<String, String> _history;
	GridLayout forecastGridLayout;
	
	// Weather textviews
	EditText _cityName;
	JSONObject _weatherJson;
	String _forecastString;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Always call the superclass first
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
		
		_cityName = (EditText)findViewById(R.id.cityNameEditText);
		
		/*
		// Add the POST button
		Button submit = (Button)findViewById(R.id.submitButton);
		
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
		*/
		
		// Button event handler
		button.setOnClickListener(new View.OnClickListener() { 

			@Override
			public void onClick(View v) {
				Handler dataServieHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// Use the respose to populate the weather data table.
						String response = null;
						if (msg.arg1 == RESULT_OK && msg.obj != null) {
							try {
								response = (String) msg.obj;
							} catch (Exception e) {
								Log.e("", e.getMessage().toString());
							}
							
							// Parse the weather json object.
							Log.i("response", response);
							
							
							displayWeatherProvider();
//							parseWeatherJsonObject(); // the parsing will be handled by the content provider.
						}
					}
				};
				
				Messenger dataMessenger = new Messenger(dataServieHandler);
				Intent startDataServiceIntent = new Intent(context, DataService.class);
				startDataServiceIntent.putExtra(DataService.MESSENGER_KEY, dataMessenger);
				startDataServiceIntent.putExtra(DataService.CITY_KEY, _cityName.getText().toString());
				startService(startDataServiceIntent);
				
				Log.i("Waiting on servie to end: ", "Waiting...");
			}
		});
	}
	
	@SuppressLint("SimpleDateFormat")
	private void displayWeatherProvider() {
		TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

		TableLayout tableLayout = new TableLayout(context);
		tableLayout.setLayoutParams(layoutParams);
		tableLayout.setShrinkAllColumns(true);

		LinearLayout tableLL = (LinearLayout) findViewById(R.id.tableLayout);
		tableLL.addView(tableLayout);
		
		ForecastProvider provider = new ForecastProvider();
		try {
			Cursor cursor = provider.query(ForecastProvider.CONTENT_URI, ForecastProvider.PROJETION, null, null, "ASC");
			
			if (cursor != null) {
				Log.i("Cursor count", String.valueOf(cursor.getCount()));
				
				cursor.moveToFirst();
				do {
					//Display the forecast date.
					String dateString = cursor.getString(cursor.getColumnIndex(ForecastProvider.DATE_COLUMN));
					String maxString = cursor.getString(cursor.getColumnIndex(ForecastProvider.MAX_COLUMN));
					String minString = cursor.getString(cursor.getColumnIndex(ForecastProvider.MIN_COLUMN));
					
					Date date = new Date(Long.parseLong(dateString) * 1000);
					SimpleDateFormat df = new SimpleDateFormat("MM-dd");
					String dateText = df.format(date);
					
					TableRow tableRow = new TableRow(context);
					tableRow.setLayoutParams(tableParams);
					String dateHighLow = " Date: " + dateText + " " + "| High: " + maxString + " | Low: " + minString + "\n";
					TextView text1 = new TextView(context);
					text1.setText(dateHighLow);
					tableRow.addView(text1);
					tableLayout.addView(tableRow);
				} while (cursor.moveToNext());
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@SuppressWarnings("unused")
	private void parseWeatherJsonObject() {
		TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

		TableLayout tableLayout = new TableLayout(context);
		tableLayout.setLayoutParams(layoutParams);
		tableLayout.setShrinkAllColumns(true);

		LinearLayout tableLL = (LinearLayout) findViewById(R.id.tableLayout);
		tableLL.addView(tableLayout);
		
		JSONObject obj;
		try {
			_forecastString = getLocalForecast().toString();
			obj = new JSONObject(_forecastString);
			if (obj != null) {
				JSONArray list = obj.getJSONArray("list");
				for (int i = 0; i < list.length(); i++) {
//					View.inflate(context,R.layout.forecast_grid_layout, linearLayout);
					Log.i("list obj", list.getJSONObject(i).toString());
					
					JSONObject json = list.getJSONObject(i);
					JSONObject temp = json.getJSONObject("temp");
					
					/*
					// Kelven to Fahrenheit conversion (¼K - 273.15)* 1.8000 + 32.00
					Double max = Double.parseDouble(temp.getString("max"));
					max = (max - 273.15) * 1.8000 + 32.00;
					BigDecimal maxBd = new BigDecimal(max).setScale(2, RoundingMode.HALF_UP);
					
					Double min = Double.parseDouble(temp.getString("min"));
					min = (min - 273.15) * 1.8000 + 32.00;
					BigDecimal minBd = new BigDecimal(min).setScale(2, RoundingMode.HALF_UP);
					*/
					
					JSONArray weather = json.getJSONArray("weather");
					JSONObject weatherObj = weather.getJSONObject(0);
					
					TableRow tableRow = new TableRow(context);
					tableRow.setLayoutParams(tableParams);
					String dateHighLow = " Date: " + json.getString("dt") + " High: " + temp.getString("max") + "\n Low: " + temp.getString("min") + "\n";
					TextView text1 = new TextView(context);
					text1.setText(dateHighLow);
					tableRow.addView(text1);
					tableLayout.addView(tableRow);
					
					TableRow tableRow2 = new TableRow(context);
					tableRow2.setLayoutParams(tableParams);
					String desc = " | Weather: " + weatherObj.getString("description");
					TextView text2 = new TextView(context);
					text2.setText(desc);
					tableRow.addView(text2);
					
					tableLayout.addView(tableRow2);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		Object stored = FileManager.readObjectFile(context, "history", false);
		HashMap<String, String> history;
		if (stored == null) {
			Log.i("History","History is empty");
			history = new HashMap<String, String>();
		} else {
			history = (HashMap<String, String>) stored;
		}
		return history;
	}
	
	/**
	 * Gets the local forecast.
	 *
	 * @return the local forecast
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, String> getLocalForecast() {
		Object stored = FileManager.readObjectFile(context, "forecast", false);
		HashMap<String, String> forecast;
		if (stored == null) {
			Log.i("forecast","forecast is empty");
			forecast = new HashMap<String, String>();
		} else {
			forecast = (HashMap<String, String>) stored;
		}
		return forecast;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {    
		// Save the form information.
		 savedInstanceState.putString("cityName", _cityName.getText().toString());
		 
		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	};
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    // Restore state members from saved instance
	    _cityName.setText("");
	    if (_cityName.getText().toString().equals("")) {
	    	_cityName.setText(savedInstanceState.getString("cityName"));
	    }
	    
	    displayWeatherProvider();
//	    parseWeatherJsonObject();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		FileManager.deleteObjectFile(context, "forecast", false);
	}
}
