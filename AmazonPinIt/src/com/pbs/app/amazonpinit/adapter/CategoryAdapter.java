package com.pbs.app.amazonpinit.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pbs.app.amazonpinit.R;

public class CategoryAdapter extends ArrayAdapter<CategoryModel> {

	private final Context context;
	private final List<CategoryModel> objects;

	public CategoryAdapter(Context context, int resource, List<CategoryModel> objects) {
		super(context, resource, objects);
		this.context = context;
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.list_category, parent, false);

		CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkbox);
		TextView category = (TextView) rowView.findViewById(R.id.category);

		final CategoryModel model = objects.get(position);
		checkBox.setChecked(model.isSelected());
		category.setText(model.getCategory());

		return rowView;
	}

}
