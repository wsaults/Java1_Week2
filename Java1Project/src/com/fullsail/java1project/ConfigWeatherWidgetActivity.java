package com.fullsail.java1project;

import android.os.Bundle;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;

public class ConfigWeatherWidgetActivity extends Activity {

	Spinner spinner;
	private ConfigWeatherWidgetActivity _context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_weather_widget);
		setResult(RESULT_CANCELED);
		
		_context = this;

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
        			int widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        			if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
        				SharedPreferences preferences;
        				SharedPreferences.Editor editor;
        				preferences = _context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
        				editor = preferences.edit();
        				editor.putString("defaultCity", spinner.getSelectedItem().toString());
        				editor.commit();
        			}
        			
        			RemoteViews rv = new RemoteViews(_context.getPackageName(), R.layout.activity_config_weather_widget);
        			
        			// save spinner choice			
        			SharedPreferences preferences = _context.getSharedPreferences("MyPreferences", MODE_PRIVATE);
        			preferences.edit().putString("defaultCity", spinner.getSelectedItem().toString());
        			
        			AppWidgetManager.getInstance(_context).updateAppWidget(widgetId, rv);
        			
        			Intent resultValue = new Intent();
        			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        			setResult(RESULT_OK, resultValue);
        			finish();
        		}
            }
        });

	}
}
