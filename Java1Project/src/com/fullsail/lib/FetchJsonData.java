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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

/*
 * This class is for fetching JSON data from a url and from a local source
 */

// Great solution: http://prativas.wordpress.com/category/android/part-1-retrieving-a-json-file/
public class FetchJsonData {
	public static String jsonToStringFromAssetFolder(String fileName,Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(fileName);

        byte[] data = new byte[file.available()];
        file.read(data);
        file.close();
        return new String(data);
    }
	
	/*
	 * Retrieve a response from the url destination
	 */
	public static String getURLStringResponse(URL url) {		
		String response = "";
		try {
			URLConnection connection = url.openConnection();
			BufferedInputStream bin = new BufferedInputStream(connection.getInputStream());
			
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer responseBuffer = new StringBuffer();
			
			while ((bytesRead = bin.read(contentBytes)) != -1) {
				response = new String(contentBytes, 0, bytesRead);
				responseBuffer.append(response);
			}
			return responseBuffer.toString();
			
		} catch (Exception e) {
			Log.e("URL response error.", "getURLStringResponse");
		}
		
		return response;
	}
}
