package com.ITFORCE.bookyourpt;

import java.util.Arrays;
import java.util.List;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends BaseActivity {
	com.facebook.widget.LoginButton fbButton;
	Button loginButton, regButton;
	LinearLayout lllogin, llbtn;
	Animation fadeIn, fadeOut, tranDown, tranUp;
	RelativeLayout mainRl;

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

	private void localInit() {
		fbButton = (com.facebook.widget.LoginButton) findViewById(R.id.btn_FBLogin);
		loginButton = (Button) findViewById(R.id.btn_Login);
		regButton = (Button) findViewById(R.id.btn_MemReg);
		llbtn = (LinearLayout) findViewById(R.id.login_btnll);
		lllogin = (LinearLayout) findViewById(R.id.login_txtll);
		fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		// fadeIn.setFillAfter(true);
		// fadeOut.setFillAfter(true);
		// tranUp.setFillAfter(true);
		// tranDown.setFillAfter(true);
		mainRl = (RelativeLayout) findViewById(R.id.login_main_rl);
	}

	private void functionInit() {
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
				animMoveIn();
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
					Log.d("Facebook Related", "Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d("Facebook Related", "User signed up and logged in through Facebook!");
					showUserDetailsActivity();
				} else {
					Log.d("Facebook Related", "User logged in through Facebook!");
					showUserDetailsActivity();
				}
			}
		});
	}

	private void showUserDetailsActivity() {
		Intent intent = new Intent(this, UserDetailsActivity.class);
		startActivity(intent);
	}

	private void animMoveIn() {
		ObjectAnimator mover = ObjectAnimator.ofFloat(llbtn, "translationY", 0, mainRl.getHeight()*0.2f);
		mover.start();
		tranDown.setDuration(1000);
		lllogin.startAnimation(fadeIn);
		lllogin.setVisibility(View.VISIBLE);
	}

	private void animMoveBack() {
		lllogin.setVisibility(View.GONE);
		lllogin.startAnimation(fadeOut);
		llbtn.startAnimation(tranUp);
	}

}
