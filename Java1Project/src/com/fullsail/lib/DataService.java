/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.lib
 * 
 * @author 	William Saults
 * 
 * date 	Jul 10, 2013
 */
package com.fullsail.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fullsail.java1project.MainActivity;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class DataService.
 */
public class DataService extends IntentService {
	
	public static final String FORECAST_TEXT_FILENAME = "forecastText.txt";
	public static final String FORECAST_OBJECT_FILENAME = "forecast";
	
	public static final String MESSENGER_KEY = "messenger";
	public static final String CITY_KEY = "city";
	
	public static String JSON_LIST = "list";
	public static String JSON_TEMP = "temp";
	public static String JSON_WEATHER = "weather";
	public static String JSON_DATE = "dt";
	public static String JSON_MAX = "max";
	public static String JSON_MIN = "min";
	public static String JSON_DESCRIPTION = "description";
	
	public static Context _context;
	Messenger messenger;
	Message message;
	HashMap <String, String> jsonHashMap;
	
	/**
	 * Instantiates a new data service.
	 */

	public DataService() {
		super("DataService");
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.i("onHandleIntent", "started");
		
		Bundle extras = intent.getExtras();
		messenger = (Messenger) extras.get(MESSENGER_KEY);
		String city = extras.getString(CITY_KEY);
		message = Message.obtain();
		
		if	(MainActivity.connected) {
			// Build the url
			try{
				String input = (city.equals("")) ? "Dallas" : city;
//				city.setText(input);
				
				//encode in case user has included symbols such as spaces etc
				String encodedSearch = URLEncoder.encode(input, "UTF-8");
				//append encoded user search term to search URL
				// http://api.openweathermap.org/data/2.5/forecast/daily?q=London&units=metric&cnt=7
				String searchURL = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+encodedSearch+"&APPID=63a7a37aaacf05a109e77797f3af426d&units=metric&cnt=7";
				
				getResponse(searchURL);
				
				//instantiate and execute AsyncTask
//				new Request().execute(searchURL);
				
			}
			catch(Exception e){ 
				Log.e("Encoding the search url failed", e.toString());
				e.printStackTrace(); 
			}
		} else {
			
			
			// Fetch the json data from the data file
			try {
				String json = FetchJsonData.jsonToStringFromAssetFolder("data.json", getBaseContext());
				Log.i("JSON string: ", json);
			} catch (IOException e) {
				message.arg1 = Activity.RESULT_CANCELED;
				Log.e("Could not get the local JSON", e.toString());
				e.printStackTrace();
			}
		}
	}
	
	public void getResponse (String... stringURL) {
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
					message.arg1 = Activity.RESULT_CANCELED;
					Log.e("Status code not 200", "getStatusCode error");
				}
			} catch(Exception e){ 
				message.arg1 = Activity.RESULT_CANCELED;
				Log.e("The HTTP request did not work", e.toString());
				e.printStackTrace(); 
			}
		}
		
		storeString(stringBuilder.toString());
		//return result string
//		return stringBuilder.toString();
	}
	
	public void storeString (String result) {
		//start preparing result string for display
		try {
			// Store the JSON string in text file.
			_context = getBaseContext();
			FileManager.storeStringFile(_context,FORECAST_TEXT_FILENAME, result, false);
			
			message.arg1 = Activity.RESULT_OK;
			message.obj = "Service is Done";
			
			try {
				messenger.send(message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			message.arg1 = Activity.RESULT_CANCELED;
			message.obj = "Service is Done";
			Log.e("Exception occured while building the result object", e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * The Class Request
	private class Request extends AsyncTask<String, Void, String> {
		/*
		 * Carry out fetching task in background
		 * - receives search URL via execute method
		 *  (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
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
						message.arg1 = Activity.RESULT_CANCELED;
						Log.e("Status code not 200", "getStatusCode error");
					}
				} catch(Exception e){ 
					message.arg1 = Activity.RESULT_CANCELED;
					Log.e("The HTTP request did not work", e.toString());
					e.printStackTrace(); 
				}
			}
			//return result string
			return stringBuilder.toString();
		}
		
		/*
		 * Process result of search query
		 *  (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)

		protected void onPostExecute(String result) {
			//start preparing result string for display
			try {
				// Store the JSON string in text file.
				_context = getBaseContext();
				FileManager.storeStringFile(_context,FORECAST_TEXT_FILENAME, result, false);
				
				message.arg1 = Activity.RESULT_OK;
				message.obj = "Service is Done";
				
				try {
					messenger.send(message);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			catch (Exception e) {
				message.arg1 = Activity.RESULT_CANCELED;
				message.obj = "Service is Done";
				Log.e("Exception occured while building the result object", e.toString());
				e.printStackTrace();
			}
		}
	}
	*/
}
