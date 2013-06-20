package com.fullsail.lib;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;


public class HistorySpinner extends LinearLayout {
	
	Spinner _spinner;
	Context _context;
	ArrayList<String> _cities = new ArrayList<String>();
	
	public HistorySpinner(Context context) {
		super(context);
		
		_context = context;
		LayoutParams lp;
		lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
		
		_cities.add("City History");
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
		_cities.add("Test");
	}
}
