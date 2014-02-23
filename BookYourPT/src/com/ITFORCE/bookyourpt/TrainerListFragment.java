package com.ITFORCE.bookyourpt;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TrainerListFragment extends ListFragment {
	private Display display;
	private int dHeight;
	private ListView list;
	private TrainerListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_trainerlist, container, false);
		list = (ListView) view.findViewById(R.id.frag_trainer_list_view);
		display = getActivity().getWindowManager().getDefaultDisplay();
		dHeight = (int) (display.getHeight() * 0.8);
		// An invisible view added as a header to the list and clicking it leads
		// to the mapfragment
		final TextView invisibleView = new TextView(inflater.getContext());
		invisibleView.setBackgroundColor(Color.TRANSPARENT);
		invisibleView.setHeight(dHeight);
		invisibleView.setOnTouchListener(null);
		list.addHeaderView(invisibleView, null, false);
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("role", "trainer");
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<ParseUser>() {
			public void done(List<ParseUser> trainerList, ParseException e) {
				if (e == null) {
					adapter = new TrainerListAdapter(getActivity(), trainerList);
					list.setAdapter(adapter);
				} else {
					Log.d("userOder", "Error: " + e.getMessage());
				}
			}
		});
		return view;
	}
}
