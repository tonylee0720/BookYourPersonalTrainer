package com.ITFORCE.bookyourpt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseObject;

public class TrainerScheduleAvailableListAdapter extends BaseAdapter {

	private Activity activity;
	private ParseObject[] data;
	private static LayoutInflater inflater = null;

	public TrainerScheduleAvailableListAdapter(Activity a, ParseObject[] parseObjects) {
		activity = a;
		data = parseObjects;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public String getParseObjectId(String objId, int position) {
		objId = data[position].getString("objectId");
		return objId;
	}

	private String getHour(int position) {

		return (position % 2 == 1) ? "" : Integer.toString(position / 2);
	}
	
	private Integer getBgColor(int position){
		return (data[position]==null)?Color.LTGRAY:com.facebook.android.R.color.com_facebook_blue;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.row_schedule_av, null);
			holder.time = (TextView) convertView.findViewById(R.id.row_av_time);
			holder.rl = (RelativeLayout) convertView.findViewById(R.id.row_rl);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.time.setText(getHour(position));
		holder.rl.setBackgroundColor(getBgColor(position));

		return convertView;
	}

	private class ViewHolder {
		TextView time;
		RelativeLayout rl;
	}
}
