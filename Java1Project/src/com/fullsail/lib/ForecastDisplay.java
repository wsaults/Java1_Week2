/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.lib
 * 
 * @author 	William Saults
 * 
 * date 	Jul 11, 2013
 */
package com.fullsail.lib;

import android.content.Context;
import android.widget.GridLayout;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class ForecastDisplay.
 */
public class ForecastDisplay extends GridLayout {

	public static TextView _date0;
	TextView _tempMin0;
	TextView _tempMax0;
	TextView _description0;
	
	TextView _date1;
	TextView _tempMin1;
	TextView _tempMax1;
	TextView _description1;
	
	TextView _date2;
	TextView _tempMin2;
	TextView _tempMax2;
	TextView _description2;
	
	TextView _date3;
	TextView _tempMin3;
	TextView _tempMax3;
	TextView _description3;
	
	TextView _date4;
	TextView _tempMin4;
	TextView _tempMax4;
	TextView _description4;
	
	TextView _date5;
	TextView _tempMin5;
	TextView _tempMax5;
	TextView _description5;
	
	TextView _date6;
	TextView _tempMin6;
	TextView _tempMax6;
	TextView _description6;
	Context _context;

	/**
	 * Instantiates a new forecast display.
	 *
	 * @param context the context
	 */
	public ForecastDisplay(Context context) {
		super(context);
		_context = context;

		this.setColumnCount(2);

		// 0
		// date
		TextView dateLabel0 = new TextView(_context);
		dateLabel0.setText("Day:");
		_date0 = new TextView(_context);

		// temp min
		TextView tempMinLabel0 = new TextView(_context);
		tempMinLabel0.setText("Low:");
		_tempMin0 = new TextView(_context);

		// temp max
		TextView tempMaxLabel0 = new TextView(_context);
		tempMaxLabel0.setText("High:");
		_tempMax0 = new TextView(_context);

		// weather desc
		TextView descLabel0 = new TextView(_context);
		descLabel0.setText("Weather:");
		_description0 = new TextView(_context);

		// 1
		// date
		TextView dateLabel1 = new TextView(_context);
		dateLabel1.setText("Day:");
		_date1 = new TextView(_context);

		// temp min
		TextView tempMinLabel1 = new TextView(_context);
		tempMinLabel1.setText("Low:");
		_tempMin1 = new TextView(_context);

		// temp max
		TextView tempMaxLabel1 = new TextView(_context);
		tempMaxLabel1.setText("High:");
		_tempMax1 = new TextView(_context);

		// weather desc
		TextView descLabel1 = new TextView(_context);
		descLabel1.setText("Weather:");
		_description1 = new TextView(_context);
		
		// 2
		// date
		TextView dateLabel2 = new TextView(_context);
		dateLabel2.setText("Day:");
		_date2 = new TextView(_context);

		// temp min
		TextView tempMinLabel2 = new TextView(_context);
		tempMinLabel2.setText("Low:");
		_tempMin2 = new TextView(_context);

		// temp max
		TextView tempMaxLabel2 = new TextView(_context);
		tempMaxLabel2.setText("High:");
		_tempMax2 = new TextView(_context);

		// weather desc
		TextView descLabel2 = new TextView(_context);
		descLabel2.setText("Weather:");
		_description2 = new TextView(_context);

		// 3
		// date
		TextView dateLabel3 = new TextView(_context);
		dateLabel3.setText("Day:");
		_date3 = new TextView(_context);

		// temp min
		TextView tempMinLabel3 = new TextView(_context);
		tempMinLabel3.setText("Low:");
		_tempMin3 = new TextView(_context);

		// temp max
		TextView tempMaxLabel3 = new TextView(_context);
		tempMaxLabel3.setText("High:");
		_tempMax3 = new TextView(_context);

		// weather desc
		TextView descLabel3 = new TextView(_context);
		descLabel3.setText("Weather:");
		_description3 = new TextView(_context);

		// 4
		// date
		TextView dateLabel4 = new TextView(_context);
		dateLabel4.setText("Day:");
		_date4 = new TextView(_context);

		// temp min
		TextView tempMinLabel4 = new TextView(_context);
		tempMinLabel4.setText("Low:");
		_tempMin4 = new TextView(_context);

		// temp max
		TextView tempMaxLabel4 = new TextView(_context);
		tempMaxLabel4.setText("High:");
		_tempMax4 = new TextView(_context);

		// weather desc
		TextView descLabel4 = new TextView(_context);
		descLabel4.setText("Weather:");
		_description4 = new TextView(_context);

		// 5
		// date
		TextView dateLabel5 = new TextView(_context);
		dateLabel5.setText("Day:");
		_date5 = new TextView(_context);

		// temp min
		TextView tempMinLabel5 = new TextView(_context);
		tempMinLabel5.setText("Low:");
		_tempMin5 = new TextView(_context);

		// temp max
		TextView tempMaxLabel5 = new TextView(_context);
		tempMaxLabel5.setText("High:");
		_tempMax5 = new TextView(_context);

		// weather desc
		TextView descLabel5 = new TextView(_context);
		descLabel5.setText("Weather:");
		_description5 = new TextView(_context);

		// 6
		// date
		TextView dateLabel6 = new TextView(_context);
		dateLabel6.setText("Day:");
		_date6 = new TextView(_context);

		// temp min
		TextView tempMinLabel6 = new TextView(_context);
		tempMinLabel6.setText("Low:");
		_tempMin6 = new TextView(_context);

		// temp max
		TextView tempMaxLabel6 = new TextView(_context);
		tempMaxLabel6.setText("High:");
		_tempMax6 = new TextView(_context);

		// weather desc
		TextView descLabel6 = new TextView(_context);
		descLabel6.setText("Weather:");
		_description6 = new TextView(_context);

		this.addView(dateLabel0);
		this.addView(_date0);
		this.addView(tempMaxLabel0);
		this.addView(_tempMax0);
		this.addView(tempMinLabel0);
		this.addView(_tempMin0);
		this.addView(descLabel0);
		this.addView(_description0);
		
		this.addView(dateLabel1);
		this.addView(_date1);
		this.addView(tempMaxLabel1);
		this.addView(_tempMax1);
		this.addView(tempMinLabel1);
		this.addView(_tempMin1);
		this.addView(descLabel1);
		this.addView(_description1);
		
		this.addView(dateLabel2);
		this.addView(_date2);
		this.addView(tempMaxLabel2);
		this.addView(_tempMax2);
		this.addView(tempMinLabel2);
		this.addView(_tempMin2);
		this.addView(descLabel2);
		this.addView(_description2);
		
		this.addView(dateLabel3);
		this.addView(_date3);
		this.addView(tempMaxLabel3);
		this.addView(_tempMax3);
		this.addView(tempMinLabel3);
		this.addView(_tempMin3);
		this.addView(descLabel3);
		this.addView(_description3);
		
		this.addView(dateLabel4);
		this.addView(_date4);
		this.addView(tempMaxLabel4);
		this.addView(_tempMax4);
		this.addView(tempMinLabel4);
		this.addView(_tempMin4);
		this.addView(descLabel4);
		this.addView(_description4);
		
		this.addView(dateLabel5);
		this.addView(_date5);
		this.addView(tempMaxLabel5);
		this.addView(_tempMax5);
		this.addView(tempMinLabel5);
		this.addView(_tempMin5);
		this.addView(descLabel5);
		this.addView(_description5);
		
		this.addView(dateLabel6);
		this.addView(_date6);
		this.addView(tempMaxLabel6);
		this.addView(_tempMax6);
		this.addView(tempMinLabel6);
		this.addView(_tempMin6);
		this.addView(descLabel6);
		this.addView(_description6);
	}

}
