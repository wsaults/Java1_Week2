/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.java1project
 * 
 * @author 	William Saults
 * 
 * date 	Jun 12, 2013
 */
package com.fullsail.java1project;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fullsail.lib.FetchJsonData;
import com.fullsail.lib.Venue;
import com.fullsail.lib.VenueEnum;

public class MainActivity extends Activity {
	
	// Variables
	Venue venues[] = new Venue[3];
	LinearLayout linearLayout;
	LinearLayout subLinearLayout;
	LinearLayout.LayoutParams layoutParams;
	TextView textView;
	RadioGroup venueGroupOptions;
	Context context = this;
	Boolean areRadiosPoulated;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		areRadiosPoulated = false;
		
		// Setup the linear layout
		linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		linearLayout.setLayoutParams(layoutParams);
		
		// Add the button
		Button button = new Button(this);
		button.setText("Get JSON");
		linearLayout.addView(button);
		
		// Add the text view
		textView = new TextView(this);
		textView.setText("The JSON data will appear here.");
		linearLayout.addView(textView);
		
		// Setup the nested ll
		subLinearLayout = new LinearLayout(this);
		subLinearLayout.setOrientation(LinearLayout.VERTICAL);
		subLinearLayout.setLayoutParams(layoutParams);
		linearLayout.addView(subLinearLayout);
		
		// Add the text view
		TextView optionsText = new TextView(this);
		optionsText.setText("Choose a locaiton.");
		subLinearLayout.addView(optionsText);
		
		// Radio group
		venueGroupOptions = new RadioGroup(this);
		subLinearLayout.addView(venueGroupOptions);
		
		// Button event handler
		button.setOnClickListener(new View.OnClickListener() { 

			@Override
			public void onClick(View v) {
				try {
					// Create venues from the resouces					
					JSONObject jsonObject = new JSONObject(getString(R.string.Venue1));
					venues[VenueEnum.VINDEX0.ordinal()] = new Venue(jsonObject);
					
					jsonObject = new JSONObject(getString(R.string.Venue2));
					venues[VenueEnum.VINDEX1.ordinal()] = new Venue(jsonObject);
					
					jsonObject = new JSONObject(getString(R.string.Venue3));
					venues[VenueEnum.VINDEX2.ordinal()] = new Venue(jsonObject);
					
					textView.setText("");
					for (Venue venue : venues) {
						textView.append("=== Venue ===\n");
						textView.append("Venue id: " + venue.getId() + "\nVenue name: " + venue.getName() + "\n");
						
						if (!areRadiosPoulated) {
							// Populate the radio buttons
							RadioButton rb = new RadioButton(context);
							rb.setText(venue.getName());
							venueGroupOptions.addView(rb);
						}
					}	
					areRadiosPoulated = true;
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					Log.i("Error: ", e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		
		// Fetch the json data from the data file
		try {
			String json = FetchJsonData.jsonToStringFromAssetFolder("data.json", getBaseContext());
			Log.i("JSON string: ", json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setContentView(linearLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
