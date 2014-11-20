package com.pbs.app.amazonpinit;

import static com.pbs.app.amazonpinit.utils.Common.convertStr2BitMap;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pbs.app.amazonpinit.utils.Common;
import com.pbs.app.amazonpinit.utils.Constants;
import com.pbs.app.amazonpinit.utils.HttpCaller;

public class PinActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin_app);

		final Bundle extras = getIntent().getExtras();
		((ImageView) findViewById(R.id.appIcon)).setImageBitmap(convertStr2BitMap(extras.getString("appIcon")));
		((TextView) findViewById(R.id.appTitle)).setText(extras.getString("appTitle"));
		if (getIntent().getExtras().getBoolean("isFromSDK"))
			findViewById(R.id.ratingBar1).setVisibility(View.VISIBLE);
	}

	public void close(View v) {
		finish();
	}

	public void pinIt(View v) {
		final String userId = getSharedPreferences(Constants.PREF_NAME, 0).getString("userId", "-1");
		final String appId = String.valueOf(getIntent().getExtras().getInt("appId"));
		final String comments = ((EditText) findViewById(R.id.comment)).getText().toString();
		final int rating = (int) ((RatingBar) findViewById(R.id.ratingBar1)).getRating();
		if (Common.isEmpty(comments)) {
			Toast.makeText(getApplicationContext(), "Please comment something!", Toast.LENGTH_SHORT).show();
			return;
		}
		new AsyncTask<Void, String, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					HttpCaller caller = new HttpCaller("user/comment");
					caller.addParam("userId", userId);
					caller.addParam("appId", appId);
					caller.addParam("comment", comments);
					caller.addParam("isLiked", "false");
					caller.addParam("rating", String.valueOf(rating));
					final String response = caller.getResponse();
					return Boolean.parseBoolean(response);
				} catch (Exception e) {
					e.printStackTrace();
					publishProgress("error");
				}
				return false;
			}

			@Override
			protected void onProgressUpdate(String... values) {
				if (values[0].equals("error"))
					Toast.makeText(getApplicationContext(), "Saving failed, please try again!", Toast.LENGTH_SHORT).show();
			};
			
			protected void onPostExecute(Boolean result) {
				if(result)
					finish();
				else
					Toast.makeText(getApplicationContext(), "Saving failed, please try again!", Toast.LENGTH_SHORT).show();
			};
		}.execute();
	}
}
