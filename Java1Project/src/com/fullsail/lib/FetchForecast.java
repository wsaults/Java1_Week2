package com.fullsail.lib;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class FetchForecast {
	private static FetchForecast instance = null;
	protected FetchForecast() {
		// Exists to defeat instantiation.
	}
	public static FetchForecast getInstance() {
		if(instance == null) {
			instance = new FetchForecast();
		}
		return instance;
	}
	
	public static void fetchForecast (String city) {
		try {
			//encode in case user has included symbols such as spaces etc
			String encodedSearch = URLEncoder.encode(city, "UTF-8");
			//append encoded user search term to search URL
			// http://api.openweathermap.org/data/2.5/forecast/daily?q=London&units=metric&cnt=7
			String searchURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+encodedSearch+"&APPID=63a7a37aaacf05a109e77797f3af426d&units=metric&cnt=7";
			//instantiate and execute AsyncTask
			
			instance.new Request().execute(searchURL);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				JSONObject description = json.getJSONObject("description");
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
}
