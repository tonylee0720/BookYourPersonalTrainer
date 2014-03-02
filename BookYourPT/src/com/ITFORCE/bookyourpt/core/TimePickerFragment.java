package com.ITFORCE.bookyourpt.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class TimePickerFragment extends TimePickerDialog {

	public TimePickerFragment(Context context, OnTimeSetListener callBack, int hourOfDay,
			int minute, boolean is24HourView) {
		super(context, callBack, hourOfDay, minute, is24HourView);
	}

	// private TimePickerFragmentListener mListener;
	public static final int TIME_PICKER_INTERVAL = 30;
	private boolean mIgnoreEvent = false;
	private TimePicker timePicker;

	// public static interface TimePickerFragmentListener {
	// public void onSetTime(int h, int m);
	// }

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		super.onTimeChanged(view, hourOfDay, minute);
		if (!mIgnoreEvent) {
			minute = getRoundedMinute(minute);
			mIgnoreEvent = true;
			view.setCurrentMinute(minute);
			mIgnoreEvent = false;
		}
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		try {
			Class<?> classForid = Class.forName("com.android.internal.R$id");
			Field timePickerField = classForid.getField("timePicker");
			this.timePicker = (TimePicker) findViewById(timePickerField.getInt(null));
			Field field = classForid.getField("minute");

			NumberPicker mMinuteSpinner = (NumberPicker) timePicker
					.findViewById(field.getInt(null));
			mMinuteSpinner.setMinValue(0);
			mMinuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
			List<String> displayedValues = new ArrayList<String>();
			for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
				displayedValues.add(String.format("%02d", i));
			}
			mMinuteSpinner.setDisplayedValues(displayedValues.toArray(new String[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// @Override
	// public Dialog onCreateDialog(Bundle savedInstanceState) {
	// Calendar cal = Calendar.getInstance();
	// int hour = cal.get(Calendar.HOUR_OF_DAY);
	// int minute = getRoundedMinute(Calendar.MINUTE) + TIME_PICKER_INTERVAL;
	// return new TimePickerDialog(getActivity(), this, hour, minute,
	// DateFormat.is24HourFormat(getActivity()));
	// }

	public static int getRoundedMinute(int minute) {
		if (minute % TIME_PICKER_INTERVAL != 0) {
			int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
			minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
			if (minute == 60)
				minute = 0;
		}

		return minute;
	}

	// @Override
	// public void onTimeSet(TimePicker arg0, int hr, int min) {
	// if (mListener != null) {
	// mListener.onSetTime(hr, min);
	// }
	// }

	// public void setOnSetTimeListener(TimePickerFragmentListener listener) {
	// mListener = listener;
	// }
}
