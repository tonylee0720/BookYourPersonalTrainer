package com.ITFORCE.bookyourpt.core;

import android.app.Application;
import android.content.Intent;

import com.ITFORCE.bookyourpt.MainActivity;
import com.ITFORCE.bookyourpt.ProfActivity;
import com.ITFORCE.bookyourpt.R;
import com.ITFORCE.bookyourpt.core.User.UserInitializationListener;
import com.facebook.LoginActivity;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class Initialize extends Application implements UserInitializationListener {

	private final static String PARSE_APPID = "PMO0FytspDIR6zFY9HjNw9TXFMvqoCXEzgWLMTPU";
	private final static String PARSE_CLIENT = "T0C8sHjPbisEAt3DkejNMJxtrLrPaqlYNDtt8bJh";

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, PARSE_APPID, PARSE_CLIENT);
		ParseFacebookUtils.initialize(getString(R.string.fb_appid));
		User.initialize(this);
	}

	@Override
	public void login() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void verify() {
		Intent intent = new Intent(this, ProfActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void prompt() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
