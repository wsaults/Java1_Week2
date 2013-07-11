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

import java.io.IOException;
import java.net.URLEncoder;

import com.fullsail.java1project.MainActivity.Request;

import android.app.Activity;
import android.app.IntentService;
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
	
	public static final String MESSENGER_KEY = "messenger";
	
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
		Messenger messenger = (Messenger) extras.get(MESSENGER_KEY);
		
		if	(Connectivity.getConnectionStatus(this)) {
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
				
//				FetchForecast.fetchForecast(input);
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
		
		Message message = Message.obtain();
		message.arg1 = Activity.RESULT_OK;
		message.obj = "Service is Done";
		
		try {
			messenger.send(message);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
