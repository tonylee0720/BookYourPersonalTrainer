package com.ITFORCE.bookyourpt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegActivity extends BaseActivity {
	EditText uName, Pw, Pw2, eMail;
	Button regButton;
	boolean uResult, eResult, pResult;
	String logMsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		localInit();
		functionInit();
	}

	private void localInit() {
		uName = (EditText) findViewById(R.id.txt_reg_username);
		Pw = (EditText) findViewById(R.id.txt_reg_pw);
		Pw2 = (EditText) findViewById(R.id.txt_reg_pw2);
		eMail = (EditText) findViewById(R.id.txt_reg_email);
		regButton = (Button) findViewById(R.id.btn_reg);
	}

	private void functionInit() {
		regButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isValidPassword(Pw.getText().toString(), Pw2.getText().toString());
				isValidUserName(uName.getText().toString());
				isExistEmail(eMail.getText().toString());
				
				if(uResult && eResult && pResult){
					uResult=false;
					pResult=false;
					eResult=false;
					passedActions();
				}else{
					Toast.makeText(getApplicationContext(), logMsg, Toast.LENGTH_LONG).show();
					return;
				}
			}
		});
	}
	
	private void isValidUserName(String uname){
		ParseQuery<ParseUser> emailQuery = ParseUser.getQuery();
		emailQuery.whereEqualTo("username", uname);
		emailQuery.countInBackground(new CountCallback() {
			@Override
			public void done(int counter, ParseException e) {
				if (e == null) {
					if (counter > 0) {
						logMsg += "Username Exists";
						uResult = false;
					} else {
						uResult = true;
					}
				} else {
					Log.v("", e.getMessage());
				}
			}
		});
	}
	
	private void isValidPassword(String pw, String pw2) {
		if(pw!=pw2){
			pResult = true;
		}else{
			logMsg += "Password doesn't match\t";
			pResult = false;
		}
	}

	private boolean isValidEmail(String email) {
		boolean result = false;
		result = (email.length() > 0) ? android.util.Patterns.EMAIL_ADDRESS.matcher(email)
				.matches() : false;
		return result;
	}

	private void isExistEmail(String email) {
		if (isValidEmail(email)) {
			ParseQuery<ParseUser> emailQuery = ParseUser.getQuery();
			emailQuery.whereEqualTo("email", email);
			emailQuery.countInBackground(new CountCallback() {
				@Override
				public void done(int counter, ParseException e) {
					if (e == null) {
						if (counter > 0) {
							logMsg += "Email Exists\t";
							eResult = false;
						} else {
							eResult = true;
						}
					} else {
						Log.v("", e.getMessage());
					}
				}
			});
		}else{
			return;
		}
	}
	
	private void passedActions(){
		ParseUser userObj = new ParseUser();
		userObj.setUsername(uName.getText().toString());
		userObj.setPassword(Pw.getText().toString());
		userObj.setEmail(eMail.getText().toString());
		userObj.signUpInBackground(new SignUpCallback() {
			  public void done(ParseException e) {
			    if (e == null) {
			    	Intent i = new Intent(RegActivity.this, ProfActivity.class);
			    	Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG);
			    	startActivity(i);
			    } else {
			      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
			    }
			  }
			});
	}
}
