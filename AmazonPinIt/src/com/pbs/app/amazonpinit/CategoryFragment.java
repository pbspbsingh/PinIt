package com.pbs.app.amazonpinit;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.pbs.app.amazonpinit.adapter.CategoryAdapter;
import com.pbs.app.amazonpinit.adapter.CategoryModel;
import com.pbs.app.amazonpinit.utils.HttpCaller;

public class CategoryFragment extends Fragment {

	private ListView listView;
	private View loading;
	private View content;
	
	public CategoryFragment() {
	}
	
	private final View.OnClickListener save = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			final StringBuilder selectedCats = new StringBuilder();
			int size = listView.getAdapter().getCount();
			int count = 0;
			for (int i = 0; i < size; i++) {
				CategoryModel model = (CategoryModel) listView.getAdapter().getItem(i);
				if (model.isSelected()) {
					selectedCats.append(model.getCategory()).append("----");
					count++;
				}
			}
			if (count < 4) {
				Toast.makeText(getActivity(), "Please select more than 3 categories", Toast.LENGTH_SHORT).show();
				return;
			}

			loading.setVisibility(View.VISIBLE);
			content.setVisibility(View.GONE);
			new AsyncTask<Void, String, Boolean>() {

				@Override
				protected Boolean doInBackground(Void... params) {
					try {
						HttpCaller caller = new HttpCaller("user/saveCategories");
						caller.addParam("categories", selectedCats.toString());
						caller.addParam("userId", getArguments().getString("userId"));
						final String res = caller.getResponse();
						return Boolean.valueOf(res);
					} catch (Exception e) {
						publishProgress("error");
					}
					return false;
				}

				protected void onProgressUpdate(String... values) {
					Toast.makeText(getActivity(), "Saving failed Please try again!", Toast.LENGTH_SHORT).show();
					loading.setVisibility(View.GONE);
					content.setVisibility(View.VISIBLE);
				};

				@Override
				protected void onPostExecute(Boolean result) {
					if (result) {
						Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
						((HomeActivity)getActivity()).selectScreen(0);
						loading.setVisibility(View.GONE);
						content.setVisibility(View.VISIBLE);
					} else {
						Toast.makeText(getActivity(), "Saving failed Please try again!", Toast.LENGTH_SHORT).show();
						loading.setVisibility(View.GONE);
						content.setVisibility(View.VISIBLE);
					}
				}

			}.execute();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_category, container, false);
		listView = (ListView) rootView.findViewById(R.id.categoryListView);
		rootView.findViewById(R.id.save).setOnClickListener(save);

		this.loading = rootView.findViewById(R.id.loading);
		this.content = rootView.findViewById(R.id.content);

		if (savedInstanceState == null) {
			loading.setVisibility(View.VISIBLE);
			content.setVisibility(View.GONE);
		}
		loadCategories();
		return rootView;
	}

	private void loadCategories() {
		final String userId = getArguments().getString("userId");
		new AsyncTask<Void, String, List<CategoryModel>>() {

			@Override
			protected List<CategoryModel> doInBackground(Void... params) {
				final List<CategoryModel> list = new ArrayList<CategoryModel>();
				try {
					final HttpCaller caller = new HttpCaller("user/category");
					caller.addParam("userId", userId);
					JSONArray array = new JSONArray(caller.getResponse());
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						CategoryModel model = new CategoryModel();
						model.setCategory(obj.getString("category"));
						model.setSelected(obj.getBoolean("selected"));

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
			protected void onPostExecute(List<CategoryModel> result) {
				if (result != null)
					listView.setAdapter(new CategoryAdapter(getActivity(), R.layout.fragment_category, result));
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);
						checkbox.setChecked(!checkbox.isChecked());
						CategoryModel model = (CategoryModel) parent.getItemAtPosition(position);
						model.setSelected(checkbox.isChecked());
					}
				});
				loading.setVisibility(View.GONE);
				content.setVisibility(View.VISIBLE);
			};

		}.execute();
	}

}
