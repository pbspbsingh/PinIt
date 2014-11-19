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
import com.pbs.app.amazonpinit.utils.Common;

public class TimelineGridAdapter extends ArrayAdapter<TimelineModel> {

	private final Context context;
	private final List<TimelineModel> list;

	public TimelineGridAdapter(Context context, int resource, List<TimelineModel> objects) {
		super(context, resource, objects);
		this.context = context;
		this.list = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.layout_timeline, parent, false);
		final TimelineModel model = list.get(position);

		ImageView icon = (ImageView) rowView.findViewById(R.id.appIcon);
		icon.setImageBitmap(Common.convertStr2BitMap(model.getAppIcon()));

		((TextView) rowView.findViewById(R.id.appTitle)).setText(model.getAppTitle());
		((TextView) rowView.findViewById(R.id.appCompany)).setText(model.getAppCompany());
		((TextView) rowView.findViewById(R.id.comment)).setText(model.getComment());
		((TextView) rowView.findViewById(R.id.favCount)).setText(String.valueOf(model.getLikesCount()));
		((TextView) rowView.findViewById(R.id.pinCount)).setText(String.valueOf(model.getPinCounts()));
		((TextView) rowView.findViewById(R.id.username)).setText(model.getUserName());
		return rowView;
	}
}
