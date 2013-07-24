/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.java1project
 * 
 * @author 	William Saults
 * 
 * date 	Jul 23, 2013
 */
package com.fullsail.java1project;

import com.fullsail.lib.Connectivity;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// TODO: Auto-generated Javadoc
/**
 * The Class SecondActivity.
 */
public class SecondActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Always call the superclass first
		super.onCreate(savedInstanceState);

		setContentView(R.layout.secondlayout);
		
		EditText cityName = (EditText)findViewById(R.id.defaultCityEditText);
		
		// cityName.getText().toString()

		// Launch web page
		Button webButton = (Button)findViewById(R.id.webpageButton);
		webButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				Boolean connected = Connectivity.getConnectionStatus(getApplicationContext());
				if (!connected) {
					// Alert the user that there is no internet connection			
					new AlertDialog.Builder(getApplicationContext())
					.setTitle("Warning")
					.setMessage("Cound not connect to the internet")
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {}
					})
					.show();
					return;
				}

				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.weather.com")));
			}
		});

		// Set to fahrenheit
		Button fahrenheitButton = (Button)findViewById(R.id.fahrenheitButton);
		fahrenheitButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				// Save preference
				SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);  
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean("isCelcius", false);
				editor.commit();
			}
		});

		// Set to celcius
		Button celciusButton = (Button)findViewById(R.id.celciusButton);
		celciusButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {				
				// Save preference
				SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);  
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean("isCelcius", true);
				editor.commit();


				//--READ data       
//				myvar = preferences.getInt("var1", 0);
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {    
		// Save stuff here.

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

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		// Google analytics
		EasyTracker.getInstance().activityStart(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		// Google analytics
		EasyTracker.getInstance().activityStop(this);
	}
}
