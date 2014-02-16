package com.ITFORCE.bookyourpt;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends BaseActivity  {
	com.facebook.widget.LoginButton fbButton;
	Button loginButton, regButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		localInit();
		functionInit();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	private void localInit(){
		fbButton = (com.facebook.widget.LoginButton) findViewById(R.id.btn_FBLogin);
		loginButton = (Button)findViewById(R.id.btn_Login);
		regButton = (Button)findViewById(R.id.btn_MemReg);
	}
	private void functionInit(){
		fbButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});
		
		regButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, RegActivity.class);
				startActivity(i);
			}
		});
		
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void onLoginButtonClicked() {
		showProgressDialog();
		List<String> permissions = Arrays.asList("basic_info", "user_about_me",
				"user_relationships", "user_birthday", "user_location");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				hideProgressDialog();
				if (user == null) {
					Log.d("Facebook Related",
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d("Facebook Related",
							"User signed up and logged in through Facebook!");
					showUserDetailsActivity();
				} else {
					Log.d("Facebook Related",
							"User logged in through Facebook!");
					showUserDetailsActivity();
				}
			}
		});
	}
	
	private void showUserDetailsActivity() {
		Intent intent = new Intent(this, UserDetailsActivity.class);
		startActivity(intent);
	}

//

}
