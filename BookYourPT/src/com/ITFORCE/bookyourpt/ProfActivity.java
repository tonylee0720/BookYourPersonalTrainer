package com.ITFORCE.bookyourpt;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ProfActivity extends BaseActivity {
	Button verButton;
	EditText fName, lName, memId, hkId;
	RadioButton maleRadio, femaleRadio;
	ParseUser curUser;
	String gender, fname, lname, memid, hkid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createprofile);
		localInit();
		functionInit();
	}

	private void localInit() {
		curUser = ParseUser.getCurrentUser();
		verButton = (Button) findViewById(R.id.btn_verify);
		fName = (EditText) findViewById(R.id.txt_profile_fname);
		lName = (EditText) findViewById(R.id.txt_profile_lname);
		memId = (EditText) findViewById(R.id.txt_profile_memid);
		hkId = (EditText) findViewById(R.id.txt_profile_hkid);
		maleRadio = (RadioButton) findViewById(R.id.rad_profile_male);
		femaleRadio = (RadioButton) findViewById(R.id.rad_profile_female);

	}

	private void functionInit() {
		gender = (maleRadio.isChecked()) ? "Male" : "Female";
		fname = fName.getText().toString();
		lname = lName.getText().toString();
		hkid = hkId.getText().toString();
		memid = memId.getText().toString();

		verButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				curUser.put("member_id", memid);
				curUser.put("hkid", hkid);
				curUser.put("fname", fname);
				curUser.put("lname", lname);
				curUser.put("gender", gender);
				curUser.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null) {
							outSourceVerify();
						} else {
							Log.v("log", e.getMessage());
						}
					}
				});
			}
		});
	}

	private void outSourceVerify() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("GymMember");
		query.whereEqualTo("member_id", memid);
		query.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				if (e == null) {
					if (count > 0) {
						Toast.makeText(getApplicationContext(), "Personal Profile Not Match",
								Toast.LENGTH_LONG).show();
						return;
					} else {
						isVerified();
					}
				} else {
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

	private void isVerified() {
		curUser.put("verified", true);
		curUser.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(getApplicationContext(),
							"Congratulations! Let's find a personal trainer!", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

}
