package com.pbs.app.amazonpinit;

import static com.pbs.app.amazonpinit.utils.Common.convertStr2BitMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbs.app.amazonpinit.utils.HttpCaller;

public class AppDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_details);

		final Bundle extras = getIntent().getExtras();

		((ImageView) findViewById(R.id.appIcon)).setImageBitmap(convertStr2BitMap(extras.getString("appIcon")));
		((TextView) findViewById(R.id.appTitle)).setText(extras.getString("appTitle"));
		((TextView) findViewById(R.id.comment)).setText(extras.getString("comment"));

		loadSuggestions();
	}

	private void loadSuggestions() {
		final int appId = getIntent().getExtras().getInt("appId");
		if (appId != 0)
			new AsyncTask<Void, String, JSONArray>() {
				@Override
				protected JSONArray doInBackground(Void... params) {
					try {
						final HttpCaller caller = new HttpCaller("user/suggestions");
						caller.addParam("appId", String.valueOf(appId));
						return new JSONArray(caller.getResponse());
					} catch (Exception e) {
						e.printStackTrace();
						publishProgress("error");
					}
					return null;
				}

				@Override
				public void onProgressUpdate(String... values) {
					findViewById(R.id.suggestionLoader).setVisibility(View.GONE);
				};

				@Override
				protected void onPostExecute(JSONArray arr) {
					if (arr == null || arr.length() < 3)
						return;
					try {
						final JSONObject obj1 = arr.getJSONObject(0);
						((ImageView) findViewById(R.id.sugAppIcon1)).setImageBitmap(convertStr2BitMap(obj1.getString("appIcon")));
						((TextView) findViewById(R.id.sugAppTitle1)).setText(obj1.getString("appTitle"));

						final JSONObject obj2 = arr.getJSONObject(1);
						((ImageView) findViewById(R.id.sugAppIcon2)).setImageBitmap(convertStr2BitMap(obj2.getString("appIcon")));
						((TextView) findViewById(R.id.sugAppTitle2)).setText(obj2.getString("appTitle"));

						final JSONObject obj3 = arr.getJSONObject(2);
						((ImageView) findViewById(R.id.sugAppIcon3)).setImageBitmap(convertStr2BitMap(obj3.getString("appIcon")));
						((TextView) findViewById(R.id.sugAppTitle3)).setText(obj3.getString("appTitle"));

						findViewById(R.id.suggestionLoader).setVisibility(View.GONE);
						findViewById(R.id.suggesetions).setVisibility(View.VISIBLE);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

			}.execute();

	}

	public void install(View v) {
		String url = "http://www.amazon.com/gp/mas/dl/android?p=" + getIntent().getExtras().getString("appPackage") + "&showAll=1";
		Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(myIntent);
	}

	public void pinIt(View v) {
	}
}
