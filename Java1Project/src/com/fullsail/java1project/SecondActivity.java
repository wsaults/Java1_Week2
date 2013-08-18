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

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

// TODO: Auto-generated Javadoc
/**
 * The Class SecondActivity.
 */
public class SecondActivity extends Activity implements PreferencesFragment.PreferencesListener {

	SharedPreferences _preferences;
	static SharedPreferences.Editor _editor;
	static EditText cityName;
	static Context context;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Always call the superclass first
		super.onCreate(savedInstanceState);
		_preferences = getApplicationContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
		_editor = _preferences.edit();
		_editor.commit();

		setContentView(R.layout.preferencesfrag);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

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

	public static void saveCityHandler(Context context) {
		// Save preference
		SharedPreferences preferences;
		SharedPreferences.Editor editor;
		preferences = context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
		editor = preferences.edit();
		editor.putString("defaultCity", cityName.getText().toString());
		editor.commit();
	}

	public static void openWebPageHandler(Context context) {
		Boolean connected = Connectivity.getConnectionStatus(context);
		if (!connected) {
			// Alert the user that there is no internet connection			
			new AlertDialog.Builder(context)
			.setTitle("Warning")
			.setMessage("Cound not connect to the internet")
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			})
			.show();
			return;
		}

		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.weather.com")));
	}

	public static void setFahrenheitHandler(Context context) {
		// Save preference
		SharedPreferences preferences;
		SharedPreferences.Editor editor;
		preferences = context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
		editor = preferences.edit();
		editor.putBoolean("isCelcius", false);
		editor.commit();
	}

	public static void setCelciusHandler(Context context) {
		// Save preference
		SharedPreferences preferences;
		SharedPreferences.Editor editor;
		preferences = context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
		editor = preferences.edit();
		editor.putBoolean("isCelcius", true);
		editor.commit();
	}

	// PreferenceFragment methods
	@Override
	public void saveCity() {
		saveCityHandler(getApplicationContext());
	}

	@Override
	public void openWebPage() {
		openWebPageHandler(getApplicationContext());
	}

	@Override
	public void setFahrenheit() {
		setFahrenheitHandler(getApplicationContext());
	}

	@Override
	public void setCelcius() {
		setCelciusHandler(getApplicationContext());
	}
}
