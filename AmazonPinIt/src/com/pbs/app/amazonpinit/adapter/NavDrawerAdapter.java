package com.pbs.app.amazonpinit.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbs.app.amazonpinit.R;

public class NavDrawerAdapter extends ArrayAdapter<NavListItemModel> {

	private final List<NavListItemModel> items;
	private final Context context;

	public NavDrawerAdapter(Context context, int resource, List<NavListItemModel> objects) {
		super(context, resource, objects);
		this.context = context;
		this.items = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.drawer_list_item, parent, false);

		ImageView imgIcon = (ImageView) rowView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
		TextView txtCount = (TextView) rowView.findViewById(R.id.counter);

		final NavListItemModel model = items.get(position);
		imgIcon.setImageResource(model.getIcon());
		txtTitle.setText(model.getTitle());
		txtCount.setText(model.getCounter());
		if (!model.isHasCounter())
			txtCount.setVisibility(View.GONE);

		return rowView;
	}

}
