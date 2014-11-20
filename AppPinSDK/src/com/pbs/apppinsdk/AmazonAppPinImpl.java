package com.pbs.apppinsdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

class AmazonAppPinImpl implements AmazonAppPin {

	private final Context context;

	public AmazonAppPinImpl(Context context) {
		this.context = context;
	}

	@Override
	public void pinIt() throws NotInstalledException {
		try {
			final PackageManager packageManager = context.getPackageManager();
			final String packageName = context.getPackageName();
			final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
			if (applicationInfo != null) {
				BitmapDrawable applicationIcon = (BitmapDrawable) packageManager.getApplicationIcon(packageName);
				String appTitle = packageManager.getApplicationLabel(applicationInfo).toString();
				String appIcon = convert2Base64(applicationIcon);
				registerApp(appTitle, packageName, appIcon);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotInstalledException(e);
		}
	}

	private String convert2Base64(BitmapDrawable applicationIcon) throws IOException {
		Bitmap bitmap = applicationIcon.getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bitMapData = stream.toByteArray();
		return Base64.encodeToString(bitMapData, Base64.DEFAULT);
	}

	private void registerApp(final String appTitle, final String packageName, final String base64) {
		final AsyncTask<Void, String, Integer> asyncTask = new AsyncTask<Void, String, Integer>() {
			@Override
			protected Integer doInBackground(Void... params) {
				try {
					HttpCaller caller = new HttpCaller("registerApp");
					caller.addParam("appTitle", appTitle);
					caller.addParam("packageName", packageName);
					caller.addParam("appIcon", base64);
					final String response = caller.getResponse();
					return Integer.parseInt(response);
				} catch (Exception e) {
					e.printStackTrace();
					publishProgress("error", e.getMessage());
				}
				return 0;
			}

			protected void onProgressUpdate(String... values) {
				if (values[0].equals("error"))
					Toast.makeText(context, "Error making network calls.", Toast.LENGTH_SHORT).show();
			}

		};
		asyncTask.execute();
		try {
			final Integer appId = asyncTask.get();
			if (appId != 0) {
				Intent intent = new Intent("com.pbs.apppin.PIN", Uri.parse("pinit://hehe"));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("appId", appId);
				intent.putExtra("appIcon", base64);
				intent.putExtra("appTitle", appTitle);
				intent.putExtra("appPackage", packageName);
				intent.putExtra("comment", "blahblash");
				intent.putExtra("isFromSDK", true);
				context.startActivity(intent);
			} else
				Toast.makeText(context, "Error making network calls.", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotInstalledException(e);
		}
	}

}
