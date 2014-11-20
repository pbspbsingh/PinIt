package com.pbs.app.amazonpinit.start;

import static com.pbs.app.amazonpinit.utils.Common.isEmpty;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.pbs.app.amazonpinit.HomeActivity;
import com.pbs.app.amazonpinit.R;
import com.pbs.app.amazonpinit.utils.Constants;
import com.pbs.app.amazonpinit.utils.HttpCaller;

public class SignIn extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		{
			final SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, 0);
			if (sharedPreferences.contains("userId") && sharedPreferences.contains("userName"))
				startMainActivity();
		}
		setContentView(R.layout.activity_signin);
		final CheckBox registered = (CheckBox) findViewById(R.id.registered);
		registered.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					findViewById(R.id.userName).setVisibility(View.GONE);
					((Button) findViewById(R.id.submit)).setText("Sign In");
				} else {
					findViewById(R.id.userName).setVisibility(View.VISIBLE);
					((Button) findViewById(R.id.submit)).setText("Sign Up");
				}
			}
		});
		findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (registered.isChecked())
					signIn();
				else
					signUp();
			}

		});
	}

	private void signUp() {
		final String userName = ((EditText) findViewById(R.id.userName)).getText().toString();
		final String email = ((EditText) findViewById(R.id.email)).getText().toString();
		final String password = ((EditText) findViewById(R.id.password)).getText().toString();
		if (isEmpty(userName) || isEmpty(email) || isEmpty(password)) {
			Toast.makeText(getApplicationContext(), "All Files are mandatory", Toast.LENGTH_SHORT).show();
			return;
		}
		new AsyncTask<Void, String, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					final HttpCaller caller = new HttpCaller("signup");
					caller.addParam("userName", userName);
					caller.addParam("email", email);
					caller.addParam("password", password);
					final String res = caller.getResponse();
					JSONObject json = new JSONObject(res);
					final boolean isRegisterSuccess = json.getBoolean("registered");
					if (isRegisterSuccess)
						publishProgress("success", json.getString("msg"));
					else
						publishProgress("error", json.getString("msg"));
				} catch (IOException e) {
					publishProgress("error");
					e.printStackTrace();
				} catch (JSONException e) {
					publishProgress("error");
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(String... values) {
				if (values[0].equals("error")) {
					if (values.length >= 2)
						Toast.makeText(getApplicationContext(), values[1], Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getApplicationContext(), "Something went wrong, please try again.", Toast.LENGTH_LONG).show();
				} else if (values[0].equals("success")) {
					login(values[1], ((EditText) findViewById(R.id.userName)).getText().toString());
				}
			}

		}.execute();
	}

	protected void signIn() {
		final String email = ((EditText) findViewById(R.id.email)).getText().toString();
		final String password = ((EditText) findViewById(R.id.password)).getText().toString();
		if (isEmpty(email) || isEmpty(password)) {
			Toast.makeText(getApplicationContext(), "All Files are mandatory", Toast.LENGTH_SHORT).show();
			return;
		}
		new AsyncTask<Void, String, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					final HttpCaller caller = new HttpCaller("signin");
					caller.addParam("email", email);
					caller.addParam("password", password);
					final String res = caller.getResponse();
					JSONObject json = new JSONObject(res);
					if (json.getBoolean("valid")) {
						JSONObject user = json.getJSONObject("user");
						publishProgress("success", String.valueOf(user.getInt("userId")), user.getString("userName"));
					} else {
						publishProgress("error", "Invalid email or password");
					}
				} catch (IOException e) {
					publishProgress("error");
					e.printStackTrace();
				} catch (JSONException e) {
					publishProgress("error");
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(String... values) {
				if (values[0].equals("error")) {
					if (values.length >= 2)
						Toast.makeText(getApplicationContext(), values[1], Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getApplicationContext(), "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
				} else if (values[0].equals("success")) {
					login(values[1], values[2]);
				}
			}

		}.execute();
	}

	private void login(String userId, String userName) {
		final SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, 0);
		final Editor editor = sharedPreferences.edit();
		editor.putString("userId", userId);
		editor.putString("userName", userName);
		editor.commit();
		startMainActivity();
	}

	private void startMainActivity() {
		Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		finish();
	}

	public void notImplemented(View v) {
		Toast.makeText(getApplicationContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
	}

}
