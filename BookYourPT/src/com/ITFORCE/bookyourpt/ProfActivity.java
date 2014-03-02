package com.ITFORCE.bookyourpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ProfActivity extends BaseActivity {
	private Button verButton;
	private EditText fName, lName, memId, hkId, nName;
	private RadioGroup genderGrp;
	private RadioButton maleRadio, femaleRadio;
	private ParseUser curUser;
	private String gender, fname, lname, memid, hkid, nname;

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
		nName = (EditText) findViewById(R.id.txt_profile_nname);
		memId = (EditText) findViewById(R.id.txt_profile_memid);
		hkId = (EditText) findViewById(R.id.txt_profile_hkid);
		maleRadio = (RadioButton) findViewById(R.id.rad_profile_male);
		femaleRadio = (RadioButton) findViewById(R.id.rad_profile_female);
		genderGrp = (RadioGroup) findViewById(R.id.rad_profile_grp);
	}

	private void functionInit() {
		verButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgressDialog();
				outSourceVerify();
			}
		});
		genderGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup grp, int checkedId) {
				gender = (checkedId == maleRadio.getId()) ? "M" : "F";
			}
		});
	}

	private boolean getData() {
		fname = fName.getText().toString();
		lname = lName.getText().toString();
		nname = nName.getText().toString();
		hkid = hkId.getText().toString();
		memid = memId.getText().toString();

		if (fname.isEmpty() || lname.isEmpty() || nname.isEmpty() || hkid.isEmpty()
				|| memid.isEmpty() || gender.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private void outSourceVerify() {
		if (getData()) {
			if (memid.length() == 10) {
				memberQuery();
			} else if (memid.length() == 11) {
				trainerQuery();
			} else {
				Toast.makeText(getApplicationContext(), "Sorry, your Member ID is not correct",
						Toast.LENGTH_LONG).show();
				return;
			}
		} else {
			Toast.makeText(getApplicationContext(), "Sorry, Please input all fields",
					Toast.LENGTH_LONG).show();
			return;
		}
	}

	private void memberQuery() {
		ParseQuery<ParseObject> memQuery = ParseQuery.getQuery("GymMember");
		memQuery.whereEqualTo("member_id", memid);
		memQuery.whereEqualTo("member_fname", fname);
		memQuery.whereEqualTo("member_lname", lname);
		memQuery.whereEqualTo("gender", gender);
		memQuery.whereEqualTo("member_hkid", hkid);
		memQuery.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				if (e == null) {
					if (count == 0 || count > 1) {
						Toast.makeText(getApplicationContext(), "Personal Profile Not Match",
								Toast.LENGTH_LONG).show();
						hideProgressDialog();
						return;
					} else if (count == 1) {
						isMemberVerified();
					}
				} else {
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
							.show();
					hideProgressDialog();
				}
			}
		});
	}

	private void trainerQuery() {
		ParseQuery<ParseObject> memQuery = ParseQuery.getQuery("GymTrainer");
		memQuery.whereEqualTo("staff_id", memid);
		memQuery.whereEqualTo("trainer_fname", fname);
		memQuery.whereEqualTo("trainer_lname", lname);
		memQuery.whereEqualTo("gender", gender);
		memQuery.whereEqualTo("trainer_hkid", hkid);
		memQuery.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				if (e == null) {
					if (count == 0 || count > 1) {
						Toast.makeText(getApplicationContext(), "Personal Profile Not Match",
								Toast.LENGTH_LONG).show();
						hideProgressDialog();
						return;
					} else if (count == 1) {
						isTrainerVerified();
						hideProgressDialog();
					}
				} else {
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
							.show();
					hideProgressDialog();
				}
			}
		});
	}

	private void isMemberVerified() {
		curUser.put("memberId", memid);
		curUser.put("fName", fname);
		curUser.put("lName", lname);
		curUser.put("nickName", nname);
		curUser.put("gender", gender);
		curUser.put("role", "member");
		curUser.put("verified", true);
		curUser.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(getApplicationContext(),
							"Congratulations! Let's find a personal trainer!", Toast.LENGTH_LONG)
							.show();
					Intent i = new Intent(ProfActivity.this, MainActivity.class);
					startActivity(i);
					hideProgressDialog();
					finish();
				} else {
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
							.show();
					hideProgressDialog();
					return;
				}
			}
		});
	}

	private void isTrainerVerified() {
		curUser.put("staffId", memid);
		curUser.put("nickName", nname);
		curUser.put("fName", fname);
		curUser.put("lName", lname);
		curUser.put("gender", gender);
		curUser.put("role", "trainer");
		curUser.put("verified", true);
		curUser.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(getApplicationContext(), "Welcome! Staff", Toast.LENGTH_LONG)
							.show();
					Intent i = new Intent(ProfActivity.this, MainActivity.class);
					startActivity(i);
					hideProgressDialog();
					finish();
				} else {
					hideProgressDialog();
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
							.show();
					hideProgressDialog();
					return;
				}
			}
		});
	}

}
