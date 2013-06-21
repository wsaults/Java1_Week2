/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.lib
 * 
 * @author 	William Saults
 * 
 * date 	Jun 20, 2013
 */
package com.fullsail.lib;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

/*
 * This class populates the history spinner.
 */

public class HistorySpinner extends LinearLayout {
	
	Spinner _spinner;
	Context _context;
	ArrayList<String> _cities = new ArrayList<String>();
	
	public HistorySpinner(Context context, HashMap<String, String> history) {
		super(context);
		
		_cities.add("City History");
		
		Log.i("Current history: ",history.toString());
		for (Object obj : history.values().toArray()) {
			try {
				JSONObject json = new JSONObject(obj.toString());
//				Log.i("name", json.getString("name"));
				if (json.getString("name").length() > 0) {
					_cities.add(json.getString("name"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		_context = context;
		LayoutParams lp;
		lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
		
		_spinner = new Spinner(_context);
		_spinner.setLayoutParams(lp);
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_item, _cities);
		listAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		_spinner.setAdapter(listAdapter);
		
		_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
				Log.i("City selected", parent.getItemAtPosition(pos).toString());
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.i("City selected", "None");
			}
		});
		
		updateHistory();
		
		this.addView(_spinner);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(lp);
	}
	
	private void updateHistory() {
//		_cities.add("Test");
	}
}
