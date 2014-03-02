package com.ITFORCE.bookyourpt;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class TrainerDetailActivity extends BaseActivity {
	private String objId;
	private ParseObject mTrainer;
	private TextView nName, lName;
	private Button bookBtn;
	private ImageView icon;
	private Bundle extra;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trainerdetail);
		objId = getIntent().getExtras().getString("objectId");
		extra = getIntent().getExtras();
		localInit();
		functionInit();
	}

	private void localInit() {
		nName = (TextView)findViewById(R.id.detail_trainer_name);
		lName = (TextView)findViewById(R.id.detail_trainer_lname);
		icon = (ImageView)findViewById(R.id.detail_trainer_icon);
		bookBtn = (Button)findViewById(R.id.detail_trainer_btn_book);
	}

	private void functionInit() {
		nName.setText(extra.getString("nickName"));
		lName.setText(extra.getString("lName"));
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("objectId", objId);
		query.findInBackground(new FindCallback<ParseUser>() {
			public void done(List<ParseUser> trainerList, ParseException e) {
				if (e == null) {
					mTrainer = trainerList.get(0);
				} else {
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		
		bookBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(TrainerDetailActivity.this, TrainerBookSchedule.class);
				i.putExtra("objectId", objId);
				i.putExtra("nickName", mTrainer.getString("nickName"));
				i.putExtra("lName", mTrainer.getString("lName"));
				startActivity(i);
			}
		});
	}
}
