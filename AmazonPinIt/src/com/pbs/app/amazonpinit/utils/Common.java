package com.pbs.app.amazonpinit.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Common {
	public static boolean isEmpty(String str) {
		return str == null || str.trim().equals("");
	}
	
	public static Bitmap convertStr2BitMap(String str) {
		byte[] appIcon = Base64.decode(str, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(appIcon, 0, appIcon.length);
	}
 }
