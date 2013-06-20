package com.fullsail.lib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

public class FileManager {
	@SuppressWarnings("resource")
	public static Boolean storeStringFile(Context context, String filename, String content, Boolean external) {
		try {
			File file;
			FileOutputStream fos;
			if (external) {
				file = new File(context.getExternalFilesDir(null), filename);
				fos = new FileOutputStream(file);
			} else {
				fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			
			fos.write(content.getBytes());
			fos.close();
		} catch (IOException e) {
			Log.e("Write error", filename);
		}
		return true;
	}
	
	@SuppressWarnings("resource")
	public static Boolean storeObjectFile(Context context, String filename, Object content, Boolean external) {
		try {
			File file;
			FileOutputStream fos;
			ObjectOutputStream oos;
			if (external) {
				file = new File(context.getExternalFilesDir(null), filename);
				fos = new FileOutputStream(file);
			} else {
				fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			oos = new ObjectOutputStream(fos);
			oos.writeObject(content);
			oos.close();
			fos.close();
		} catch (IOException e) {
			Log.e("Write error", filename);
		}
		return true;
	}
	
	@SuppressWarnings("resource")
	public static String readStringFile(Context context, String filename, Boolean external) {
		String content = "";
		try {
			File file;
			FileInputStream fin;
			if (external) {
				file = new File(context.getExternalFilesDir(null), filename);
				fin = new FileInputStream(file);
			} else {
				file = new File(filename);
				fin = context.openFileInput(filename);
			}
			BufferedInputStream bin = new BufferedInputStream(fin);
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer contentBuffer = new StringBuffer();
			
			while((bytesRead = bin.read(contentBytes)) != -1) {
				content = new String(contentBytes, 0, bytesRead);
				contentBuffer.append(content);
			}
			content = contentBuffer.toString();
			fin.close();
		} catch (FileNotFoundException e) {
			Log.e("Read error", "File not found " + filename);
		} catch (IOException e) {
			Log.e("Read error", "I/O Error");
		}
		return content;
	}
	
	@SuppressWarnings("resource")
	public static Object readObjectgFile(Context context, String filename, Boolean external) {
		Object content = new Object();
		try {
			File file;
			FileInputStream fin;
			if (external) {
				file = new File(context.getExternalFilesDir(null), filename);
				fin = new FileInputStream(file);
			} else {
				file = new File(filename);
				fin = context.openFileInput(filename);
			}
			
			ObjectInputStream ois = new ObjectInputStream(fin);
			try {
				content = (Object) ois.readObject();
			} catch (ClassNotFoundException e) {
				Log.e("Read error", "Invalid Java object file");
			}
			ois.close();
			fin.close();
		} catch (FileNotFoundException e) {
			Log.e("Read error", "File not found " + filename);
			return null;
		} catch (IOException e) {
			Log.e("Read error", "I/O Error");
		}
		return content;
	}
}
