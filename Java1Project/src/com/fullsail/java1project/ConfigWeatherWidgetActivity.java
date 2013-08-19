package com.fullsail.java1project;

import android.os.Bundle;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ConfigWeatherWidgetActivity extends Activity implements OnClickListener {

	Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_config_weather_widget);

		spinner = (Spinner) findViewById(R.id.citySpinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.cities_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		Button button = (Button) this.findViewById(R.id.submitButton);
		button.setOnClickListener((android.view.View.OnClickListener) this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config_weather_widget, menu);
		return true;
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

			if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				SharedPreferences preferences;
				SharedPreferences.Editor editor;
				preferences = this.getSharedPreferences("MyPreferences", MODE_PRIVATE);
				editor = preferences.edit();
				editor.putString("defaultCity", spinner.getSelectedItem().toString());
				editor.commit();
			}

		}
	}

}
