package com.fullsail.java1project;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class PreferencesFragment extends Fragment {
	private PreferencesListener listener;

	public interface PreferencesListener {
		public void saveCity();
		public void openWebPage();
		public void setFahrenheit();
		public void setCelcius();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.secondlayout, container, false);

		// Save the default city based on the value entered into the city editText.
		Button saveCityButton = (Button) view.findViewById(R.id.saveDefaultCity);
		saveCityButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				listener.saveCity();
			}
		});

		// Launch web page
		Button webButton = (Button) view.findViewById(R.id.webpageButton);
		webButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				listener.openWebPage();
			}
		});

		// Set to fahrenheit
		Button fahrenheitButton = (Button) view.findViewById(R.id.fahrenheitButton);
		fahrenheitButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				listener.setFahrenheit();
			}
		});

		// Set to celcius
		Button celciusButton = (Button) view.findViewById(R.id.celciusButton);
		celciusButton.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {		
				listener.setCelcius();
			}
		});

		return view;
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (PreferencesListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " implement PreferencesListener");
		}
	}
}
