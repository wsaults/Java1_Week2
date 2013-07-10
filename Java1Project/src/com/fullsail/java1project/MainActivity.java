/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.java1project
 * 
 * @author 	William Saults
 * 
 * date 	Jul 9, 2013
 */
package com.fullsail.java1project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullsail.lib.Connectivity;
import com.fullsail.lib.FetchJsonData;
import com.fullsail.lib.FileManager;
import com.fullsail.lib.HistorySpinner;

import com.parse.Parse;
import com.parse.ParseObject;


// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
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
				if	(connected) {
					// Build the url
					try{
						String input = (_name.getText().toString().equals("")) ? "Dallas" : _name.getText().toString();
						_name.setText(input);
						
						//encode in case user has included symbols such as spaces etc
						String encodedSearch = URLEncoder.encode(input, "UTF-8");
						//append encoded user search term to search URL
						// http://api.openweathermap.org/data/2.5/forecast/daily?q=London&units=metric&cnt=7
						String searchURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+encodedSearch+"&APPID=63a7a37aaacf05a109e77797f3af426d&units=metric&cnt=7";
						//instantiate and execute AsyncTask
						new Request().execute(searchURL);
						
//						FetchForecast.fetchForecast(input);
					}
					catch(Exception e){ 
						Log.e("Encoding the search url failed", e.toString());
						e.printStackTrace(); 
					}
				} else {
					Log.i("Network Connection", Connectivity.getConnectionType(context));
					// Fetch the json data from the data file
					try {
						String json = FetchJsonData.jsonToStringFromAssetFolder("data.json", getBaseContext());
						Log.i("JSON string: ", json);
					} catch (IOException e) {
						Log.e("Could not get the local JSON", e.toString());
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	/**
	 * The Class Request.
	 */
	private class Request extends AsyncTask<String, Void, String> {
		/*
		 * Carry out fetching task in background
		 * - receives search URL via execute method
		 */
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(String... stringURL) {
			//start building result which will be json string
			StringBuilder stringBuilder = new StringBuilder();
			//should only be one URL, receives array
			for (String searchURL : stringURL) {
				HttpClient client = new DefaultHttpClient();
				try {
					//pass search URL string to fetch
					HttpGet get = new HttpGet(searchURL);
					//execute request
					HttpResponse response = client.execute(get);
					//check status, only proceed if ok
					StatusLine searchStatus = response.getStatusLine();
					if (searchStatus.getStatusCode() == 200) {
						//get the response
						HttpEntity entity = response.getEntity();
						InputStream content = entity.getContent();
						//process the results
						InputStreamReader input = new InputStreamReader(content);
						BufferedReader reader = new BufferedReader(input);
						String lineIn;
						while ((lineIn = reader.readLine()) != null) {
							stringBuilder.append(lineIn);
						}
					} else {
						Log.e("Status code not 200", "getStatusCode error");
					}
				} catch(Exception e){ 
					Log.e("The HTTP request did not work", e.toString());
					e.printStackTrace(); 
				}
			}
			//return result string
			return stringBuilder.toString();
		}
		/*
		 * Process result of search query
		 */
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		protected void onPostExecute(String result) {
			//start preparing result string for display
			try {
				//get JSONObject from result
				JSONObject json = new JSONObject(result);
				
				JSONArray list = json.getJSONArray("list");

				for (int i = 0; i < list.length(); i++) {
					Log.i("list obj", list.getJSONObject(i).toString());
				}
				
				/*
				
				JSONObject coord = json.getJSONObject("coord");
				JSONObject sys = json.getJSONObject("sys");
				JSONArray weather = json.getJSONArray("weather");
				JSONObject main = json.getJSONObject("main");
				JSONObject wind = json.getJSONObject("wind");
//				JSONObject clouds = json.getJSONObject("clouds");
				
				Log.i("coord", coord.toString());
				Log.i("weather", weather.toString());
				Log.i("main", main.toString());
				Log.i("id", json.getString("id"));
				
				_country.setText(sys.getString("country"));
				
				// Kelven to Fahrenheit conversion (¼K - 273.15)* 1.8000 + 32.00
				Double temp = Double.parseDouble(main.getString("temp"));
				temp = (temp - 273.15) * 1.8000 + 32.00;
				BigDecimal bd = new BigDecimal(temp).setScale(2, RoundingMode.HALF_UP);
				
				_temp.setText(bd.toString());
				_windSpeed.setText(wind.getString("speed"));
				
				_history.put(json.getString("id"), result);
				FileManager.storeObjectFile(context, "history", _history, false);
				*/
			}
			catch (Exception e) {
				Log.e("Exception occured while building the result object", e.toString());
				e.printStackTrace();
			}
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
