package com.fullsail.java1project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.fullsail.lib.Connectivity;
import com.fullsail.lib.FetchJsonData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;



public class ConfigWeatherWidgetActivity extends Activity {

	Spinner spinner;
	private ConfigWeatherWidgetActivity _context;
	public static Boolean connected = false;
	String spinnerChoice;
	RemoteViews rv;
	int widgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_weather_widget);
		setResult(RESULT_CANCELED);

		_context = this;
		
		// Test Network Connetion
		connected = Connectivity.getConnectionStatus(_context);

		spinner = (Spinner) findViewById(R.id.citySpinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.cities_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		Button button = (Button) this.findViewById(R.id.submitButton);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				Bundle extras = getIntent().getExtras();
				if (extras != null) {
					widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

					if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
						SharedPreferences preferences;
						SharedPreferences.Editor editor;
						preferences = _context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
						editor = preferences.edit();
						editor.putString("defaultCity", spinner.getSelectedItem().toString());
						editor.commit();
					}

					rv = new RemoteViews(_context.getPackageName(), R.layout.widget_layout);

					// save spinner choice			
					spinnerChoice = spinner.getSelectedItem().toString();
					SharedPreferences preferences = _context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
					preferences.edit().putString("defaultCity", spinnerChoice);
					
					
					if	(connected) {
						// Build the url
						try{
							String input = (spinnerChoice.equals("")) ? "Dallas" : spinnerChoice;

							//encode in case user has included symbols such as spaces etc
							String encodedSearch = URLEncoder.encode(input, "UTF-8");
							//append encoded user search term to search URL
							String searchURL = "http://openweathermap.org/data/2.5/weather?q="+encodedSearch+"&APPID=63a7a37aaacf05a109e77797f3af426d";
							//instantiate and execute AsyncTask
							new Request().execute(searchURL);

						}
						catch(Exception e){
							Log.e("Encoding the search url failed", e.toString());
							e.printStackTrace();
						}
					} else {
						Log.i("Network Connection", Connectivity.getConnectionType(_context));
						// Fetch the json data from the data file
						try {
							String json = FetchJsonData.jsonToStringFromAssetFolder("data.todaysWeather", getBaseContext());
							Log.i("JSON string: ", json);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							Log.e("Could not get the local JSON", e.toString());
							e.printStackTrace();
						}
					}
				}
			}
		});

	}
	
	private class Request extends AsyncTask<String, Void, String> {
		/*
		 * Carry out fetching task in background
		 * - receives search URL via execute method
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
					HttpResponse response = (HttpResponse) client.execute(get);
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
		protected void onPostExecute(String result) {
			//start preparing result string for display
			try {
				//get JSONObject from result
				JSONObject json = new JSONObject(result);

//				JSONObject coord = json.getJSONObject("coord");
//				JSONArray weather = json.getJSONArray("weather");
				JSONObject main = json.getJSONObject("main");

//				Log.i("coord", coord.toString());
//				Log.i("weather", weather.toString());
//				Log.i("main", main.toString());
//				Log.i("id", json.getString("id"));

				//Kelven to Fahrenheit conversion (K - 273.15)* 1.8000 + 32.00
				Double temp = Double.parseDouble(main.getString("temp"));
				temp = (temp - 273.15) * 1.8000 + 32.00;
				BigDecimal bd = new BigDecimal(temp).setScale(2, RoundingMode.HALF_UP);

				rv.setTextViewText(R.id.textView2, bd.toString());
			}
			catch (Exception e) {
				Log.e("Exception occured while building the result object", e.toString());
				e.printStackTrace();
			}
			
			AppWidgetManager.getInstance(_context).updateAppWidget(widgetId, rv);

			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			setResult(RESULT_OK, resultValue);
			finish();
		}
	}
}
