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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
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
import com.fullsail.lib.Venue;

public class MainActivity extends Activity {
	
	// Variables
	LinearLayout linearLayout;
	LinearLayout subLinearLayout;
	LinearLayout.LayoutParams layoutParams;
	TextView textView;
	Context context = this;
	Boolean connected = false;
	HashMap<String, String> _history;
	HistorySpinner _historySpinner;
	
	// Weather textviews
	EditText _name;
	EditText _country;
	EditText _temp;
	EditText _windSpeed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		_history = getHistory();
		Log.i("Current history: ",_history.toString());
		
		// Test Network Connetion
		connected = Connectivity.getConnectionStatus(context);
		
		// Setup the linear layout
		linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		linearLayout.setLayoutParams(layoutParams);
		
		// Add the Get button
		Button button = new Button(this);
		button.setText("Search");
		linearLayout.addView(button);
		
		// Add history display
		_historySpinner = new HistorySpinner(context);
		linearLayout.addView(_historySpinner);
		
		// Add the text view
		textView = new TextView(this);
		textView.setText("Enter a city name and press the Search button.");
		linearLayout.addView(textView);
		
		// Weather text views
		TextView name = new TextView(this);
		name.setText("Name:");

		TextView country = new TextView(this);
		country.setText("Country:");

		TextView temp = new TextView(this);
		temp.setText("Temp:");

		TextView windSpeed = new TextView(this);
		windSpeed.setText("Wind Speed:");

		_name = new EditText(this);
		_country = new EditText(this);
		_country.setEnabled(false);
		_temp = new EditText(this);
		_windSpeed = new EditText(this);

		linearLayout.addView(name);
		linearLayout.addView(_name);
		linearLayout.addView(country);
		linearLayout.addView(_country);
		linearLayout.addView(temp);
		linearLayout.addView(_temp);
		linearLayout.addView(windSpeed);
		linearLayout.addView(_windSpeed);
		
		// Add the POST button
		Button submit = new Button(this);
		submit.setText("Submit");
		linearLayout.addView(submit);
		
		// Setup the nested ll
		subLinearLayout = new LinearLayout(this);
		subLinearLayout.setOrientation(LinearLayout.VERTICAL);
		subLinearLayout.setLayoutParams(layoutParams);
		linearLayout.addView(subLinearLayout);
		
		// Radio group
//		venueGroupOptions = new RadioGroup(this);
//		subLinearLayout.addView(venueGroupOptions);
		
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
						String searchURL = "http://openweathermap.org/data/2.5/weather?q="+encodedSearch+"&APPID=63a7a37aaacf05a109e77797f3af426d";
						//instantiate and execute AsyncTask
						new Request().execute(searchURL);

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
						// TODO Auto-generated catch block
						Log.e("Could not get the local JSON", e.toString());
						e.printStackTrace();
					}
				}
				
				
				/*
				try {
					// Create venues from the resouces					
					JSONObject jsonObject = new JSONObject(getString(R.string.Venue1));
					venues[VenueEnum.VINDEX0.ordinal()] = new Venue(jsonObject);
					
					jsonObject = new JSONObject(getString(R.string.Venue2));
					venues[VenueEnum.VINDEX1.ordinal()] = new Venue(jsonObject);
					
					jsonObject = new JSONObject(getString(R.string.Venue3));
					venues[VenueEnum.VINDEX2.ordinal()] = new Venue(jsonObject);
					
					textView.setText("");
					for (Venue venue : venues) {
						textView.append("=== Venue ===\n");
						textView.append("Venue id: " + venue.getId() + "\nVenue name: " + venue.getName() + "\n");
						
						if (!areRadiosPoulated) {
							// Populate the radio buttons
							RadioButton rb = new RadioButton(context);
							rb.setText(venue.getName());
							venueGroupOptions.addView(rb);
						}
					}	
					areRadiosPoulated = true;
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					Log.i("Error: ", e1.getMessage());
					e1.printStackTrace();
				}
				*/
			}
		});
		
		setContentView(linearLayout);
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
		protected void onPostExecute(String result) {
			//start preparing result string for display
			try {
				//get JSONObject from result
				JSONObject json = new JSONObject(result);
				
				JSONObject coord = json.getJSONObject("coord");
				JSONObject sys = json.getJSONObject("sys");
				JSONArray weather = json.getJSONArray("weather");
				JSONObject main = json.getJSONObject("main");
				JSONObject wind = json.getJSONObject("wind");
				JSONObject clouds = json.getJSONObject("clouds");
				
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
			}
			catch (Exception e) {
				Log.e("Exception occured while building the result object", e.toString());
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
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
