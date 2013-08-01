package com.fullsail.java1project;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class WeatherFragment extends Fragment {

	private WeatherListener listener;

	public interface WeatherListener {
		public void switchToPreferencesActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.mainlayout, container, false);

		// Switch views button
		Button switchViewsButton = (Button) view.findViewById(R.id.switchViewsButton);
		switchViewsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.switchToPreferencesActivity();
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
			listener = (WeatherListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " implement WeatherListener");
		}
	}
}
