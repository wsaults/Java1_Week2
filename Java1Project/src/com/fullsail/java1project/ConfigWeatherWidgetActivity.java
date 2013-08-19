package com.fullsail.java1project;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.widget.Button;

public class ConfigWeatherWidgetActivity extends Activity implements   {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_weather_widget);
		
//		Button button = (button) this.findViewById(R.id.submitButton);
//		button.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config_weather_widget, menu);
		return true;
	}

}
