/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.lib
 * 
 * @author 	William Saults
 * 
 * date 	Jun 12, 2013
 */
package com.fullsail.lib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

// This class will be used to create location objects from the FourSquare API.
/*
 * A venue is made up of...
 * 
 *  id: "42377700f964a52024201fe3"
 *  name: "Brooklyn Heights Promenade"
 *  contact: { }
 *  location: {}
 *  canonicalUrl:
 *  categories: []
 *  verified: false
 *  stats: {}
 *  specials: {}
 *  hereNow: {}
 *  referralId: "v-1371082711"
 */
public class Venue {
	private String id;
	private String name;
	private JSONArray location;
	private JSONArray categories;
	
	// Accessors and Mutators.
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JSONArray getLocation() {
		return location;
	}
	public void setLocation(JSONArray location) {
		this.location = location;
	}
	public JSONArray getCategories() {
		return categories;
	}
	public void setCategories(JSONArray categories) {
		this.categories = categories;
	}
	
	public Venue(JSONObject obj) throws JSONException {
		this.id = obj.getString("id");
		this.name = obj.getString("name");
//		this.location = obj.getJSONArray("location");
//		this.categories = obj.getJSONArray("categories");
		
		Log.i("id: ", id);
		Log.i("name: ", name);
//		Log.i("location: ", location.toString());
//		Log.i("categories: ", categories.toString());
	}
}
