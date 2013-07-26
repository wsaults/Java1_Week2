/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.java1project
 * 
 * @author 	William Saults
 * 
 * date 	Jul 24, 2013
 */
package com.fullsail.java1project;

import com.fullsail.lib.Connectivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Log;

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

	SharedPreferences _preferences;
	SharedPreferences.Editor _editor;
	EditText cityName;
	Button fahrenheitButton;
	Button celciusButton;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Always call the superclass first
		super.onCreate(savedInstanceState);
		_preferences = getApplicationContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
		_editor = _preferences.edit();

		setContentView(R.layout.secondlayout);

		// Get the history. 
		// In the future add a clear history button.
		Bundle data = getIntent().getExtras();
		if (data != null){
			String history = data.getString("history");
			Log.i("The history arrived: " + history);
		}

		// Grab the default city or static default if it does not exist.
		cityName = (EditText)findViewById(R.id.defaultCityEditText);
		String cityNameString = _preferences.getString("defaultCity", "dallas");
		cityName.setText(cityNameString);

		// Save the default city based on the value entered into the city editText.
		Button saveCityButton = (Button)findViewById(R.id.saveDefaultCity);
		saveCityButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				// Save preference
				_editor.putString("defaultCity", cityName.getText().toString());
				_editor.commit();
			}
		});

		// Set to fahrenheit
		fahrenheitButton = (Button)findViewById(R.id.fahrenheitButton);
		fahrenheitButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				// Save preference
				_editor.putBoolean("isCelcius", false);
				_editor.commit();
				updateTempButtonStates();
			}
		});

		// Set to celcius
		celciusButton = (Button)findViewById(R.id.celciusButton);
		celciusButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {				
				// Save preference
				_editor.putBoolean("isCelcius", true);
				_editor.commit();
				updateTempButtonStates();
			}
		});

		// Set which temp button is selected.
		updateTempButtonStates();

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
	}

	/**
	 * Update temp button states.
	 */
	public void updateTempButtonStates() {
		// Set which temp button is selected.
		Boolean isCelcius = _preferences.getBoolean("isCelcius", false);
		if (isCelcius) {
			fahrenheitButton.setSelected(false);
			celciusButton.setSelected(true);
		} else {
			fahrenheitButton.setSelected(true);
			celciusButton.setSelected(false);
		}
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

		// Restore the text in the edit text.
		String cityNameString = _preferences.getString("defaultCity", "dallas");
		cityName.setText(cityNameString);
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

	@Override
	public void onBackPressed() {
		// Send the choosen temp format back.
		Intent a = new Intent(getApplicationContext(),MainActivity.class);
		Boolean isCelcius = _preferences.getBoolean("isCelcius", false);
		a.putExtra("isCelcius", isCelcius);
		setResult(RESULT_OK, a);
		super.finish();
	}
}
