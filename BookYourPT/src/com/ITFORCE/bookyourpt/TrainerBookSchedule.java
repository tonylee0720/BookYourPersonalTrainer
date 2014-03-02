package com.ITFORCE.bookyourpt;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ITFORCE.bookyourpt.core.DatePickerFragment;
import com.ITFORCE.bookyourpt.core.DatePickerFragment.DatePickerFragmentListener;
import com.ITFORCE.bookyourpt.core.TimePickerFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class TrainerBookSchedule extends BaseActivity implements DatePickerFragmentListener {
	private EditText bookSTime, bookETime, bookDate;
	private Button bookBtn;
	private ListView bookList;
	private TrainerScheduleAvailableListAdapter adapter;
	private String objId;
	private SimpleDateFormat dateFormat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_schedule);
		objId = getIntent().getExtras().getString("objectId");
		localInit();
		functionInit();
	}

	private void localInit() {
		bookSTime = (EditText) findViewById(R.id.schedule_BooksTime);
		bookETime = (EditText) findViewById(R.id.schedule_BookeTime);
		bookDate = (EditText) findViewById(R.id.schedule_BookDate);
		bookBtn = (Button) findViewById(R.id.schedule_trainer_btn_book);
		bookList = (ListView) findViewById(R.id.schuedule_listview);
		dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss", Locale.getDefault());
	}

	private void functionInit() {
		onSetDate(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		bookBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				book();
			}
		});
		bookDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				datePickerCall();
			}
		});
		bookSTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				timePickerCall(bookSTime);
			}
		});
		bookETime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				timePickerCall(bookETime);
			}
		});
	}

	private void timePickerCall(EditText target) {
		OnTimeSetListener timeSetListener = (target == bookSTime) ? stimeSetListner
				: etimeSetListner;
		TimePickerFragment newFragment = new TimePickerFragment(TrainerBookSchedule.this,
				timeSetListener, Calendar.getInstance().get(Calendar.HOUR),
				TimePickerFragment.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE)
						+ TimePickerFragment.TIME_PICKER_INTERVAL), true);
		newFragment.show();
	}

	private void datePickerCall() {
		DatePickerFragment newFragment = new DatePickerFragment();
		newFragment.setOnSetDateListener(this);
		newFragment.show(getFragmentManager(), "btnDateTime");
	}

	@Override
	public void onSetDate(int year, int month, int day) {
		String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", month + 1, day, year);
		bookDate.setText(date);
		querySchedule();
	}

	private void querySchedule() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
		query.whereEqualTo("trainerId", objId);
		query.whereEqualTo("accepted", true);
		query.whereGreaterThanOrEqualTo("sTime", getTimeStamp(null));
		 query.whereLessThan("sTime", getTimeStamp(null) + 86400);
		query.orderByDescending("sTime");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> trainerScheduleList, ParseException e) {
				if (e == null) {
					adapter = new TrainerScheduleAvailableListAdapter(TrainerBookSchedule.this,
							to48TimeSlot(trainerScheduleList));
					bookList.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else {
					Log.d("userOder", "Error: " + e.getMessage());
				}
			}
		});
	}

	private OnTimeSetListener stimeSetListner = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int h, int m) {
			String time = String.format("%02d:%02d", h, m);
			bookSTime.setText(time);
		}
	};

	private OnTimeSetListener etimeSetListner = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int h, int m) {
			String time = String.format("%02d:%02d", h, m);
			bookETime.setText(time);
		}
	};

	private int getTimeStamp(EditText timeText) {
		int result = 0;

		try {
			Long convertedDate = null;
			String date = bookDate.getText().toString();
			String time = (timeText != null) ? " " + timeText.getText().toString() + ":00"
					: " 00:00:00";
			String datetime = date + time;
			convertedDate = dateFormat.parse(datetime).getTime();
			Timestamp timeStamp = new Timestamp(convertedDate);
			result = (int) (timeStamp.getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.v("sTime", result + "");
		return result;
	}

	private void book() {
		if (bookDate.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "Please select date first!", Toast.LENGTH_LONG)
					.show();
		} else if (bookETime.getText().length() == 0 || bookSTime.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "Please select start time and end time",
					Toast.LENGTH_LONG).show();
		} else if (bookSTime.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "Please select start time", Toast.LENGTH_LONG)
					.show();
		} else if (bookETime.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "Please select end time", Toast.LENGTH_LONG)
					.show();
		} else {
			ParseObject newSchedule = new ParseObject("Schedule");
			newSchedule.put("clientId", ParseUser.getCurrentUser().getObjectId());
			newSchedule.put("trainerId", objId);
			newSchedule.put("sTime", getTimeStamp(bookSTime));
			newSchedule.put("eTime", getTimeStamp(bookETime));
			newSchedule.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					if (e == null) {
						Toast.makeText(getApplicationContext(),
								"Done! Please wait to confirm purchase", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
								.show();
					}
				}
			});
			Log.v("sTime", getTimeStamp(bookSTime) + "");
			Log.v("eTime", getTimeStamp(bookETime) + "");
		}
	}

	private ParseObject[] to48TimeSlot(List<ParseObject> srcList) {
		final Calendar scal = Calendar.getInstance();
		final Calendar ecal = Calendar.getInstance();
		ParseObject[] result  = new ParseObject[48];
		for (int position = 0; position < srcList.size(); position++) {
			srcList.get(position).getString("clientId");
			long sTime = srcList.get(position).getLong("sTime")*1000 ;
			long eTime = srcList.get(position).getLong("eTime")*1000 ;
			
			scal.setTimeInMillis(sTime);
			ecal.setTimeInMillis(eTime);
			
			int shr =  scal.get(Calendar.HOUR)*2;
			int smin =  scal.get(Calendar.MINUTE);
			int ehr =  ecal.get(Calendar.HOUR)*2;
			int emin =  ecal.get(Calendar.MINUTE);
			int sPos = (smin>0)?shr++:shr;
			int ePos = (emin>0)?ehr++:ehr;
			for(int st = sPos; st<ePos;st++){
				result[st] = srcList.get(position);
			}
		}
		return result;
	}
}
