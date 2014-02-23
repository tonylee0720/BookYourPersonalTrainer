package com.ITFORCE.bookyourpt;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.parse.ParseUser;

public class TrainerListAdapter extends BaseAdapter {

	private Activity activity;
	private List<ParseUser> data;
	private static LayoutInflater inflater = null;

	public TrainerListAdapter(Activity a, List<ParseUser> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null)
			view = inflater.inflate(R.layout.row_trainerlist, null);

		TextView trainerName = (TextView) view.findViewById(R.id.row_trainer_name); 
		TextView trainerlName = (TextView) view.findViewById(R.id.row_trainer_lname);
		ImageView trainerIcon = (ImageView) view.findViewById(R.id.row_trainer_icon); 

		ParseUser curObj = data.get(position);

		// Setting all values in listview
		trainerName.setText(curObj.getString("nickName"));
		trainerlName.setText(curObj.getString("lname"));
		return view;
	}
}
