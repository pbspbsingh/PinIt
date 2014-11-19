package com.pbs.app.amazonpinit.start;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pbs.app.amazonpinit.R;
import com.pbs.app.amazonpinit.utils.Constants;
import com.pbs.app.amazonpinit.utils.HttpCaller;

public class SplashActvity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);

		testNetwork();
	}

	private void testNetwork() {
		if (Constants.IS_DEV_MODE) {
			final SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, 0);
			final Editor editor = sharedPreferences.edit();
			editor.putString("userId", "5");
			editor.putString("userName", "Prashant Singh");
			editor.commit();
		}
		(new AsyncTask<Void, Integer, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					HttpCaller caller = new HttpCaller("");
					final String response = caller.getResponse();
					Log.i("Testing", response);
					if (response != null) {
						publishProgress(1);
					}
				} catch (IOException e) {
					publishProgress(0);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				if (values[0] == 1) {
					final Intent intent = new Intent(SplashActvity.this, SignIn.class);
					startActivity(intent);
					finish();
				} else {
					findViewById(R.id.facebook).setVisibility(View.VISIBLE);
					findViewById(R.id.readyToGetStarted).setVisibility(View.VISIBLE);
				}
			}
		}).execute();

	}

	public void retry(View v) {
		testNetwork();
	}
}
