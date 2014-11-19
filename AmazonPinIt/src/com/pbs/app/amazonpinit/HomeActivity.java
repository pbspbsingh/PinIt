package com.pbs.app.amazonpinit;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.pbs.app.amazonpinit.utils.Constants;
import com.pbs.app.amazonpinit.utils.HttpCaller;

public class HomeActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	private NavigationDrawerFragment mNavigationDrawerFragment;

	private String userName;
	private String userId;

	private HomeFragment homeFragement;

	private MeFragment meFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

		SharedPreferences sf = getSharedPreferences(Constants.PREF_NAME, 0);
		userName = sf.getString("userName", "");
		userId = sf.getString("userId", "-1");

		if (savedInstanceState == null) {
			final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

			final Bundle bundle = new Bundle();
			bundle.putString("userName", userName);
			bundle.putString("userId", userId);

			this.homeFragement = new HomeFragment();
			homeFragement.setArguments(bundle);
			transaction.replace(R.id.container1, homeFragement);

			Fragment fragment2 = new CategoryFragment();
			fragment2.setArguments(bundle);
			transaction.replace(R.id.container2, fragment2);
			
			this.meFragment = new MeFragment();
			fragment2.setArguments(bundle);
			transaction.replace(R.id.container2, fragment2);

			transaction.commit();

			checkFollowedCategories();
		}
	}

	private void checkFollowedCategories() {

		new AsyncTask<Void, String, Void>() {

			protected void onProgressUpdate(String... values) {
				if (values[0].equals("error"))
					Toast.makeText(getApplicationContext(), "Network call failed", Toast.LENGTH_SHORT).show();
				else if (values[0].equals("success") && values.length == 2)
					if (values[1].equals("openCategory")) {
						Log.i("USER", "Need to open category");
						mNavigationDrawerFragment.selectItem(1);
					}
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					HttpCaller caller = new HttpCaller("user/countCategory");
					caller.addParam("userId", userId);
					String res = caller.getResponse();
					if (Integer.parseInt(res) < 3)
						publishProgress("success", "openCategory");
				} catch (Exception e) {
					e.printStackTrace();
					publishProgress("error");
				}
				return null;
			}
		}.execute();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (findViewById(R.id.container1) != null)
			findViewById(R.id.container1).setVisibility(View.GONE);
		if (findViewById(R.id.container2) != null)
			findViewById(R.id.container2).setVisibility(View.GONE);
		if (findViewById(R.id.container3) != null)
			findViewById(R.id.container3).setVisibility(View.GONE);

		switch (position) {
		case 0:
			if (findViewById(R.id.container1) != null) {
				findViewById(R.id.container1).setVisibility(View.VISIBLE);
				homeFragement.refresh();
			}
			break;
		case 1:
			if (findViewById(R.id.container2) != null)
				findViewById(R.id.container2).setVisibility(View.VISIBLE);
			break;
			
		case 2:
			if (findViewById(R.id.container3) != null) {
				findViewById(R.id.container3).setVisibility(View.VISIBLE);
				meFragment.refresh();
			}
			break;

		default:
			if (findViewById(R.id.container1) != null) {
				findViewById(R.id.container1).setVisibility(View.VISIBLE);
				homeFragement.refresh();
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.user, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void selectScreen(int position) {
		mNavigationDrawerFragment.selectItem(position);
	}

}
