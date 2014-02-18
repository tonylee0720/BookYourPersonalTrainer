package com.ITFORCE.bookyourpt.core;

import android.app.Activity;
import android.app.ProgressDialog;

import com.ITFORCE.bookyourpt.BaseActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class User {
	public interface UserInitializationListener {
		public void login();

		public void verify();

		public void prompt();
	}

	public interface UserLoginListener {
		public void signInSuccess(ParseUser user);

		public void signInError(String error);
	}

	public static final void initialize(UserInitializationListener listener) {
		final ParseUser user = ParseUser.getCurrentUser();
		if (user != null) { // logged in
//			new BaseActivity().showProgressDialog();
			if (user.getBoolean("verified")) { // account verified
				listener.login();
			} else { // needs to login
				listener.verify();
			}
		} else { // needs to login
			listener.prompt();
		}
	}

	public static ParseUser current() {
		return ParseUser.getCurrentUser();
	}

	public static void logOut() {
		ParseUser.logOut();
	}

	public static void logIn(String email, String pass, final UserLoginListener listener) {
		ParseUser.logInInBackground(email, pass, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if (e != null) {
					listener.signInError(e.getMessage());
				} else {
					listener.signInSuccess(user);
				}
			}
		});
	}


	public static void resetPassword(String email) {
	}
}
