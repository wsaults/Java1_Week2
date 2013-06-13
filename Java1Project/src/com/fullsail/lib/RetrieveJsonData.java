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

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

/* Use this to parse.
 * String jsonLocation = converJsonToStringFromAssetFolder(your_filename, your_context);
 * parseJsonFileToJavaObjects(jsonLocation);//this function parses the json data to java object
 */

// Great solution: http://prativas.wordpress.com/category/android/part-1-retrieving-a-json-file/
public class RetrieveJsonData {
	public static String jsonToStringFromAssetFolder(String fileName,Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(fileName);

        byte[] data = new byte[file.available()];
        file.read(data);
        file.close();
        return new String(data);
    }
}
