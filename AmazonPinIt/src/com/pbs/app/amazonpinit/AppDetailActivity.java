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
						final String title1 = obj1.getString("appTitle");
						((TextView) findViewById(R.id.sugAppTitle1)).setText(title1.length() > 10 ? title1.substring(0, 10) + "..." : title1);

						final JSONObject obj2 = arr.getJSONObject(1);
						((ImageView) findViewById(R.id.sugAppIcon2)).setImageBitmap(convertStr2BitMap(obj2.getString("appIcon")));
						final String title2 = obj1.getString("appTitle");
						((TextView) findViewById(R.id.sugAppTitle2)).setText(title2.length() > 10 ? title2.substring(0, 10) + "..." : title2);

						final JSONObject obj3 = arr.getJSONObject(2);
						((ImageView) findViewById(R.id.sugAppIcon3)).setImageBitmap(convertStr2BitMap(obj3.getString("appIcon")));
						final String title3 = obj3.getString("appTitle");
						((TextView) findViewById(R.id.sugAppTitle3)).setText(title3.length() > 10 ? title3.substring(0, 10) + "..." : title3);

						findViewById(R.id.suggestionLoader).setVisibility(View.GONE);
						findViewById(R.id.suggesetions).setVisibility(View.VISIBLE);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

			}.execute();

	}

	public void install(View v) {
		final String url = "http://www.amazon.com/gp/mas/dl/android?s=" + getIntent().getExtras().getString("appTitle") + "&showAll=1";
		final Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(myIntent);
	}

	public void pinIt(View v) {
		final Intent intent = new Intent(getApplicationContext(), PinActivity.class);
		intent.putExtras(getIntent().getExtras());
		startActivity(intent);
	}
}
