package com.pbs.app.amazonpinit;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
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
import android.widget.Toast;

import com.pbs.app.amazonpinit.adapter.TimelineGridAdapter;
import com.pbs.app.amazonpinit.adapter.TimelineModel;
import com.pbs.app.amazonpinit.utils.HttpCaller;

public class HomeFragment extends Fragment {

	private View loading;
	private ViewGroup content;
	private GridView gridView;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);

		this.gridView = (GridView) rootView.findViewById(R.id.timelineGrid);

		this.loading = rootView.findViewById(R.id.loading);
		this.content = (ViewGroup) rootView.findViewById(R.id.content);

		loadTimeline();
		return rootView;
	}

	private void loadTimeline() {
		loading.setVisibility(View.VISIBLE);
		content.setVisibility(View.GONE);
		
		final String userId = getArguments().getString("userId");
		new AsyncTask<Void, String, List<TimelineModel>>() {

			@Override
			protected List<TimelineModel> doInBackground(Void... params) {
				final List<TimelineModel> list = new ArrayList<TimelineModel>();
				try {
					final HttpCaller caller = new HttpCaller("user/timelines");
					caller.addParam("userId", userId);
					JSONArray array = new JSONArray(caller.getResponse());
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
				if (getActivity() != null)
					Toast.makeText(getActivity(), "Oops Network call failed.", Toast.LENGTH_SHORT).show();
			};

			@Override
			protected void onPostExecute(List<TimelineModel> result) {
				if (result != null)
					gridView.setAdapter(new TimelineGridAdapter(getActivity(), R.layout.fragment_home, result));
				gridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						HomeFragment.this.onItemClick(parent, view, position, id);
					}
				});
				loading.setVisibility(View.GONE);
				content.setVisibility(View.VISIBLE);
			};
		}.execute();
	}

	protected void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		TimelineModel model = (TimelineModel) parent.getItemAtPosition(position);
		if (model != null) {
			Intent intent = new Intent(getActivity(), AppDetailActivity.class);
			intent.putExtra("appId", model.getAppId());
			intent.putExtra("appIcon", model.getAppIcon());
			intent.putExtra("appTitle", model.getAppTitle());
			intent.putExtra("appPackage", model.getAppPackage());
			intent.putExtra("comment", model.getComment());
			startActivity(intent);
		}
	}

	public void refresh() {
		loadTimeline();
	}

}
