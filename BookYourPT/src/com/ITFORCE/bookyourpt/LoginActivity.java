package com.ITFORCE.bookyourpt;

import java.util.Arrays;
import java.util.List;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ITFORCE.bookyourpt.core.User;
import com.ITFORCE.bookyourpt.core.User.UserLoginListener;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends BaseActivity implements UserLoginListener {
	com.facebook.widget.LoginButton fbButton;
	Button loginButton, regButton, loginBtn;
	LinearLayout lllogin, llbtn;
	Animation fadeIn, fadeOut, tranDown, tranUp;
	RelativeLayout mainRl;
	EditText loginUserName, loginPassWord;

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
		// btn
		fbButton = (com.facebook.widget.LoginButton) findViewById(R.id.btn_FBLogin);
		loginButton = (Button) findViewById(R.id.btn_Login);
		regButton = (Button) findViewById(R.id.btn_MemReg);
		llbtn = (LinearLayout) findViewById(R.id.login_btnll);
		// txt
		lllogin = (LinearLayout) findViewById(R.id.login_txtll);
		loginBtn = (Button) findViewById(R.id.login_btn);
		loginUserName = (EditText) findViewById(R.id.login_uname);
		loginPassWord= (EditText) findViewById(R.id.login_pw);
		fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		mainRl = (RelativeLayout) findViewById(R.id.login_main_rl);
		
		fadeIn.setFillAfter(true);
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

		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgressDialog();
				User.logIn(loginUserName.getText().toString(), loginPassWord.getText().toString(), LoginActivity.this);
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
		ObjectAnimator mover = ObjectAnimator.ofFloat(llbtn, "translationY", 0,
				mainRl.getHeight() * 0.2f);
		mover.start();
		lllogin.setVisibility(View.VISIBLE);
		lllogin.startAnimation(fadeIn);
	}

	private void animMoveBack() {
		lllogin.setVisibility(View.GONE);
		lllogin.startAnimation(fadeOut);
	}

	@Override
	public void signInSuccess(ParseUser user) {
		hideProgressDialog();
		Intent i = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(i);
		finish();
		Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void signInError(String error) {
		hideProgressDialog();
		Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
	}

}
