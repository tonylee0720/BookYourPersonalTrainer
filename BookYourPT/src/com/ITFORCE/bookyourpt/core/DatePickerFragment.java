package com.ITFORCE.bookyourpt.core;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private DatePickerFragmentListener mListener;

	public static interface DatePickerFragmentListener {
		public void onSetDate(int year, int month, int day);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(getActivity(), this, year, month, day);

	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		if (mListener != null) {
			mListener.onSetDate(year, month, day);
		}

	}

	public void setOnSetDateListener(DatePickerFragmentListener listener) {
		mListener = listener;
	}

}
