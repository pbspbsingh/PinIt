package com.pbs.app.amazonpinit;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.pbs.app.amazonpinit.adapter.TimelineGridAdapter;
import com.pbs.app.amazonpinit.adapter.TimelineModel;
import com.pbs.app.amazonpinit.utils.HttpCaller;

public class MeFragment extends Fragment {

	private TextView totalPins;
	private TextView totalLikes;
	private View loader;
	private GridView grid;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_me, container, false);

		((TextView) rootView.findViewById(R.id.username)).setText(getArguments().getString("userName"));
		this.totalPins = (TextView) rootView.findViewById(R.id.pins);
		this.totalLikes = (TextView) rootView.findViewById(R.id.likes);
		this.loader = rootView.findViewById(R.id.progressBar1);
		this.grid = (GridView) rootView.findViewById(R.id.timelineGrid);

		refresh();
		return rootView;
	}

	public void refresh() {
		grid.setVisibility(View.GONE);
		loader.setVisibility(View.VISIBLE);

		final String userId = getArguments().getString("userId");
		new AsyncTask<Void, String, List<TimelineModel>>() {

			@Override
			protected List<TimelineModel> doInBackground(Void... params) {
				final List<TimelineModel> list = new ArrayList<TimelineModel>();
				try {
					final HttpCaller caller = new HttpCaller("user/mytimeline");
					caller.addParam("userId", userId);
					JSONObject object = new JSONObject(caller.getResponse());
					
					publishProgress("success", Integer.toString(object.getInt("totalLikes")), Integer.toString(object.getInt("totalPins")));
					
					JSONArray array = object.getJSONArray("timelines");
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						final TimelineModel model = new TimelineModel();
						model.setAppCompany(obj.getString("appCompany"));
						model.setAppIcon(obj.getString("appIcon"));
						model.setAppId(obj.getInt("appId"));
						model.setAppPackage(obj.getString("appPackage"));
						model.setAppTitle(obj.getString("appTitle"));
						model.setComment(obj.getString("comment"));
						model.setLikesCount(obj.getInt("likesCount"));
						model.setNumberOfUsers(obj.getInt("numberOfUsers"));
						model.setPinCounts(obj.getInt("pinCounts"));
						model.setRatings(obj.getInt("ratings"));
						model.setUserId(obj.getInt("userId"));
						model.setUserName(obj.getString("userName"));

						list.add(model);
					}
				} catch (Exception e) {
					publishProgress("error");
					e.printStackTrace();
				}
				return list;
			}
			
			@Override
			public void onProgressUpdate(String... values) {
				if (getActivity() != null && values[0].equals("error"))
					Toast.makeText(getActivity(), "Oops Network call failed.", Toast.LENGTH_SHORT).show();
				else if(values[0].equals("success")) {
					totalLikes.setText(values[1] + "\nLikes");
					totalPins.setText(values[2] + "\nPins");
				}
			};
			
			@Override
			protected void onPostExecute(List<TimelineModel> result) {
				if (result != null)
					grid.setAdapter(new TimelineGridAdapter(getActivity(), R.layout.fragment_home, result));
				grid.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MeFragment.this.onItemClick(parent, view, position, id);
					}
				});
				loader.setVisibility(View.GONE);
				grid.setVisibility(View.VISIBLE);
			};
		}.execute();
	}

	protected void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
	}

}
